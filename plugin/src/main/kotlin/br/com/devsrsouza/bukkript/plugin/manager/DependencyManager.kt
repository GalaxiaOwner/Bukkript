package br.com.devsrsouza.bukkript.plugin.manager

import br.com.devsrsouza.bukkript.plugin.BukkriptPlugin
import br.com.devsrsouza.bukkript.plugin.manager.host.HostClassLoader
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.LifecycleListener
import me.bristermitten.pdm.PDMBuilder
import me.bristermitten.pdmlibs.artifact.Artifact
import me.bristermitten.pdmlibs.artifact.SnapshotArtifact
import java.io.File

class DependencyManager(
    override val plugin: BukkriptPlugin
) : LifecycleListener<BukkriptPlugin> {

    private val isolatedDependency = "br.com.devsrsouza.bukkript:plugin-isolated:${plugin.description.version}"

    private val dependencies: List<File>
    private val hostClassLoader: HostClassLoader

    init {
        val pdm = PDMBuilder(plugin)
            .build()
        val dep = isolatedDependency.split(":")
        pdm.addRequiredDependency(SnapshotArtifact(dep[0], dep[1], dep[2]))

        dependencies = pdm.downloadAllDependencies().join()

        hostClassLoader = HostClassLoader(plugin, dependencies, plugin::class.java.classLoader)
    }
}
