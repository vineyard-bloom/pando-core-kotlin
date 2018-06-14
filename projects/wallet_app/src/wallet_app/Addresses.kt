package wallet_app

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
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.Text
import jsoning.parseJsonFile
import jsoning.saveJson
import networking.primitiveBlockchain
import pando.*
import persistence.PandoDatabase
import java.io.File
import persistence.PandoDatabase.*

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

fun addressesScene(client: Client, db: PandoDatabase): Scene {
  val root = getRoot()

  val addressScene = Scene(root, 800.0, 500.0)

  val addressCol = TableColumn<Address, String>("Address")

  val addresses = Text("My Addresses")
  addresses.setFont(Font.font("Arial", FontPosture.REGULAR, 16.0))
  GridPane.setHalignment(addresses, HPos.CENTER)

  val tableView = TableView<Address>()
  addressCol.setCellValueFactory(PropertyValueFactory<Address, String>("address"))


  val data = FXCollections.observableArrayList<Address>()

  tableView.getColumns().addAll(addressCol)

  tableView.setItems(data);

  val newBlockchain = Button()
  GridPane.setHalignment(newBlockchain, HPos.CENTER)
  newBlockchain.text = "Create Address"
  newBlockchain.onAction = EventHandler {
    val pair = generateAddressPair()
    val blockchain = createNewBlockchain(pair.address, pair.keyPair.public)
    db.saveBlockchain(blockchain)
    val primitiveBlockchain = primitiveBlockchain(blockchain)
    val newKeys = Keys(
      primitiveBlockchain.publicKey,
      privateKeyToString(pair.keyPair.private)
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

  root.add(addresses, 0, 0, 4, 1)
  root.add(tableView, 0, 1, 4, 1)
  root.add(newBlockchain, 0, 2, 4, 1)

  return addressScene
}
