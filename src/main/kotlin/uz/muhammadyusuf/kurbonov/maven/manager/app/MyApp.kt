package uz.muhammadyusuf.kurbonov.maven.manager.app

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*
import uz.muhammadyusuf.kurbonov.maven.manager.view.MainView

class MyApp : App(MainView::class, Styles::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        print(resources)
        setStageIcon(Image(resources["/icon.bmp"]))
    }
}