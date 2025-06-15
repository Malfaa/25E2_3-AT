plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //Javalin
    implementation("io.javalin:javalin:6.6.0")
    //Database
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.jdbi:jdbi3-core:3.43.0")
    implementation("org.jdbi:jdbi3-sqlobject:3.43.0")

    //Jackson JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")
    implementation("org.slf4j:slf4j-simple:2.0.9")


    //JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    //Javalin
    testImplementation("io.javalin:javalin-testtools:6.6.0")

    //OKHTTP
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")

    //Mockito
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
}

tasks.test {
    useJUnitPlatform()
}