package org.jetbrains.migration.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.project.platform
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.platform.js.isJs
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.declarationVisitor
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.migration.quickfixes.ConvertFunctionReceiverToParameter

class FunctionsWithReceiverInExternalInterfaceInspection : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor = declarationVisitor { declaration ->
        if(!declaration.platform.isJs()) return@declarationVisitor
        val parent = declaration.containingClassOrObject as? KtClass ?: return@declarationVisitor
        if(parent.isInterface() && parent.hasModifier(KtTokens.EXTERNAL_KEYWORD)) {
            val property = declaration as? KtProperty ?: return@declarationVisitor
            val functionType = property.typeReference?.typeElement as? KtFunctionType ?: return@declarationVisitor
            val functionTypeReceiver = functionType.receiverTypeReference
            if(functionTypeReceiver != null) {
                val typeRef = property.typeReference ?: return@declarationVisitor
                holder.registerProblem(typeRef, "External interface can't contain functions with receivers",
                ConvertFunctionReceiverToParameter)
            }
        }
    }
}