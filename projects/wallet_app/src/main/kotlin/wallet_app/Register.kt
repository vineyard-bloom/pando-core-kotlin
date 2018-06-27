package wallet_app

import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import persistence.PandoDatabase

fun registerScene(client: Client, address: String,  db: PandoDatabase):Scene {
  val root = getRoot()
  getMenu(root, client)

  val registerScene = Scene(root, 800.0, 500.0)

  val back = Button()
  GridPane.setHalignment(back, HPos.CENTER)
  back.text = "Back"
  back.onAction = EventHandler {
    client.goToMainScene(client, db)
  }

  root.add(back, 3, 1)
  return registerScene
}