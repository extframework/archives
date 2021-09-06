group = "net.yakclient"
version = "1.0-SNAPSHOT"

dependencies {
    api( project(":base"))
    implementation(project(":base.agent"))
    implementation( project(":base.test.example"))
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.11.15")

}

