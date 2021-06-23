package com.jetbrains.migration.quickfixes

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass

object AddExternalQuickFix : LocalQuickFix {
    override fun getName(): String = "Add external keyword"

    override fun getFamilyName(): String = name

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val psiElement = descriptor.psiElement.parent

        if(FileModificationService.getInstance().preparePsiElementForWrite(psiElement)) {
            if(psiElement is KtClass) {
                psiElement.addModifier(KtTokens.EXTERNAL_KEYWORD)
            }
        }
    }

}