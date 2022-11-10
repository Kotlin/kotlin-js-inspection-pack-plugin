/*
 * Copyright (c) 2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.migration.inspections

import com.intellij.codeInspection.IntentionWrapper
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.project.platform
import org.jetbrains.kotlin.idea.codeinsights.impl.base.quickFix.ChangeVariableMutabilityFix
import org.jetbrains.kotlin.idea.search.usagesSearch.descriptor
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.platform.js.isJs
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.declarationVisitor
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.migration.KotlinJsInspectionPackBundle
import org.jetbrains.migration.react.implementsRProps
import org.jetbrains.migration.react.implementsRState

class NonVarPropertyInExternalInterfaceInspection : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor = declarationVisitor { declaration ->
        if (!declaration.platform.isJs()) return@declarationVisitor
        val parent = declaration.containingClassOrObject as? KtClass ?: return@declarationVisitor
        val property = declaration as? KtProperty ?: return@declarationVisitor
        val parentClassDescriptor = parent.descriptor as? ClassDescriptor ?: return@declarationVisitor
        val parentImplementsReactStateOrProps =
            parentClassDescriptor.implementsRProps || parentClassDescriptor.implementsRState
        if (parent.isInterface() && parent.hasModifier(KtTokens.EXTERNAL_KEYWORD) && parentImplementsReactStateOrProps && !property.isVar) {
            holder.registerProblem(
                property.valOrVarKeyword,
                KotlinJsInspectionPackBundle.message("property.in.external.interface.should.be.var"),
                IntentionWrapper(ChangeVariableMutabilityFix(property, true), declaration.containingFile)
            )
        }
    }
}
