/*
 * Copyright (c) 2021-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.migration.react

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.getSuperInterfaces

private val REACT_PACKAGE_ID = Name.identifier("react")
private val REACT_PACKAGE = FqName(REACT_PACKAGE_ID.identifier)
private val R_PROPS = REACT_PACKAGE.child(Name.identifier("RProps"))
private val R_STATE = REACT_PACKAGE.child(Name.identifier("RState"))
private val STATE = REACT_PACKAGE.child(Name.identifier("State"))

val ClassDescriptor.implementsRState: Boolean
    get() = getSuperInterfaces().any { it.fqNameSafe == STATE || it.fqNameSafe == R_STATE || it.implementsRState }

val ClassDescriptor.implementsRProps: Boolean
    get() = getSuperInterfaces().any { it.fqNameSafe == R_PROPS || it.implementsRProps }