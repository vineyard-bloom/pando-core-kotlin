package wallet_app


import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.scene.text.Text


fun newTransactionScene(client: Client,  address: String): Scene {
  val root = VBox()
  val transactionScene = Scene(root, 800.0, 500.0)
  val sendQty = TextField()
  val fromAddress = Text("Address: $address")

  val send = Button()
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

  root.children.add(fromAddress)
  root.children.add(sendQty)
  root.children.add(send)
  root.children.add(cancel)
  return transactionScene
 }
