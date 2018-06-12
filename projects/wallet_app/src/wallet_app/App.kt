package wallet_app

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Duration
import jsoning.parseJsonFile
import jsoning.saveJson
import networking.primitiveBlockchain
import pando.*
import java.io.File
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections




data class Keys(
    val publicKey: String,
    val privateKey: String
)


class Address constructor(address: String, publicKey: String, privateKey: String) {
  private val address: SimpleStringProperty
  private val publicKey: SimpleStringProperty
  private val privateKey: SimpleStringProperty

  init {
    this.address = SimpleStringProperty(address)
    this.publicKey = SimpleStringProperty(publicKey)
    this.privateKey = SimpleStringProperty(privateKey)
  }

  fun getAddress(): String {
    return address.get()
  }

  fun setAddress(fName: String) {
    address.set(fName)
  }

  fun getPublicKey(): String {
    return publicKey.get()
  }

  fun setPublicKey(fName: String) {
    publicKey.set(fName)
  }

  fun getPrivateKey(): String {
    return privateKey.get()
  }

  fun setPrivateKey(fName: String) {
    privateKey.set(fName)
  }

}
val keyDirectory = "out/bin/addresses"



class AppWindow : Application() {

  override fun start(primaryStage: Stage) {
    primaryStage.title = "Pando Wallet"
    val addressCol = TableColumn<Address, String>("Address")
    val publicCol = TableColumn<Address, String>("Public Key")
    val privateCol = TableColumn<Address, String>("Private Key")
    val tableView = TableView<Address>()
    addressCol.setCellValueFactory(
      PropertyValueFactory<Address, String>("address")
    )
    publicCol.setCellValueFactory(
      PropertyValueFactory<Address, String>("publicKey")
    )
    privateCol.setCellValueFactory(
      PropertyValueFactory<Address, String>("privateKey")
    )

    tableView.getSelectionModel().setCellSelectionEnabled(true)

    val data = FXCollections.observableArrayList<Address>()

    tableView.getColumns().addAll(addressCol, publicCol, privateCol)

    tableView.setItems(data);

    val newBlockchain = Button()
    newBlockchain.text = "Create Blockchain"
    newBlockchain.onAction = EventHandler {
      val pair = generateAddressPair()
      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
      val primitiveBlockchain = primitiveBlockchain(blockchain)

      val newKeys = Keys(
          primitiveBlockchain.publicKey,
          keyToString(pair.keyPair.private)
      )
      val directory = File(keyDirectory);
      if (!directory.exists())
        directory.mkdir()

      saveJson(newKeys, keyDirectory + "/" + blockchain.address)
    }

    File(keyDirectory).walk().forEach {
      if (it.extension == "json") {
        val address = File(it.toString()).nameWithoutExtension
        val keys = parseJsonFile<Keys>(it)
        val publicKey = stringToPublicKey(keys.publicKey)
        val privateKey = stringToPrivateKey(keys.privateKey)
        data.add(Address(address, keys.publicKey, keys.privateKey))
      }
    }

    val updater = Timeline(KeyFrame(Duration.seconds(1.0), EventHandler {
    }))
    updater.cycleCount = Timeline.INDEFINITE
    updater.play()

    val root = VBox()
    root.children.add(tableView)
    root.children.add(newBlockchain)

    primaryStage.scene = Scene(root, 1400.0, 500.0)

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