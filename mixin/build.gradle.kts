plugins {
    id("org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}


dependencies {
    api(project(":"))
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

            artifactId = "archives-mixin"

            pom {
                name.set("Archive mixins")
                description.set("A mixin module for the YakClient Archive library")
                url.set("https://github.com/yakclient/archives")

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
                    connection.set("scm:git:git://github.com/yakclient/archives")
                    developerConnection.set("scm:git:ssh://github.com:yakclient/archives.git")
                    url.set("https://github.com/yakclient/archives")
                }
            }
        }
    }
}