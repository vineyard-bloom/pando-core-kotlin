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
  val root = GridPane()
  root.padding = Insets(0.0, 10.0, 10.0, 10.0)
  root.hgap = 10.0
  root.vgap = 10.0

  val columnOneConstraints = ColumnConstraints(100.0, 100.0, Double.MAX_VALUE)
  columnOneConstraints.halignment = HPos.RIGHT
  val columnTwoConstraints = ColumnConstraints(200.0, 200.0, Double.MAX_VALUE)
  columnTwoConstraints.hgrow = Priority.ALWAYS
  root.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstraints)

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

  val send = Button()
  GridPane.setHalignment(send, HPos.RIGHT)
  send.text = "Send"
  send.onAction = EventHandler {
   client.goToMainScene(client)
  }
  val cancel = Button()
  cancel.text = "Cancel"
  cancel.onAction = EventHandler {
   client.goToMainScene(client)
  }

  sendQty.textProperty().addListener({observable, oldValue, newValue ->
    println(newValue)
  })

  root.add(header, 0, 0, 2, 1)
  root.add(fromLabel, 0, 1)
  root.add(fromAddress, 1, 1)
  root.add(sendLabel, 0, 2)
  root.add(sendQty, 1,2)
  root.add(toLabel, 0, 3)
  root.add(toDropdown, 1, 3)
  root.add(send, 0, 4)
  root.add(cancel, 1, 4)
  return transactionScene
 }
