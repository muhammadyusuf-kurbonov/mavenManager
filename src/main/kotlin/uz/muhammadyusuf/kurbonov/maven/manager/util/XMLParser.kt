package uz.muhammadyusuf.kurbonov.maven.manager.util

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class XMLParser(file: File) {

    private val doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(file)

    fun getValuesByTagName(tagName: String): List<String> {
        val childNodes = doc.documentElement.childNodes
        val list = mutableListOf<String>()
        for (i in 0 until childNodes.length) {
            val node = childNodes.item(i)
            if (node.nodeName.startsWith("#")) continue
            if (node.nodeName == tagName) list += node.textContent
        }
        return list
    }
}