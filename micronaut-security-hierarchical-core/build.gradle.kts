plugins {
    id("io.micronaut.minimal.library") version "4.4.2"
}

dependencies {
    compileOnly(mn.lombok)
    annotationProcessor(mn.lombok)
    annotationProcessor(mn.micronaut.inject.java)
    annotationProcessor(mn.micronaut.serde.processor)

    implementation(mn.micronaut.security)
    implementation(mn.micronaut.security.jwt)
    implementation(mn.micronaut.serde.jackson)

    testImplementation(mn.assertj.core)
    testImplementation(mn.mockito.junit.jupiter)

    testRuntimeOnly(mn.logback.classic)
    testRuntimeOnly(mn.snakeyaml)

    testAnnotationProcessor(mn.lombok)
    testCompileOnly(mn.lombok)
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.frogdevelopment.micronaut.*")
    }
}
