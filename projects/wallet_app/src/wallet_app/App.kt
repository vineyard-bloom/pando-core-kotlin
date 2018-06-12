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
import javafx.stage.Modality


data class Keys(
    val publicKey: String,
    val privateKey: String
)


class Address constructor(address: String) {
  private val address: SimpleStringProperty

  init {
    this.address = SimpleStringProperty(address)
  }

  fun getAddress(): String {
    return address.get()
  }

  fun setAddress(fName: String) {
    address.set(fName)
  }

}
val keyDirectory = "out/bin/addresses"



class AppWindow : Application() {

  override fun start(primaryStage: Stage) {
    primaryStage.title = "Pando Wallet"
//    val addressCol = TableColumn<Address, String>("Address")
//    val tableView = TableView<Address>()
//    addressCol.setCellValueFactory(PropertyValueFactory<Address, String>("address"))
//
//
//    tableView.getSelectionModel().setCellSelectionEnabled(true)
//
//    val data = FXCollections.observableArrayList<Address>()
//
//    tableView.getColumns().addAll(addressCol)
//
//    tableView.setItems(data);

    val root = VBox()
    primaryStage.scene = Scene(root, 800.0, 500.0)

    val client = Client(primaryStage)

//    val newBlockchain = Button()
//    newBlockchain.text = "Create Address"
//    newBlockchain.onAction = EventHandler {
//      val pair = generateAddressPair()
//      val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
//      val primitiveBlockchain = primitiveBlockchain(blockchain)
//
//      val newKeys = Keys(
//          primitiveBlockchain.publicKey,
//          keyToString(pair.keyPair.private)
//      )
//      val directory = File(keyDirectory);
//      if (!directory.exists())
//        directory.mkdir()
//      data.add(Address(blockchain.address))
//      saveJson(newKeys, keyDirectory + "/" + blockchain.address)
//    }
//
//    val newTransaction = Button()
//    newTransaction.text = "New Transaction"
//    newTransaction.onAction = EventHandler {
//      client.sendTransaction(client)
//    }
//
//    File(keyDirectory).walk().forEach {
//      if (it.extension == "json") {
//        val address = File(it.toString()).nameWithoutExtension
//        val keys = parseJsonFile<Keys>(it)
//        val publicKey = stringToPublicKey(keys.publicKey)
//        val privateKey = stringToPrivateKey(keys.privateKey)
//        data.add(Address(address))
//      }
//    }
//
//    val updater = Timeline(KeyFrame(Duration.seconds(1.0), EventHandler {
//    }))
//    updater.cycleCount = Timeline.INDEFINITE
//    updater.play()
//
//    root.children.add(tableView)
//    root.children.add(newBlockchain)
//    root.children.add(newTransaction)

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