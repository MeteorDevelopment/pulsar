plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "meteordevelopment"
version = "0.1.0"

var snapshot = true

repositories {
    mavenCentral()
}

dependencies {
    api(project(":pts"))

    compileOnly(platform("org.lwjgl:lwjgl-bom:3.3.1"))

    compileOnly("org.lwjgl:lwjgl")
    compileOnly("org.lwjgl:lwjgl-glfw")
    compileOnly("org.lwjgl:lwjgl-opengl")
    compileOnly("org.lwjgl:lwjgl-stb")
    compileOnly("org.lwjgl:lwjgl-nanovg")

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
