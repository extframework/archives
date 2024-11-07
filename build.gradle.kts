import dev.extframework.gradle.common.commonUtil
import dev.extframework.gradle.common.extFramework

plugins {
    kotlin("jvm") version "1.9.21"

    id("dev.extframework.common") version "1.0.28"
}

tasks.wrapper {
    gradleVersion = "8.5"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    implementation(kotlin("reflect"))
    api("org.ow2.asm:asm:9.6")
    api("org.ow2.asm:asm-util:9.6")
    api("org.ow2.asm:asm-commons:9.6")

    commonUtil(configurationName = "api")
}

common {
    publishing {
        publication {
            withJava()
            withSources()
            withDokka()

            artifactId = "archives"

            commonPom {
                name.set("Archives")
                description.set("The base module for YakClient Archive operations.")
                url.set("https://github.com/extframework/archives")

                packaging = "jar"

                defaultDevelopers()
                withExtFrameworkRepo()
                gnuLicense()
                extFrameworkScm("archives")
            }
        }
    }
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "dev.extframework.common")

    group = "dev.extframework"
    version = "1.3-SNAPSHOT"

    repositories {
        mavenCentral()
        extFramework()
    }

    common {
        defaultJavaSettings()
        publishing {
            repositories {
                extFramework(credentials = propertyCredentialProvider)
            }
        }
    }

    kotlin {
        explicitApi()
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
        testImplementation(kotlin("test"))
    }
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    }
}