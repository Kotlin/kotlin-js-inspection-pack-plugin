/*
 * Copyright (c) 2021-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.migration.actions

import com.intellij.codeInsight.FileModificationService
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.migration.core.IRInterfaceCodeInsightAction
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
                runWriteAction { targetClass.addModifier(KtTokens.EXTERNAL_KEYWORD) }
            }
        }
     }
}