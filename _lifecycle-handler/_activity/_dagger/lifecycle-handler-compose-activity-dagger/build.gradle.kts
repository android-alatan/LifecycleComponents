plugins {
    id("lib-kotlin-android-no-config")
    id("lib-compose")
    id("publish-android")
    kotlin("kapt")
}

dependencies {
    api(projects.lifecycleHandlerComposeActivity)
    api(projects.daggerBaseBuilder)

    implementation(libs.google.material)

    implementation(libs.dagger.android)
    kapt(libs.dagger.baseCompiler)

}