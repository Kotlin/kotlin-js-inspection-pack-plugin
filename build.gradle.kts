plugins {
    id("org.jetbrains.intellij") version "1.9.0"
    kotlin("jvm") version "1.7.21"
}

group = "org.jetbrains"
version = "0.0.12"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("223-EAP-SNAPSHOT")
    plugins.set(listOf("java", "Kotlin"))
//    updateSinceUntilBuild.set(false)
}

tasks {
    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("223.*")
    }
}
