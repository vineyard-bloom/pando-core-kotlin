package wallet_app

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.util.Duration
import jsoning.parseJsonFile
import jsoning.saveJson
import networking.primitiveBlockchain
import pando.*
import wallet_app.Client
import java.io.File

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
val keyDirectory = "addresses"

fun addressesScene(client: Client): Scene {
  val root = VBox()

  val addressScene = Scene(root, 800.0, 500.0)

  val addressCol = TableColumn<Address, String>("Address")
  val tableView = TableView<Address>()
  addressCol.setCellValueFactory(PropertyValueFactory<Address, String>("address"))


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


  File(keyDirectory).walk().forEach {
    if (it.extension == "json") {
      val address = File(it.toString()).nameWithoutExtension
      val keys = parseJsonFile<Keys>(it)
      val publicKey = stringToPublicKey(keys.publicKey)
      val privateKey = stringToPrivateKey(keys.privateKey)
      data.add(Address(address))
    }
  }

  tableView.getSelectionModel().selectedItemProperty().addListener({ row, oldSelection, newSelection ->
    val address = newSelection.getAddress()
    client.goToAddressScene(client, address)
  })

  root.children.add(tableView)
  root.children.add(newBlockchain)

  return addressScene
}
