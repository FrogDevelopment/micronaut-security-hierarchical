plugins {
    java
}

version = "0.0.1"
group = "com.frogdevelopment.micronaut.security"

java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.wrapper {
    gradleVersion = "8.10"
    distributionType = Wrapper.DistributionType.ALL
}

