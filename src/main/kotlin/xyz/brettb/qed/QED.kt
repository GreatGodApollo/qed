package xyz.brettb.qed

import javafx.stage.Stage
import tornadofx.App
import xyz.brettb.qed.views.PrimaryView

class QED: App(PrimaryView::class) {

    override fun start(stage: Stage) {
        stage.minHeight = 180.0
        stage.minWidth = 300.0
        stage.maxHeight = 180.0
        stage.maxWidth = 300.0

        super.start(stage)
    }
}