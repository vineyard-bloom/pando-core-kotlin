package wallet_app


import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.Text
import persistence.PandoDatabase

class Transaction constructor(to: String, from: String, value: String, createdAt: String) {
  private val to: SimpleStringProperty
  private val from: SimpleStringProperty
  private val value: SimpleStringProperty
  private val createdAt: SimpleStringProperty

  init {
    this.to = SimpleStringProperty(to)
    this.from = SimpleStringProperty(from)
    this.value = SimpleStringProperty(value)
    this.createdAt = SimpleStringProperty(createdAt)
  }

  fun getTo(): String {
    return to.get()
  }
  fun getFrom(): String {
    return from.get()
  }
  fun getValue(): Any {
    return value.get()
  }
  fun getCreatedAt(): Any {
    return createdAt.get()
  }


}


fun addressScene(client: Client, address: String, db: PandoDatabase): Scene {
  val root = getRoot()
  val addressScene = Scene(root, 800.0, 500.0)

  val addressText = Text("Address: $address")
  addressText.setFont(Font.font("Arial", FontPosture.REGULAR, 16.0))
  GridPane.setHalignment(addressText, HPos.CENTER)

  val toCol = TableColumn<Transaction, String>("To")
  val fromCol = TableColumn<Transaction, String>("From")
  val valueCol = TableColumn<Transaction, String>("Value")
  val createdAtCol = TableColumn<Transaction, String>("Created At")
  val tableView = TableView<Transaction>()

  toCol.setCellValueFactory(PropertyValueFactory<Transaction, String>("to"))
  fromCol.setCellValueFactory(PropertyValueFactory<Transaction, String>("from"))
  valueCol.setCellValueFactory(PropertyValueFactory<Transaction, String>("value"))
  createdAtCol.setCellValueFactory(PropertyValueFactory<Transaction, String>("createdAt"))

  val data = FXCollections.observableArrayList<Transaction>()

  tableView.getColumns().addAll(toCol, fromCol, valueCol, createdAtCol)

  tableView.setItems(data)

  val blockchain = db.loadBlockchain(address)

  blockchain!!.blocks.forEach {
    data.add(Transaction(it!!.transaction.to, it.transaction.from.toString(), it.transaction.value.toString(), it.createdAt.toString()))
  }

  val newTransaction = Button()
  GridPane.setHalignment(newTransaction, HPos.CENTER)
  newTransaction.text = "New Transaction"
  newTransaction.onAction = EventHandler {
    client.sendTransaction(client, address, db)
  }
  val back = Button()
  GridPane.setHalignment(back, HPos.CENTER)
  back.text = "Back"
  back.onAction = EventHandler {
    client.goToMainScene(client, db)
  }

  root.add(addressText, 0, 0, 4, 1)
  root.add(tableView, 0, 1, 4, 1)
  root.add(newTransaction, 1, 2, 2, 1)
  root.add(back, 2,2)
  return addressScene
}
