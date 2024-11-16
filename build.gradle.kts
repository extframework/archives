import dev.extframework.gradle.common.commonUtil
import dev.extframework.gradle.common.extFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"

    id("dev.extframework.common") version "1.0.28"
    id("me.champeau.mrjar") version "0.1.1"
}

tasks.wrapper {
    gradleVersion = "8.5"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

multiRelease {
    targetVersions(8, 11)
}

tasks.named<Test>("java11Test") {
    description = "Runs tests in the java11Test source set"
    group = "verification"

    testClassesDirs = sourceSets["java11Test"].output.classesDirs
    classpath = sourceSets["java11Test"].runtimeClasspath

    // Use JUnit 5 if applicable
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("reflect"))
    api("org.ow2.asm:asm:9.6")
    api("org.ow2.asm:asm-util:9.6")
    api("org.ow2.asm:asm-commons:9.6")

    commonUtil(configurationName = "java11Implementation")
    commonUtil(configurationName = "api")

    "java11TestImplementation"(kotlin("test"))
    "java11TestImplementation"(sourceSets.main.get().output)
    "java11TestImplementation"(sourceSets.named("java11").get().output)
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

tasks.compileKotlin {
    kotlinJavaToolchain.toolchain.use(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    })
    kotlinOptions.jvmTarget = "1.8"
}

tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.named<KotlinCompile>("compileJava11Kotlin") {
    kotlinJavaToolchain.toolchain.use(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    })
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
}

tasks.named<JavaCompile>("compileJava11Java") {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.named<JavaCompile>("compileJava11TestJava") {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

java {
    testing {
    }
}

tasks.test {
    useJUnitPlatform()
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "dev.extframework.common")

    group = "dev.extframework"
    version = "1.4-SNAPSHOT"

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
        toolchain.languageVersion.set(JavaLanguageVersion.of(11))
    }
}