plugins {
    id("maven-publish")
    id("org.jetbrains.dokka")
    id("signing")
}

group = "net.yakclient"
version = "1.1.3"

repositories {
    mavenCentral()
}


sourceSets {
    create("java9") {
        java.srcDir("src/main/java9")
    }
}

tasks.named<JavaCompile>("compileJava9Java") {
//    JavaLanguageVersion.of(8).
    targetCompatibility = "1.9"
    sourceCompatibility =  "1.9"
//    options.compilerArgs.addAll(arrayOf("--release", "1.9"))

    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(9))
    })

    options.compilerArgs.addAll(
        listOf(
            "--patch-module",
            "yakclient.mixins.api=${sourceSets.main.get().java.destinationDirectory.get().asFile.absolutePath}"
        )
    )
}

tasks.compileJava {
    targetCompatibility = "1.8"
    sourceCompatibility = "1.8"
//    options.compilerArgs.addAll(arrayOf("--release", "8"))

    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    })
}

tasks.jar {
    archiveBaseName.set("mixins-api")
    archiveClassifier.set("")

    into("META-INF/versions/9") {
        from(sourceSets["java9"].output)
    }
    manifest {
        attributes(
            "Multi-Release" to true
        )
    }
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

            artifactId = "mixins-api"

            pom {
                name.set("Mixins api")
                description.set("The api module for the kotlin yakclient mixin library.")
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
    sign(publishing.publications["api-maven"])
}