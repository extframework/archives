plugins {
    id("maven-publish")
    id("org.jetbrains.dokka")
    id("signing")
}

group = "net.yakclient"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.30")


    api("org.ow2.asm:asm:9.2")
    api("org.ow2.asm:asm-util:9.2")
    api(project(":api"))
    implementation(project(":base.agent"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.jar {
    archiveBaseName.set("mixins-base")
    archiveClassifier.set("")
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
        create<MavenPublication>("base-maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            artifactId = "mixins-base"

            pom {
                name.set("Mixins base")
                description.set("The base for a Kotlin library for doing basic bytecode manipulation, redefinition and mixins")
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
signing {
    sign(publishing.publications["base-maven"])
}




