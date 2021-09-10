group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2") {
        exclude(group="org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    }
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.5.30")

    api("org.ow2.asm:asm:9.2")
    api("org.ow2.asm:asm-util:9.2")
    api(project(":api"))
    implementation(project(":base.agent"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}


