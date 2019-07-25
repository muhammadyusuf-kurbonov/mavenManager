package uz.muhammadyusuf.kurbonov.maven.manager.util

import java.io.File
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory

class SVGGenerator {
    fun generate() {
        val pathname = "src\\main\\kotlin\\" + "uz\\muhammadyusuf\\kurbonov\\maven\\manager\\icons"
        print(pathname)
        val file = File(pathname, "ICONS.kt")
        if (file.exists().not()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val icons_kt = FileWriter(file)
        icons_kt.write("package uz.muhammadyusuf.kurbonov.maven.manager.icons\n")
        val vectors = File("src\\main\\resources\\vector").listFiles { _, name ->
            name.endsWith(".svg")
        }
        icons_kt.use { iconsKt ->
            vectors?.forEach {

                val path = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse(it).documentElement.childNodes.item(0).attributes.item(0).textContent
                iconsKt.append("const val ${it.nameWithoutExtension.toUpperCase().replace("-", "_")} = \"$path\" \n")
            }
        }
    }
}

fun main() {
    SVGGenerator().generate()
}