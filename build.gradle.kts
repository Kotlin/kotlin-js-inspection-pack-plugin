plugins {
    id("org.jetbrains.intellij") version "1.2.1"
    kotlin("jvm") version "1.5.31"
}

group = "org.jetbrains"
version = "0.0.6"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.2.3")
    plugins.set(listOf("java","Kotlin"))
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
//    changeNotes("""
//      Add change notes here.<br>
//      <em>most HTML tags may be used</em>""")
}

tasks {
    patchPluginXml {
        sinceBuild.set("203")
        untilBuild.set("213.*")
    }
}