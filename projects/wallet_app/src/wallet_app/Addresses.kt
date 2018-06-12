package wallet_app

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.VBox
import javafx.util.Duration
import jsoning.parseJsonFile
import jsoning.saveJson
import networking.primitiveBlockchain
import pando.*
import wallet_app.Client
import java.io.File

fun addressesScene(client: Client): Scene {
  val root = VBox()

  val addressScene = Scene(root, 800.0, 500.0)

  val addressCol = TableColumn<Address, String>("Address")
  val tableView = TableView<Address>()
  addressCol.setCellValueFactory(PropertyValueFactory<Address, String>("address"))


  tableView.getSelectionModel().setCellSelectionEnabled(true)

  val data = FXCollections.observableArrayList<Address>()

  tableView.getColumns().addAll(addressCol)

  tableView.setItems(data);

  val newBlockchain = Button()
  newBlockchain.text = "Create Address"
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
    data.add(Address(blockchain.address))
    saveJson(newKeys, keyDirectory + "/" + blockchain.address)
  }

  val newTransaction = Button()
  newTransaction.text = "New Transaction"
  newTransaction.onAction = EventHandler {
    client.sendTransaction(client)
  }

  File(keyDirectory).walk().forEach {
    if (it.extension == "json") {
      val address = File(it.toString()).nameWithoutExtension
      val keys = parseJsonFile<Keys>(it)
      val publicKey = stringToPublicKey(keys.publicKey)
      val privateKey = stringToPrivateKey(keys.privateKey)
      data.add(Address(address))
    }
  }

  val updater = Timeline(KeyFrame(Duration.seconds(1.0), EventHandler {
  }))
  updater.cycleCount = Timeline.INDEFINITE
  updater.play()

  root.children.add(tableView)
  root.children.add(newBlockchain)
  root.children.add(newTransaction)

  return addressScene
}
