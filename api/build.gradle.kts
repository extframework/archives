plugins {
    id("org.jetbrains.dokka")
}

group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy-agent:1.11.15")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.30")

    api("org.ow2.asm:asm:9.2")
    api("org.ow2.asm:asm-util:9.2")
}

task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

task<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications {
        create<MavenPublication>("api-maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            artifactId = "bmu-api"

            pom {
                name.set("Bmu Api")
                description.set("The base API module for YakClient Bytecode manipulation utils")
                url.set("https://github.com/yakclient/mixins")

                packaging = "jar"

                developers {
                    developer {
                        id.set("Chestly")
                        name.set("Durgan McBroom")
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License")
                        url.set("https://opensource.org/licenses/gpl-license")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/yakclient/mixins")
                    developerConnection.set("scm:git:ssh://github.com:yakclient/mixins.git")
                    url.set("https://github.com/yakclient/mixins")
                }
            }
        }
    }
}




