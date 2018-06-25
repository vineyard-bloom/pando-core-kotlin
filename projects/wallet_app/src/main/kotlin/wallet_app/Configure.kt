import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import wallet_app.Client
import wallet_app.getRoot

fun configureScene(client: Client){
  val configStage = Stage()
  configStage.title = "Configure"
  val root = getRoot()
  val configScene = Scene(root, 800.0, 500.0)

  val newConfig = javafx.scene.control.Button()
  GridPane.setHalignment(newConfig, HPos.RIGHT)
  newConfig.text = "New Config"
  newConfig.onAction = EventHandler {

  }

  val loadConfig = javafx.scene.control.Button()
  GridPane.setHalignment(newConfig, HPos.RIGHT)
  loadConfig.text = "Load Config"
  loadConfig.onAction = EventHandler {

  }


  configStage.show()
  configStage.centerOnScreen()

  configStage.scene = configScene

  root.add(newConfig, 1, 1)
  root.add(loadConfig, 2, 1)

}