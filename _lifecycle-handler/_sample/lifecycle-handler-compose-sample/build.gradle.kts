plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    kotlin("android")

}

android {
    compileSdk = libs.versions.androidCompile.get().toInt()
    buildToolsVersion = libs.versions.androidBuildTool.get()

    defaultConfig {
        applicationId = "io.androidalatan.lifecycle.compose.handler.sample"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

}
afterEvaluate {
    android.applicationVariants.forEach { variant ->
        kotlin.sourceSets {
            val name = variant.name
            getByName(name) {
                kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.lifecycle.viewmodelKtx)
    implementation(libs.androidx.lifecycle.livedataKtx)

    implementation(libs.common.rxjava)
    implementation(libs.common.rxjavaAndroid)
    implementation(libs.google.material)

    implementation(libs.compose.ui)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtimeLiveData)
    implementation(libs.compose.material)

    implementation(libs.alatan.rxjava.scheduler.api)
    implementation(libs.alatan.bundledata.impl)

    implementation(projects.lifecycleHandlerComposeActivity)
    implementation(projects.viewEventAdapterFlow)
    implementation(projects.viewEventAdapterRx)
    implementation(projects.lifecycleHandlerInvokeadapterApi)
    implementation(projects.lifecycleHandlerInvokeadapterFlow)
    implementation(projects.lifecycleHandlerInvokeadapterRx)

    implementation(projects.providedComposeLocalApi)
    ksp(projects.providedComposeLocalKsp)

}