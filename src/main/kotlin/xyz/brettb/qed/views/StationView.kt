package xyz.brettb.qed.views

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*
import xyz.brettb.qed.api.EddbApiClient
import xyz.brettb.qed.errors.SystemNonExistent
import xyz.brettb.qed.models.EdStation
import xyz.brettb.qed.models.EdSystem

class StationView: Fragment() {
    val station: EdStation by param()
    val api: EddbApiClient by param()
    val system: EdSystem = api.getSystem(station.systemID)
    override val root = borderpane {
        paddingAll = 8.0
        title = "Station: ${station.name}"
        center {
            vbox {
                vbox {
                    label("Station Name") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label(station.name)
                }

                vbox {
                    label("Station System") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label(system.name)
                }

                vbox {
                    label("Planetary Station") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label("${station.isPlanetary}")
                }
            }
        }
        bottom {
            hbox {
                paddingTop = 6.0
                alignment = Pos.CENTER
                button("Lookup System") {
                    hboxConstraints {
                        marginRight = 10.0
                    }
                    action {
                        runAsyncWithProgress {
                            find<SystemView>(mapOf(
                                SystemView::system to system,
                                SystemView::api to api))
                        } ui {
                            it.openWindow(resizable = false, escapeClosesWindow = false)
                        } fail {
                            if (it is SystemNonExistent) {
                                find<ErrorPopup>(mapOf(ErrorPopup::message to "That system doesn't exist!")).openModal()
                            } else {
                                find<ErrorPopup>(mapOf(ErrorPopup::message to it.localizedMessage)).openModal()
                            }
                        }
                    }
                }
                button("Close") {
                    action {
                        close()
                    }
                }
            }
        }
        autosize()
    }
}

