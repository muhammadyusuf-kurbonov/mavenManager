package uz.muhammadyusuf.kurbonov.maven.manager.model

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import uz.muhammadyusuf.kurbonov.maven.manager.util.XMLParser
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

val file1 = File("index.db")

class DependencyProvider{

    private val reader: FileReader

    init {
        if (file1.exists().not()){
            file1.createNewFile()
        }
        reader = FileReader(file1)
    }

    fun loadArtifactsAsync(): Observable<Dependency> {
        val create: Observable<Dependency> = Observable.create {
            reader.forEachLine { line ->
                it.onNext(Dependency.fromID(line))
            }
            it.onComplete()
        }


        return create.subscribeOn(Schedulers.newThread())
    }

    fun generateIndexFile(onMessageChange: (String) -> Unit = {}
                          , onProgressChange: (Double) -> Unit = {}) {

            val indexes = file1
            onMessageChange("Indexing started")
            onProgressChange(0.03)
            indexes.writeText("")
            onMessageChange("Indexes cleared")

            val walk = File("D:\\SDKs\\Maven Repository").walkBottomUp()
            walk.onEnter {
                it.isDirectory && it.listFiles()?.any { file ->
                    file.isFile && file.name.matches(Regex("[\\s|\\S]+.pom"))
                } ?: false
            }
            onProgressChange(0.05)
            onMessageChange("Indexing ...")
            val tempDependency = Dependency("", "", "")
            val writer = BufferedWriter(FileWriter(indexes))
            onProgressChange(0.07)
            var currentProgress = 0.07
            val d = 0.9 / walk.count()
            for (file in walk) {
                onProgressChange(currentProgress)
                currentProgress += d
                if (!file.name.matches(Regex("[\\s|\\S]+.pom"))) continue

                try {
                    val parser = XMLParser(file)
                    tempDependency.groupName = parser.getValuesByTagName("groupId")[0]
                    tempDependency.artifactID = parser.getValuesByTagName("artifactId")[0]
                    tempDependency.version = parser.getValuesByTagName("version")[0]


                    indexes.appendText(tempDependency.toID() + "\n")
                    onMessageChange("Indexing ${tempDependency.toID()}")
                } catch (e: Exception) {
                    continue
                }
            }
            writer.close()
            onProgressChange(1.0)
            onMessageChange("Indexing done!")

    }

    fun optimizeIndexes(reload: Boolean = false
                        ,onMessageChange: (String) -> Unit = {}
                        , onProgressChange: (Double) -> Unit = {}) {

        if (reload) {
            generateIndexFile(onMessageChange, onProgressChange)
        }
        val indexes = file1
        val newIndexes = File.createTempFile("index.db", ".tmp")
        onProgressChange(0.1)
        onMessageChange("Analyze started")
        var oldLine = ""
        onProgressChange(0.3)
        onMessageChange("Analyzing ...")
        indexes.forEachLine {
            if (oldLine == "") {
                oldLine = it
            } else if (!areSameArtifacts(Dependency.fromID(oldLine), Dependency.fromID(it))) {
                newIndexes.appendText(oldLine + "\n")
            }
            oldLine = it
            onMessageChange("Analyzing $it")
        }
        onProgressChange(0.73)
        onMessageChange("Writing new index.db")
        indexes.writeText("")
        onProgressChange(0.75)
        onMessageChange("Coping Indexes ...")
        newIndexes.forEachLine {
            indexes.appendText(it + "\n")
            onMessageChange("Coping $it")
        }
        onProgressChange(0.9)
        onMessageChange("Deleting temp files")
        newIndexes.delete()
        onProgressChange(1.0)
        onMessageChange("Analyze finished")

    }

    private fun areSameArtifacts(dependency: Dependency, dependency2: Dependency): Boolean {
        return dependency.artifactID == dependency2.artifactID && dependency.groupName == dependency2.groupName
    }
}