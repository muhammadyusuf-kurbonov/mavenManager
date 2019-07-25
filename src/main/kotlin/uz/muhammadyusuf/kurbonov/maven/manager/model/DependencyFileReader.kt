package uz.muhammadyusuf.kurbonov.maven.manager.model

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import uz.muhammadyusuf.kurbonov.maven.manager.util.XMLParser
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class DependencyFileReader : FileReader(File("index.db")) {


    fun loadArtifactsAsync(): Observable<Dependency> {
        val create: Observable<Dependency> = Observable.create {
            forEachLine { line ->
                it.onNext(Dependency.fromID(line))
            }
            it.onComplete()
        }


        return create.subscribeOn(Schedulers.newThread())
    }

    private fun generateIndexFile(callBack: (Double) -> Unit = {}) {
        val indexes = File("index.db")
        callBack(0.03)
        indexes.writeText("")


        val walk = File("D:\\SDKs\\Maven Repository").walkBottomUp()
        walk.onEnter {
            it.isDirectory && it.listFiles()?.any { file ->
                file.isFile && file.name.matches(Regex("[\\s|\\S]+.pom"))
            } ?: false
        }
        callBack(0.05)

        val tempDependency = Dependency("", "", "")
        val writer = BufferedWriter(FileWriter(indexes))
        callBack(0.07)
        var value = 0.07
        for (file in walk) {
            if (!file.name.matches(Regex("[\\s|\\S]+.pom"))) continue

            try {
                val parser = XMLParser(file)
                tempDependency.groupName = parser.getValuesByTagName("groupId")[0]
                tempDependency.artifactID = parser.getValuesByTagName("artifactId")[0]
                tempDependency.version = parser.getValuesByTagName("version")[0]

                if (value < 0.4) {
                    callBack(value + 0.01); value += 0.01
                }

                indexes.appendText(tempDependency.toID() + "\n")
                println(tempDependency.toID())
            } catch (e: Exception) {
                continue
            }
        }
        writer.close()
        callBack(0.45)
    }

    fun optimizeIndexes(reload: Boolean = false, callBack: (Double) -> Unit = {}) {
        if (reload) generateIndexFile(callBack)
        callBack(0.50)
        val indexes = File("index.db")
        val newIndexes = File.createTempFile("index.db", ".tmp")
        callBack(0.53)
        var oldLine = ""
        indexes.forEachLine {
            if (oldLine == "") {
                oldLine = it
            } else if (!areSameArtifacts(Dependency.fromID(oldLine), Dependency.fromID(it))) {
                newIndexes.appendText(oldLine + "\n")
            }
            oldLine = it
        }
        callBack(0.73)
        indexes.writeText("")
        callBack(0.75)
        newIndexes.forEachLine {
            indexes.appendText(it + "\n")
        }
        callBack(0.90)
        newIndexes.delete()
        callBack(1.0)
    }

    private fun areSameArtifacts(dependency: Dependency, dependency2: Dependency): Boolean {
        return dependency.artifactID == dependency2.artifactID && dependency.groupName == dependency2.groupName
    }
}