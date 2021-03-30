import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import java.io.File

project.tasks.create("projectDependencyGraph") {
    doLast {
        val dot = File(rootProject.buildDir, "reports/dependency-graph/project.dot")
        dot.parentFile.mkdirs()
        dot.delete()

        dot.appendText(
            """
            digraph {
                graph [label="${rootProject.name}",labelloc=t,fontsize=30,ranksep=1.4];
                node [style=filled, fillcolor="#bbbbbb"];
                rankdir=TB;
            """.trimIndent()
        )

        val rootProjects = mutableListOf<Project>()
        var queue = mutableListOf(rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            rootProjects.add(project)
            queue.addAll(project.childProjects.values)
        }

        val projects = linkedSetOf<Project>()
        val dependencies = mutableMapOf<Pair<Project, Project>, MutableList<String>>()
        val multiplatformProjects = mutableListOf<Project>()
        val jsProjects = mutableListOf<Project>()
        val androidProjects = mutableListOf<Project>()
        val javaProjects = mutableListOf<Project>()
        val iosProjects = mutableListOf<Project>()

        queue = mutableListOf(rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            queue.addAll(project.childProjects.values)

            if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                multiplatformProjects.add(project)
            }
            if (project.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
                jsProjects.add(project)
            }
            if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("com.android.application")) {
                androidProjects.add(project)
            }
            if (project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                javaProjects.add(project)
            }
            if (project.plugins.hasPlugin("org.openbakery.xcode-plugin")) {
                iosProjects.add(project)
            }

            project.configurations.all {
                this.dependencies
                    .withType(ProjectDependency::class.java)
                    .map { it.dependencyProject }
                    .filterNot { it == project }
                    .forEach { dependency ->
                        projects.add(project)
                        projects.add(dependency)
                        rootProjects.remove(dependency)

                        val graphKey = project to dependency
                        val traits = dependencies.computeIfAbsent(graphKey) { mutableListOf() }

                        when {
                            name.toLowerCase().endsWith("api") -> traits.add("style=solid")
                            project.name.toLowerCase() == "app" -> traits.add("style=dotted")
                            name.toLowerCase().endsWith("implementation") -> traits.add("style=dashed")
                            name.toLowerCase().endsWith("default") -> traits.add("style=dashed")
                        }
                    }
            }
        }

        val sortedProjects = projects.sortedBy { it.path }

        dot.appendText("\n  # Projects\n\n")
        for (project in sortedProjects) {
            val traits = mutableListOf<String>()

            if (rootProjects.contains(project)) {
                traits.add("shape=box")
            }

            val color = when {
                multiplatformProjects.contains(project) -> "#ffd2b3"
                jsProjects.contains(project) -> "#ffffba"
                androidProjects.contains(project) -> "#baffc9"
                javaProjects.contains(project) -> "#ffb3ba"
                iosProjects.contains(project) -> "#b9b5ff"
                else -> "#eeeeee"
            }
            traits.add("fillcolor=\"$color\"")

            dot.appendText("  \"${project.path}\" [${traits.joinToString(", ")}];\n")
        }

        dot.appendText("\n  {rank = same;")
        for (project in sortedProjects) {
            if (rootProjects.contains(project)) {
                dot.appendText(" \"${project.path}\";")
            }
        }
        dot.appendText("}\n")

        dot.appendText("\n  # Dependencies\n\n")
        dependencies.forEach { key, traits ->
            dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
            if (traits.isNotEmpty()) {
                dot.appendText(" [${traits.joinToString(", ")}]")
            }
            dot.appendText("\n")
        }

        dot.appendText("}\n")

        val p = Runtime.getRuntime().exec("dot -Tpng -O project.dot", emptyArray(), dot.parentFile)
        p.waitFor()
        if (p.exitValue() != 0) {
            println(p.errorStream.bufferedReader().use(java.io.BufferedReader::readText))
        }

        println("Project module dependency graph created at ${dot.absolutePath}.png")
    }
}
