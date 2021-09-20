group = "net.yakclient"
version = "1.1"

dependencies {
    api(project(":base"))
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation("org.reflections:reflections:0.9.12")
}

tasks.test {
    useJUnitPlatform()
}