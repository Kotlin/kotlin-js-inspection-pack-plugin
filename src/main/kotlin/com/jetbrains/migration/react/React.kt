package com.jetbrains.migration.react

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.getSuperInterfaces

private val REACT_PACKAGE_ID = Name.identifier("react")
private val REACT_PACKAGE = FqName(REACT_PACKAGE_ID.identifier)
private val R_PROPS = REACT_PACKAGE.child(Name.identifier("RProps"))
private val R_STATE = REACT_PACKAGE.child(Name.identifier("RState"))

val ClassDescriptor.implementsRState: Boolean
    get() = getSuperInterfaces().any { it.fqNameSafe == R_STATE || it.implementsRState }

val ClassDescriptor.implementsRProps: Boolean
    get() = getSuperInterfaces().any { it.fqNameSafe == R_PROPS || it.implementsRProps }