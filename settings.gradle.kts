dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
            content {
                includeGroupByRegex("com\\.github\\.android-alatan.*")
            }
        }

    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "LifecycleComponents"
include(":coroutine-test-util")

// view interaction
include(
    ":view-event-api",
    ":view-event-adapter-flow",
    ":view-event-adapter-rx",
    ":view-event-assertion",
    ":view-event-viewinteractionstream",
    ":view-event-legacy-impl",
    ":view-event-compose-extension-api",
    ":view-event-compose-extension-impl",
    ":view-event-compose-impl"
)

// bundle-collection
include(
    ":bundle-collector-api",
    ":bundle-collector",
    ":bundle-collector-assertion",
    ":bundle-collector-rx-adapter",
    ":bundle-collector-flow-adapter",
)

// router
include(
    ":router-api",
    ":router",
    ":router-assertion",
    ":router-web-api",
)

// permission
include(
    ":request-permission-api",
    ":request-permission",
    ":request-permission-flow-handler",
    ":request-permission-rx-handler",
    ":request-permission-assertion",
)

// back key handler
include(
    ":back-key-handler-api",
    ":back-key-handler",
    ":back-key-handler-assertion",
    ":back-key-handler-adapter-flow",
    ":back-key-handler-adapter-rx",
)

// result handler
include(
    ":result-handler-api",
    ":result-handler",
    ":result-handler-assertion",
    ":result-handler-flow-adapter",
    ":result-handler-rx-adapter",
)

// lifecycle handler
include(
    ":lifecycle-handler-api",
    ":lifecycle-handler-annotations",
    ":lifecycle-handler-assertion",
    ":lifecycle-handler-invokeadapter-api",
    ":lifecycle-handler-invokeadapter-rx",
    ":lifecycle-handler-invokeadapter-flow",
    ":lifecycle-handler-invokeadapter-test",
    ":lifecycle-handler-impl",
    ":lifecycle-handler-lint",

    ":lifecycle-handler-compose-util",
    ":composable-holder",
    ":composable-lifecycle-listener-activator",
    ":provided-compose-local-api",
    ":provided-compose-local-ksp",

    ":lifecycle-handler-activity",
    ":lifecycle-handler-compose-activity",
    ":lifecycle-handler-activity-dagger",
    ":lifecycle-handler-compose-activity-dagger",
)

include(
    ":dagger-scope",
    ":dagger-base-builder",
)

include(
    ":lifecycle-handler-sample",
    ":lifecycle-handler-compose-sample",
)

// common
include(
    ":lazy-provider"
)

include(
    ":coroutine-api"
)

val modules = hashMapOf<String, String>()

rootProject.projectDir.listFiles()
    ?.forEach {
        findSubProjects(it)
    }

fun findSubProjects(file: File) {
    if (file.name.startsWith(".")) {
        return
    }

    if (file.name == "build.gradle.kts" || file.name == "build.gradle") {
        modules[file.parentFile.name] = file.parentFile.path
        return
    }

    if (file.isDirectory) {
        file.listFiles()
            ?.forEach {
                findSubProjects(it)
            }
    }
}

for (project in rootProject.children) {

    if (modules.containsKey(project.name)) {
        val directory = modules[project.name] ?: continue
        project.projectDir = File(directory)
    }
}
