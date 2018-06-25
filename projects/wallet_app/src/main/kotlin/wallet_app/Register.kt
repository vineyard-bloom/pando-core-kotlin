package wallet_app

import getMenu
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import persistence.PandoDatabase

fun registerScene(client: Client, address: String,  db: PandoDatabase):Scene {
  val root = getRoot()
  getMenu(root)

  val registerScene = Scene(root, 800.0, 500.0)

  val back = Button()
  GridPane.setHalignment(back, HPos.CENTER)
  back.text = "Back"
  back.onAction = EventHandler {
    client.goToAddressScene(client, address, db)
  }

  root.add(back, 3, 1)
  return registerScene
}