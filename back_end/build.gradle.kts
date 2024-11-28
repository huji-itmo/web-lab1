
plugins {
    id("java")
    id("io.freefair.lombok") version "8.10"
}

group = "org.example"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))));
    implementation("com.google.code.gson:gson:2.11.0");
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("--release", "17"))
}

tasks.compileJava{
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.create("deploy_helios") {

    dependsOn("jar")

    doLast {
        exec {
            workingDir(".")
            commandLine("bash", "scripts/deploy_helios.sh")
        }
    }
}

tasks.create("deploy_docker") {

    dependsOn("jar")

    doLast {
        exec {
            workingDir(".")
            commandLine("bash", "scripts/deploy_docker.sh")
        }
    }
}