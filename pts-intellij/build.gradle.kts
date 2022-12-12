import org.jetbrains.intellij.tasks.BuildSearchableOptionsTask

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.9.0"
}

group = "meteordevelopment"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4-intellij-adaptor:0.1")

    implementation(project(":pts"))
}

intellij {
    version.set("2022.2.3")
}

tasks.withType<BuildSearchableOptionsTask> {
    enabled = false
}

tasks.withType<Jar> {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
