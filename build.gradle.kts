import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.2.0-RC"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "tech.bufallo.pw.scz"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("tech.bufallo.pw.scz.MainKt")
}

repositories {
    mavenCentral()
}

val clicktVersion = "5.0.3"
val junitVersion = "5.11.1"

dependencies {
    implementation("com.github.ajalt.clikt:clikt:$clicktVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("crc-calculator.jar")
        mergeServiceFiles()
        exclude("**/CrcCalculatorTest.class")
        exclude("**/kotlin/test/**")
        exclude("**/org/junit/**")
    }

    build {
        dependsOn(shadowJar)
    }
}