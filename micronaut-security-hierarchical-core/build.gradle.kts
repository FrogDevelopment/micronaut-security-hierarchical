plugins {
    id("io.micronaut.minimal.library") version "4.3.4"
}

dependencies {
    val lombokVersion = "1.18.24"

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    // Micronaut processor defined after Lombok
    annotationProcessor("io.micronaut:micronaut-inject-java")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")

    implementation("io.micronaut.serde:micronaut-serde-jackson")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")

    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")

    testRuntimeOnly("ch.qos.logback:logback-classic")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.yaml:snakeyaml")
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.frogdevelopment.micronaut.*")
    }
}
