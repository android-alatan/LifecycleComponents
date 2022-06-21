plugins {
    id("lib-jvm")
    id("code-quality")
    id("publish-jvm")
    id("com.android.lint")
}

dependencies {
    compileOnly(libs.kotlin.stdlib.jdk8)
    compileOnly(libs.androidLint.api)

    testImplementation(libs.androidLint.impl)
    testImplementation(libs.androidLint.test)
    testImplementation(libs.test.junit4)
}

val jar:Jar by tasks
jar.manifest {
    attributes("Lint-Registry-v2" to "io.androidalatan.lifecycle.handler.lint.LifecycleLintIssueRegistry")
}