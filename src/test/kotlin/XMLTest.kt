import uz.muhammadyusuf.kurbonov.maven.manager.util.XMLParser
import java.io.File

fun main() {
    XMLParser(File("src\\test\\res\\test2.pom")).getValuesByTagName("groupId").forEach {
        println(it)
    }
}