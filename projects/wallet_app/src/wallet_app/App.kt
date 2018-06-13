package wallet_app

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

class AppWindow : Application() {

  override fun start(primaryStage: Stage) {
    primaryStage.title = "Pando Wallet"

    val root = VBox()
    primaryStage.scene = Scene(root, 800.0, 500.0)

    val client = Client(primaryStage)

    primaryStage.show()

    client.goToMainScene(client)

  }

  companion object {
    @JvmStatic
    fun main() {
      Application.launch(AppWindow::class.java)
    }
  }
}



object App {
  @JvmStatic
  fun main(args: Array<String>) {
    AppWindow.main()
  }
}