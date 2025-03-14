plugins {
    kotlin("jvm")
    //id("io.ktor.plugin") version "3.1.0"
    id("application")
}

/*repositories {
    mavenCentral()
}*/

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Reflection support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // If using coroutines

    // Ktor server
    implementation("io.ktor:ktor-server-core:3.1.0")
    implementation("io.ktor:ktor-server-netty:3.1.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.1.0")
    implementation("io.ktor:ktor-serialization-jackson:3.1.0")
    implementation("io.ktor:ktor-server-call-logging:3.1.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2") // Needed for date attributes

    // Exposed ORM Kotlin SQL framework for easier maintenance
    implementation("org.jetbrains.exposed:exposed-core:0.59.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.59.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.59.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.59.0")

    // PostgreSQL JDBC driver for the studybuddydatabase
    implementation("org.postgresql:postgresql:42.6.0")

    // ZXing for QR code generation
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    // Logback Classic for logging
    implementation("ch.qos.logback:logback-classic:1.4.8")

    // JUnit for unit tests
    testImplementation("junit:junit:4.13.2")

    // SLF4J logger for checking connections
    implementation("org.slf4j:slf4j-api:2.0.7")
}

application {
    mainClass.set("com.example.studybuddybackend.ApplicationKt")
}
