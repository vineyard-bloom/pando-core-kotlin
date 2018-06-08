package wallet_app

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import pando.*
import jsoning.*
import networking.*

class Keys(
  val publicKey: String,
  val privateKey: String
)

class AppWindow : Application() {

  override fun start(primaryStage: Stage) {
    primaryStage.title = "Pando Wallet"
    val textArea = TextArea()
    textArea.setPrefRowCount(10)
    textArea.setFont(Font.font("Courier", 14.0))

    val newBlockchain = Button()
    newBlockchain.text = "Create Blockchain"
    newBlockchain.onAction = EventHandler {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      textArea.text = blockchain.toString()
      val primitiveBlockchain = primitiveBlockchain(blockchain)

      val newKeys = Keys(
        primitiveBlockchain.publicKey,
        privateKeyToString(pair.keyPair.private)
      )

      saveJson(newKeys, "./addresses/${blockchain.address}")
    }

    val getAddresses = Button()
    getAddresses.text = "Get Addresses"
    getAddresses.onAction = EventHandler {

    }

    val updater = Timeline(KeyFrame(Duration.seconds(1.0), EventHandler {
    }))
    updater.cycleCount = Timeline.INDEFINITE
    updater.play()

    val root = VBox()
    root.children.add(textArea)
    root.children.add(newBlockchain)
    root.children.add(getAddresses)
    primaryStage.scene = Scene(root, 300.0, 250.0)

    primaryStage.scene.addEventFilter(KeyEvent.KEY_PRESSED, object : EventHandler<KeyEvent> {
      internal val keyComb: KeyCombination = KeyCodeCombination(KeyCode.S,
          KeyCombination.CONTROL_DOWN)

      override fun handle(event: KeyEvent) {
        if (keyComb.match(event)) {

          event.consume() // <-- stops passing the event to next node
        }
      }
    })

    primaryStage.show()
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