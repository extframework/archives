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
    targetCompatibility = "1.9"
    sourceCompatibility = "1.9"

    options.compilerArgs.addAll(
        listOf(
            "--patch-module",
            "yakclient.mixins.base=${sourceSets.main.get().java.destinationDirectory.get().asFile.absolutePath}"
        )
    )
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.compileJava {
    targetCompatibility = "1.8"
    sourceCompatibility = "1.8"
}

tasks.jar {
    archiveBaseName.set("mixins-base")
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

fun DependencyHandlerScope.modularDependency(dependency: Any, configuration: String = "implementation") {
    add(configuration, dependency)
    add("java9${configuration.capitalize()}" /* Sucks but i cant be bothered to do something better */, dependency)
}

dependencies {
    modularDependency("net.bytebuddy:byte-buddy-agent:1.11.15")

    modularDependency("org.jetbrains.kotlin:kotlin-reflect:1.5.30")
    modularDependency(project(":api"), "api")
    modularDependency("org.ow2.asm:asm:9.2", "api")
    modularDependency("org.ow2.asm:asm-util:9.2", "api")
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




