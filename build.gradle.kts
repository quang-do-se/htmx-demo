plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.14"
}

group = "dev.qdo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    runtimeOnly("org.webjars.npm:htmx.org:2.0.5")
    runtimeOnly("org.webjars.npm:bootstrap:5.3.7")
    runtimeOnly("org.webjars.npm:bootstrap-icons:1.13.1")
    runtimeOnly("org.webjars:webjars-locator-core:0.48")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
