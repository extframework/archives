pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.extframework.dev/releases")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "archives"

include(":mixin")
include("test-jar")
