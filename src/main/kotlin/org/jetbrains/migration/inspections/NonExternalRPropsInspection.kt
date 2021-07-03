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
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtVisitorVoid

class NonExternalRPropsInspection : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return NonExternalRPropsVisitor(holder)
    }
}

class NonExternalRPropsVisitor(
    private val holder: ProblemsHolder,
) : KtVisitorVoid() {
    override fun visitKtFile(file: KtFile) {
        if(file.platform.isJs()) {
            super.visitKtFile(file)
        }
    }

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        if (classOrObject.containingKtFile.platform.isJs() && classOrObject is KtClass && classOrObject.isInterface()) {
            val classDescriptor = classOrObject.descriptor as? ClassDescriptor ?: return
            if(!classDescriptor.isExternal && (classDescriptor.implementsRProps || classDescriptor.implementsRState)) {
                val nameIdentifier = classOrObject.nameIdentifier ?: return
                holder.registerProblem(nameIdentifier, "Interface should be external", AddExternalQuickFix)
            }
        }
    }
}