plugins {
    id("io.micronaut.platform.catalog") version "4.4.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

rootProject.name = "micronaut-security-hierarchical"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":micronaut-security-hierarchical-core")
