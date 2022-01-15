plugins {
    id("maven-publish")
    id("org.jetbrains.dokka")
    id("signing")
}

group = "net.yakclient"
version = "1.0-SNAPSHOT"

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
            "yakclient.bmu.mixin=${sourceSets.main.get().java.destinationDirectory.get().asFile.absolutePath}"
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
    modularDependency(project(":api"), "api")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
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
        create<MavenPublication>("mixin-maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            artifactId = "bmu-mixin"

            pom {
                name.set("BMU mixins")
                description.set("A mixin module for the YakClient Bytecode Manipulation Utils")
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
    sign(publishing.publications["mixin-maven"])
}