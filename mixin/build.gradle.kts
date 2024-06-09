dependencies {
    api(project(":"))
}

common {
    publishing {
        publication {
            withJava()
            withSources()
            withDokka()

            artifactId = "archives-mixin"

            commonPom {
                name.set("Archive mixins")
                description.set("A mixin module for the YakClient Archive library")
                url.set("https://github.com/extframework/archives")

                packaging = "jar"

                withExtFrameworkRepo()

                defaultDevelopers()
                gnuLicense()
                extFrameworkScm("archives")
            }
        }
    }
}