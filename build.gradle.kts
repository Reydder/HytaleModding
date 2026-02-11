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
}

tasks.test {
    useJUnitPlatform()
}