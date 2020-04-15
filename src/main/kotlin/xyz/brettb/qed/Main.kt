package xyz.brettb.qed

import com.squareup.okhttp.OkHttpClient
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.ToggleGroup
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import tornadofx.*
import xyz.brettb.qed.api.EddbApiClient
import xyz.brettb.qed.errors.*
import xyz.brettb.qed.models.EdStation
import xyz.brettb.qed.models.EdSystem
import java.text.DecimalFormat

fun main() {
    launch<QED>()
}

class QED: App(PrimaryView::class) {

    override fun start(stage: Stage) {
        stage.minHeight = 150.0
        stage.minWidth = 300.0

        super.start(stage)
    }
}


class PrimaryView : View() {
    var cli = OkHttpClient()
    var api = EddbApiClient(cli)

    val searchname = SimpleStringProperty()

    private val typeToggleGroup = ToggleGroup()

    override val root = borderpane {


        title = "Quick Elite: Dangerous"

        bottom {
            paddingAll = 10.0
            hbox {
                alignment = Pos.CENTER
                button("Search") {
                    hboxConstraints {
                        marginRight = 10.0
                    }
                    action {
                        if (!searchname.value.isNullOrBlank()) {
                            if (typeToggleGroup.selectedToggle.toString().contains("System")) {
                                runAsyncWithProgress {
                                    find<SystemView>(mapOf(SystemView::system to api.getSystem(searchname.value),
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
                            } else {
                                runAsyncWithProgress {
                                    find<StationView>(mapOf(StationView::station to api.getStation(searchname.value),
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
                            }
                        } else {
                            find<ErrorPopup>(mapOf(ErrorPopup::message to "You must supply a system!")).openModal()
                        }
                    }
                }
                button("Quit") {
                    action {
                        close()
                    }
                }
            }
        }

        center {
            form {

                fieldset {

                    radiobutton("System", typeToggleGroup) {
                        isSelected = true
                    }
                    radiobutton("Station", typeToggleGroup)

                    field("Name") {
                        textfield(searchname)
                    }
                }
            }
        }
    }
}

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
                            find<SystemView>(mapOf(SystemView::system to system,
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

