plugins {
    id("org.jetbrains.intellij") version "1.6.0"
    kotlin("jvm") version "1.6.21"
}

group = "org.jetbrains"
version = "0.0.11"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.3.2")
    plugins.set(listOf("java", "Kotlin"))
    updateSinceUntilBuild.set(false)
}

tasks {
    patchPluginXml {
        sinceBuild.set("203")
    }
}
