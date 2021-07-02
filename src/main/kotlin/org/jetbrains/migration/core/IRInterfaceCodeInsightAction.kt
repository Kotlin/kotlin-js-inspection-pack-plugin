package org.jetbrains.migration.core

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.CodeInsightAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.project.platform
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.getNonStrictParentOfType
import org.jetbrains.kotlin.idea.refactoring.canRefactor
import org.jetbrains.kotlin.platform.js.isJs

abstract class IRInterfaceCodeInsightAction : CodeInsightAction(), CodeInsightActionHandler {
    override fun isValidForFile(project: Project, editor: Editor, file: PsiFile): Boolean {
        if(file !is KtFile || file.isCompiled) return false
        val targetClass = getTargetClassOrNull(editor, file) ?: return false
        return targetClass.canRefactor() && isValidForClass(targetClass) && file.platform.isJs()
    }

    override fun getHandler(): CodeInsightActionHandler = this

    override fun startInWriteAction(): Boolean = false

    protected fun getTargetClassOrNull(editor: Editor, psiFile: PsiFile) : KtClassOrObject? {
        return psiFile.findElementAt(editor.caretModel.offset)?.getNonStrictParentOfType<KtClassOrObject>()
    }

    protected abstract fun isValidForClass(targetClass: KtClassOrObject): Boolean
}