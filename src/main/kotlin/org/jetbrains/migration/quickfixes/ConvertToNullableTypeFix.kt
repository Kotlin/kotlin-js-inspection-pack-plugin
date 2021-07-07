/*
 * Copyright (c) 2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.migration.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.core.setType
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.types.typeUtil.makeNullable

class ConvertToNullableTypeFix : LocalQuickFix {
    override fun getName(): String = "Convert to nullable type"

    override fun getFamilyName(): String = name

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val parameter = descriptor.psiElement as? KtProperty ?: return
        val type = parameter.type() ?: return
        parameter.setType(type.makeNullable())
    }
}