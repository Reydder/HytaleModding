plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
}

group = "com.reydder"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(files("../../hytale_server/Server/HytaleServer.jar"))
    testImplementation(kotlin("test"))
    implementation("com.google.code.gson:gson:2.13.2")

    // Mod implementations
    //implementation(files("./mods/ZombiesGame.jar"))
    //implementation(files("./mods/MultipleHUD-1.0.5.jar"))
}

tasks.test {
    useJUnitPlatform()
}