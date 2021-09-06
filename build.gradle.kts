plugins {
    kotlin("jvm") version "1.5.30"
//    kotlin("jvm") version "1.5.21"


    id( "org.springframework.boot") version "2.4.7"
}

group = "net.yakclient"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


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
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        destinationDir = tasks.compileJava.get().destinationDirectory.asFile.get()
        kotlinOptions.jvmTarget = "11"

    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.compileJava {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    
}