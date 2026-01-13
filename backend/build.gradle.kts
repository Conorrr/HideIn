plugins {
    java
    application
    id("com.gradleup.shadow") version "8.3.5"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.4.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.apache.pdfbox:pdfbox:3.0.3")
}

application {
    mainClass = "dev.hidein.App"
}
