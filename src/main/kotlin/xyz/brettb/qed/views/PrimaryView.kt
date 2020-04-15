package xyz.brettb.qed.views

import com.squareup.okhttp.OkHttpClient
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import tornadofx.*
import xyz.brettb.qed.api.EddbApiClient
import xyz.brettb.qed.errors.*


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