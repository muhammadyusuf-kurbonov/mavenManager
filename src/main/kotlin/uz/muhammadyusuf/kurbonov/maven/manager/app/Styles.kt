package uz.muhammadyusuf.kurbonov.maven.manager.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val toolbar_button by cssclass()


        val groupId by cssclass()
        val artifactId by cssclass()
        val version by cssclass()

        val dep_button by cssclass()
        val dep_button_selected by cssclass()

        val mouse_in by csspseudoclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        root {
            backgroundColor += c("#919191")
        }

        toolBar {
            backgroundColor += c("#4E4E4E")
        }

        toolbar_button and mouse_in {
            backgroundColor += c("#7a7a7a")
            textFill = c("#FFFFFFFF")
        }
        toolbar_button {
            backgroundColor += c("#4d4d4d")
            textFill = c("#FFFFFFFF")
        }


        dep_button {
            backgroundColor += Color.BEIGE
            textFill = Color.DIMGREY

            startMargin = Dimension(2.0, Dimension.LinearUnits.pt)
            endMargin = Dimension(2.0, Dimension.LinearUnits.pt)
        }

        dep_button_selected {
            backgroundColor += Color.DODGERBLUE
            textFill = Color.WHITE
        }

        groupId {
            fontSize = Dimension(12.0, Dimension.LinearUnits.px)
        }
        artifactId {
            fontSize = Dimension(14.0, Dimension.LinearUnits.px)
        }
        version {
            fontSize = Dimension(10.0, Dimension.LinearUnits.px)
        }


    }
}