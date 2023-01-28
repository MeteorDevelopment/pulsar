plugins {
    id("java")
    id("application")
}

group = "org.meteordev"
version = "0.1.0"
var mainClassName = "$group.example.musicplayer.MusicPlayer"

repositories {
    mavenCentral()

    maven {
        name = "Meteor - Snapshots"
        setUrl("https://maven.meteordev.org/snapshots")
    }
}

val lwjglNatives = Pair(
        System.getProperty("os.name")!!,
        System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else
                "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }                ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        arrayOf("Windows").any { name.startsWith(it) }                           ->
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else
                "natives-windows-x86"
        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

dependencies {
    implementation(project(":pulsar"))

    implementation("com.udojava:EvalEx:2.7")
    implementation("com.badlogicgames.jlayer:jlayer:1.0.2-gdx")
    implementation("com.mpatric:mp3agic:0.9.1")

    implementation("org.meteordev:juno-api:0.1.0-SNAPSHOT")
    implementation("org.meteordev:juno-opengl:0.1.0-SNAPSHOT")

    implementation(platform("org.lwjgl:lwjgl-bom:3.3.1"))

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")
    implementation("org.lwjgl:lwjgl-nanovg")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.joml:joml:1.10.2")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nanovg::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

tasks.withType<Jar> {
    dependsOn(project(":pulsar").tasks.withType<Jar>())

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes("Main-Class" to mainClassName)
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
