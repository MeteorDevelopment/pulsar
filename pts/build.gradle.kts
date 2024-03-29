plugins {
    id("java")
    id("java-library")
    id("antlr")
    id("maven-publish")
}

group = "org.meteordev"
version = "0.1.0"

var snapshot = true

repositories {
    mavenCentral()
}

dependencies {
    // ANTLR
    antlr("org.antlr:antlr4:4.11.1")
    api("org.antlr:antlr4-runtime:4.11.1")

    // Jabel
    annotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:1.0.0")
    compileOnly("com.github.bsideup.jabel:jabel-javac-plugin:1.0.0")
}

configurations.api {
    exclude(group = "org.antlr", module = "antlr4")
}

sourceSets {
    create("generated") {
        java.srcDir("${projectDir}/src/generated/java")
    }

    main {
        java.srcDirs("${projectDir}/src/generated/java")
    }
}

tasks.withType<AntlrTask> {
    maxHeapSize = "64m"
    outputDirectory = file("${projectDir}/src/generated/java/org.meteordev/pts")

    arguments.add("-package")
    arguments.add("org.meteordev.pts")
    arguments.add("-visitor")
}

tasks.withType<JavaCompile> {
    dependsOn("generateGrammarSource")

    sourceCompatibility = JavaVersion.VERSION_17.toString()
    options.release.set(8)

    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })
}

tasks.named("clean") {
    delete("${projectDir}/src/generated")
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
