package xyz.brettb.qed.views

import javafx.geometry.Pos
import tornadofx.*

class ErrorPopup: Fragment("Error!") {
    val message: String by param()
    override val root = borderpane {
        paddingAll = 8.0
        center {
            label(message) {
                textFill = c("red")
                paddingBottom = 6.0
            }
        }

        bottom {
            hbox {
                alignment = Pos.CENTER
                button("Ok") {
                    action {
                        close()
                    }
                }
            }
        }
        autosize()
    }
}