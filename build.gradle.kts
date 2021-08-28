plugins {
    kotlin("jvm") version "1.5.21"


    id( "org.springframework.boot") version "2.4.7"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation(kotlin("stdlib"))

//    implementation( "org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":base"))
}

tasks.wrapper {
    gradleVersion = "7.1.1"
//    distributionType = Wrapper.DistributionType.ALL
}

subprojects {
    apply( plugin= "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))
//        implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        destinationDir = tasks.compileJava.get().destinationDirectory.asFile.get()
    }

    tasks.test {
        useJUnitPlatform()
    }
}