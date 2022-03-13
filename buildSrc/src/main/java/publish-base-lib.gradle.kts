import groovy.util.Node
import groovy.util.NodeList

plugins {
    id("maven-publish")
}

afterEvaluate {

    val isAndroid = plugins.hasPlugin("com.android.library")

    val artifactName: String = name

    if (artifactName.contains("sample")) return@afterEvaluate

    publishing {
        publications {
            create<MavenPublication>("release") {
                if (isAndroid) {

                    pom.withXml {
                        val rootNode = asNode()
                        (rootNode.get("dependencies") as NodeList).forEach {
                            rootNode.remove(it as Node)
                        }
                        val dependenciesNode = rootNode.appendNode("dependencies")

                        configurations.maybeCreate("api").dependencies.forEach {
                            addDependency(dependenciesNode.appendNode("dependency"), it, "compile")
                        }
                        configurations.maybeCreate("compile").dependencies.forEach {
                            addDependency(dependenciesNode.appendNode("dependency"), it, "compile")
                        }
                        configurations.maybeCreate("implementation").dependencies.forEach {
                            addDependency(dependenciesNode.appendNode("dependency"), it, "runtime")
                        }
                        configurations.maybeCreate("compileOnly").dependencies.forEach {
                            addDependency(dependenciesNode.appendNode("dependency"), it, "provided")
                        }
                    }

                    from(components.getByName("release"))
                } else {
                    from(components.getByName("java"))
                }
                groupId = groupId()
                artifactId = artifactName

                artifact(tasks.getByName("sourceJar"))
            }

        }
    }
}

fun groupId(): String {
    return "com.github.android-alatan.lifecyclecomponents"
}

fun addDependency(dependencyNode: groovy.util.Node, dep: Dependency, scope: String) {
    dependencyNode.appendNode("groupId", dep.group)
    dependencyNode.appendNode("artifactId", dep.name)
    dependencyNode.appendNode("version", dep.version)
    dependencyNode.appendNode("scope", scope)
}
