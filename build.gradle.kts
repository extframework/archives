import dev.extframework.gradle.common.commonUtil
import dev.extframework.gradle.common.dm.resourceApi
import dev.extframework.gradle.common.extFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"

    id("dev.extframework.common") version "1.0.43"
}

tasks.wrapper {
    gradleVersion = "8.5"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

sourceSets {
    create("java11")
    create("java11Test") {
        compileClasspath += sourceSets["main"].output + configurations["testCompileClasspath"]
        runtimeClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
    }
    create("mr")
}

task<Test>("java11Test") {
    description = "Runs tests in the java11Test source set"
    group = "verification"

    testClassesDirs = sourceSets["java11Test"].output.classesDirs
    classpath = sourceSets["java11Test"].runtimeClasspath

    // Use JUnit 5 if applicable
    useJUnitPlatform()
}

task<Jar>("java11Jar") {
    from(sourceSets.named("java11").get().output)
    archiveClassifier = "jdk11"
}

dependencies {
    implementation(kotlin("reflect"))
    api("org.ow2.asm:asm:9.6")
    api("org.ow2.asm:asm-util:9.6")
    api("org.ow2.asm:asm-commons:9.6")

    commonUtil(configurationName = "java11Implementation")
    commonUtil(configurationName = "mrImplementation")
    commonUtil(configurationName = "api")
//    resourceApi()

    "mrImplementation"(sourceSets.main.get().output)

    "java11Implementation"(sourceSets.main.get().output)
    "java11TestImplementation"("org.junit.jupiter:junit-jupiter-api:5.11.3")
    "java11TestImplementation"(kotlin("test"))
    "java11TestImplementation"(sourceSets.main.get().output)
    "java11TestImplementation"(sourceSets.named("java11").get().output)
    "java11TestRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

common {
    publishing {
        publication {
            withJava()
            withSources()
            withDokka()
            artifact(tasks.named("java11Jar")).classifier = "jdk11"

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

tasks.jar {
    from(sourceSets["mr"].output) {
        into("META-INF/versions/11")
    }
    manifest {
        attributes("Multi-Release" to "true")
    }
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


tasks.named<KotlinCompile>("compileMrKotlin") {
    kotlinJavaToolchain.toolchain.use(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    })
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
}

tasks.named<JavaCompile>("compileMrJava") {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.named<JavaCompile>("compileJava11TestJava") {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.test {
    useJUnitPlatform()
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "dev.extframework.common")

    group = "dev.extframework"
    version = "1.5-SNAPSHOT"

    repositories {
        mavenCentral()
        extFramework()
        mavenLocal()
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