package com.jetbrains.migation

import com.intellij.codeInsight.FileModificationService
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.migation.core.IRInterfaceCodeInsightAction
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile

class ExternalizeInterfaceAction : IRInterfaceCodeInsightAction() {

    override fun isValidForClass(targetClass: KtClassOrObject): Boolean =
        targetClass is KtClass && targetClass.isInterface() && targetClass.hasModifier(KtTokens.EXTERNAL_KEYWORD).not()

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val targetClass = getTargetClassOrNull(editor, file) ?: return

        if (file is KtFile && FileModificationService.getInstance().prepareFileForWrite(targetClass.containingKtFile)) {
            if(targetClass.hasModifier(KtTokens.EXTERNAL_KEYWORD).not()) {
                targetClass.addModifier(KtTokens.EXTERNAL_KEYWORD)
            }
        }
     }
}