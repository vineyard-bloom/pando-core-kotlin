package wallet_app


import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import com.sun.tools.corba.se.idl.Util.getAbsolutePath
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import java.io.File




fun newTransactionScene(client: Client,  address: String): Scene {

  val root = getRoot()
  val transactionScene = Scene(root, 800.0, 500.0)

  val header = Label("New Transaction")
  header.setFont(Font.font("Arial", FontPosture.REGULAR, 24.0))
  GridPane.setHalignment(header, HPos.CENTER)
  GridPane.setMargin(header, Insets(20.0, 0.0,20.0,0.0))

  val fromLabel = Label("From Address: ")
  val fromAddress = Text(address)

  val sendLabel = Label("Send Qty: ")
  val sendQty = TextField()

  val data = FXCollections.observableArrayList<String>()
  data.add(address)

  val toLabel = Label("To Address: ")
  val toDropdown = ComboBox<String>(data)
  GridPane.setMargin(toDropdown, Insets(0.0, 0.0,20.0,0.0))
  GridPane.setMargin(toLabel, Insets(0.0, 0.0,20.0,0.0))

  val send = Button()
  GridPane.setHalignment(send, HPos.RIGHT)
  send.text = "Send"
  send.onAction = EventHandler {
//   client.goToMainScene(client)
  }
  val cancel = Button()
  cancel.text = "Cancel"
  cancel.onAction = EventHandler {
//   client.goToMainScene(client)
  }

  sendQty.textProperty().addListener({observable, oldValue, newValue ->
    println(newValue)
  })

  root.add(header, 0, 0, 4, 1)
  root.add(fromLabel, 0, 1, 2, 1)
  root.add(fromAddress, 2, 1)
  root.add(sendLabel, 0, 2, 2, 1)
  root.add(sendQty, 2,2)
  root.add(toLabel, 0, 3, 2, 1)
  root.add(toDropdown, 2, 3)
  root.add(send, 1, 4)
  root.add(cancel, 2, 4)
  return transactionScene
 }
