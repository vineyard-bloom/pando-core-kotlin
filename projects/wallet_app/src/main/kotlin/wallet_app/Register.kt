package wallet_app

import javafx.scene.Scene
import persistence.PandoDatabase

fun registerScene(client: Client, address: String,  db: PandoDatabase):Scene {
  val root = getRoot()
  val registerScene = Scene(root, 800.0, 500.0)



  return registerScene
}