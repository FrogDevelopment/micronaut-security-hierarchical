plugins {
    java
}

version = "0.0.1"
group = "com.frogdevelopment.micronaut.security"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.wrapper {
    gradleVersion = "8.10"
    distributionType = Wrapper.DistributionType.ALL
}

