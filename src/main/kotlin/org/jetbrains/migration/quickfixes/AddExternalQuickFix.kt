/*
 * Copyright (c) 2021-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.migration.quickfixes

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.migration.KotlinJsInspectionPackBundle

object AddExternalQuickFix : LocalQuickFix {
    override fun getName(): String = KotlinJsInspectionPackBundle.message("add.external.keyword")

    override fun getFamilyName(): String = name

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val psiElement = descriptor.psiElement.parent
        if (psiElement is KtClass) {
            psiElement.addModifier(KtTokens.EXTERNAL_KEYWORD)
        }
    }
}
