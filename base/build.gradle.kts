group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.reflections:reflections:0.9.12")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.5.30")

    api("org.ow2.asm:asm:9.1")
    api("org.ow2.asm:asm-util:9.1")
    api(project(":api"))
    implementation(project(":base.agent"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}


