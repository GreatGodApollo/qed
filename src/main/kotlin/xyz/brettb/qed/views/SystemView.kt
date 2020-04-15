package xyz.brettb.qed.views

import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.text.FontWeight
import tornadofx.*
import xyz.brettb.qed.api.EddbApiClient
import xyz.brettb.qed.errors.StationNonExistent
import xyz.brettb.qed.models.EdSystem
import java.text.DecimalFormat

class SystemView: Fragment() {
    val system: EdSystem by param()
    val api: EddbApiClient by param()
    var stations: ListView<String> = ListView()
    override val root = borderpane {
        paddingAll = 8.0
        title = "System: ${system.name}"
        center {
            vbox {
                vbox {
                    label("System Name") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label(system.name)
                }

                vbox {
                    label("System Location") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label("${system.x}, ${system.y}, ${system.z}")
                }

                vbox {
                    label("System Populated") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label("${system.isPopulated}")
                }

                if (system.isPopulated) {
                    vbox {
                        label("System Population") {
                            style {
                                fontWeight = FontWeight.BOLD
                            }
                        }
                        val format = DecimalFormat("#,###")
                        label(format.format(system.population))
                    }
                }

                vbox {
                    val stationLabel = label("System Stations") {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    try {
                        val names: MutableList<String> = mutableListOf()
                        for (station in api.getSystemStations(system)) {
                            names.add(station.name)
                        }
                        stations = listview(names.asObservable()) {
                            maxHeight = 80.0
                        }
                        stationLabel.text += " (${names.size})"
                    } catch (e: Throwable) {
                        if (e is StationNonExistent) {
                            label("This system has no stations")
                        } else {
                            find<ErrorPopup>(mapOf(ErrorPopup::message to e.localizedMessage)).openModal()
                        }
                    }
                }
            }
        }
        bottom {
            hbox {
                paddingTop = 6.0
                alignment = Pos.CENTER
                button("Lookup Station") {
                    hboxConstraints {
                        marginRight = 10.0
                    }
                    action {
                        if (stations.selectionModel.selectedItem != null) {
                            runAsyncWithProgress {
                                find<StationView>(mapOf(StationView::station to api.getStation(stations.selectionModel.selectedItem),
                                    StationView::api to api))
                            } ui {
                                it.openWindow(resizable = false, escapeClosesWindow = false)
                            } fail {
                                if (it is StationNonExistent) {
                                    find<ErrorPopup>(mapOf(ErrorPopup::message to "That station doesn't exist!")).openModal()
                                } else {
                                    find<ErrorPopup>(mapOf(ErrorPopup::message to it.localizedMessage)).openModal()
                                }
                            }
                        } else {
                            find<ErrorPopup>(mapOf(ErrorPopup::message to "You must select a station!")).openModal()
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