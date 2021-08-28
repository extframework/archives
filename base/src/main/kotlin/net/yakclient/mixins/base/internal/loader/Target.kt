package net.yakclient.mixins.base.target

import java.net.URL

interface Target {
    /**
    * Decides if the current path is equally or less
    * specific than then one given.
    *
    * @param target The path to compare against.
    * @return if the given path could be a child of the current target.
    */
    fun name(): String
}
data class ModuleTarget(private val module: Module) : Target {
    val moduleName: String = module.name

    override fun name(): String {
        return moduleName
    }

    fun hasClass(name: String): Boolean {
        return module.packages.contains(
            name.split(".").let { it.subList(0, it.size - 1).joinToString(separator = ".") })
    }


}
data class JarTarget(val url: URL) : Target {
    override fun name(): String {
        return url.file
    }
}