plugins {
    kotlin("jvm") version "1.5.30"

    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    id("signing")
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.32"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
//    id("org.javamodularity.moduleplugin") version "1.8.9"

//    id("java")
//    id("java-library")
}

group = "net.yakclient"
version = "1.1"

//tasks.compileJ

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

//    implementation(project(":base"))
}

tasks.wrapper {
    gradleVersion = "7.2"
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("mavenUsername") as String)
            password.set(project.findProperty("mavenPassword") as String)
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.gitlab.arturbosch.detekt")
//    apply(plugin = "org.javamodularity.moduleplugin")
//    apply(plugin = "java")
//    apply(plugin = "java-library")
//    apply(plugin = "signing")

    repositories {
        mavenCentral()
    }

    kotlin {
        explicitApi()
//        sourceSets["java9"]
    }


//    modularity.mixedJavaRelease("8")


//    tasks.compileJava {
//        sourceCompatibility = 8
//        targetCompatibility = 8
//    }

//    tasks.compileJava
//    tasks.compileJ {
//        sourceCompatibility = 9
//        targetCompatibility = 9
//    }

    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        destinationDirectory.set(tasks.compileJava.get().destinationDirectory.asFile.get())
        kotlinOptions.jvmTarget = "11"
    }


}