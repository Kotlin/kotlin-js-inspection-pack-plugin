package org.jetbrains.migration.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtFunctionType
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.typeRefHelpers.setReceiverTypeReference

object ConvertFunctionReceiverToParameter : LocalQuickFix {
    override fun getName(): String = "Move function receiver to parameter"

    override fun getFamilyName(): String = name

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val typeReference = descriptor.psiElement as? KtTypeReference ?: return
        val functionType = typeReference.typeElement as? KtFunctionType ?: return
        val functionTypeParameterList = functionType.parameterList ?: return
        val functionTypeReceiver = functionType.receiverTypeReference ?: return
        val newParameter = KtPsiFactory(project).createFunctionTypeParameter(functionTypeReceiver)
        functionTypeParameterList.addParameterBefore(newParameter, functionTypeParameterList.parameters.firstOrNull())
        functionType.setReceiverTypeReference(null)
    }

}