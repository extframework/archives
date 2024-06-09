package dev.extframework.archives.extension

import org.objectweb.asm.commons.Method

public fun Method(method: String) : Method {
    val descriptorStart = method.indexOf('(')
    val name = method.substring(0 until descriptorStart)
    val descriptor = method.substring(descriptorStart until method.length)

    check(descriptor.indexOf(')') != descriptor.lastIndex) {"Method name + descriptor has no return type! (It must)"}

    return Method(name, descriptor)
}

/**
 * Checks if the two methods provided have the same name and argument types,
 * ie. would they overload each other if in the same class.
 */
public fun Method.overloads(other: Method): Boolean {
    return name == other.name && argumentTypes.contentEquals(other.argumentTypes)
}