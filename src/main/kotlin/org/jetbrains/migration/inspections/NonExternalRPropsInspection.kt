/*
 * Copyright (c) 2021-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.migration.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.migration.quickfixes.AddExternalQuickFix
import org.jetbrains.migration.react.implementsRProps
import org.jetbrains.migration.react.implementsRState
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.project.platform
import org.jetbrains.kotlin.idea.search.usagesSearch.descriptor
import org.jetbrains.kotlin.platform.js.isJs
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.classOrObjectVisitor

class NonExternalRPropsInspection : AbstractKotlinInspection() {

    override fun buildVisitor(
        holder: ProblemsHolder, isOnTheFly: Boolean
    ): PsiElementVisitor = classOrObjectVisitor { classOrObject ->
        if (classOrObject.platform.isJs()) {
            val classDescriptor = classOrObject.descriptor as? ClassDescriptor ?: return@classOrObjectVisitor
            if (classDescriptor.implementsRProps || classDescriptor.implementsRState) {
                if(classOrObject is KtClass) {
                    if (classOrObject.isInterface() && !classDescriptor.isExternal) {
                        val nameIdentifier = classOrObject.nameIdentifier ?: return@classOrObjectVisitor
                        holder.registerProblem(nameIdentifier, "Interface should be external", AddExternalQuickFix)
                    } else {
                        val classKeyword = classOrObject.getClassKeyword() ?: return@classOrObjectVisitor
                        holder.registerProblem(classKeyword, "Class should be external interface")
                    }
                }
                if(classOrObject is KtObjectDeclaration){
                    val objectKeyword = classOrObject.getObjectKeyword() ?: return@classOrObjectVisitor
                    holder.registerProblem(objectKeyword, "Object should be external interface")
                }
            }
        }
    }
}