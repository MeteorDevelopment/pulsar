plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "org.meteordev"
version = "0.1.0"

var snapshot = true

repositories {
    mavenCentral()

    maven {
        name = "Meteor - Snapshots"
        setUrl("https://maven.meteordev.org/snapshots")
    }
}

dependencies {
    api(project(":pts"))

    compileOnly(platform("org.lwjgl:lwjgl-bom:3.3.1"))

    compileOnly("org.lwjgl:lwjgl")
    compileOnly("org.lwjgl:lwjgl-glfw")
    compileOnly("org.lwjgl:lwjgl-stb")
    compileOnly("org.lwjgl:lwjgl-nanovg")

    compileOnly("org.joml:joml:1.10.5")
    compileOnly("org.meteordev:juno-api:0.1.0-SNAPSHOT")

    annotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:1.0.0")
    compileOnly("com.github.bsideup.jabel:jabel-javac-plugin:1.0.0")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    options.release.set(8)

    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })
}

tasks.withType<Jar> {
    dependsOn(project(":pts").tasks.withType<Jar>())
}

publishing {
    publications {
        create<MavenPublication>("java") {
            version = "${project.version}${if (snapshot) "-SNAPSHOT" else ""}"

            from(components["java"])
        }
    }

    repositories {
        maven {
            setUrl("https://maven.meteordev.org/${if (snapshot) "snapshots" else "releases"}")

            credentials {
                username = System.getenv("MAVEN_METEOR_ALIAS")
                password = System.getenv("MAVEN_METEOR_TOKEN")
            }

            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}
