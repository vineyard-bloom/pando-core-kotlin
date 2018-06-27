package wallet_app

import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import jsoning.saveJson
import pando.getBalance
import wallet_app.Address
import wallet_app.Client
import wallet_app.getRoot
import wallet_app.keyDirectory
import java.io.File

val configDirectory = "out/bin/publishers"

data class PublisherConfig(
  val alias: String,
  val publisherUrl: String
)


fun configureScene(client: Client){
  val configStage = Stage()
  configStage.title = "Configure"
  val root = getRoot()
  val configScene = Scene(root, 800.0, 500.0)

  val configLabel = Label("Publisher URL: ")
  GridPane.setHalignment(configLabel, HPos.RIGHT)
  val configAddress = TextField()
  val aliasLabel = Label("Publisher Alias: ")
  GridPane.setHalignment(aliasLabel, HPos.RIGHT)
  val aliasConfig = TextField()

  val newConfig = javafx.scene.control.Button()
  GridPane.setHalignment(newConfig, HPos.RIGHT)
  newConfig.text = "Save"
  newConfig.onAction = EventHandler {
    val address = configAddress.text
    val alias = aliasConfig.text
    val config = PublisherConfig(alias, address)
    saveJson(config, configDirectory + "/" + alias)
  }

  val loadConfig = javafx.scene.control.Button()
  GridPane.setHalignment(newConfig, HPos.RIGHT)
  loadConfig.text = "Load"
  loadConfig.onAction = EventHandler {

  }


  configStage.show()
  configStage.centerOnScreen()

  configStage.scene = configScene

  root.add(configLabel, 1, 1)
  root.add(configAddress, 2, 1)
  root.add(aliasLabel, 1, 2)
  root.add(aliasConfig, 2, 2)
  root.add(newConfig, 1, 3)
  root.add(loadConfig, 2, 3)

}