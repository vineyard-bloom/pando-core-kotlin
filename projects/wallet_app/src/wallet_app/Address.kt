package wallet_app


import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.text.Text


fun addressScene(client: Client, address: String): Scene {
  val root = VBox()
  val addressScene = Scene(root, 800.0, 500.0)
  val addressText = Text("Address: $address")
  val send = Button()
  send.text = "New Transaction"
  send.onAction = EventHandler {
    client.sendTransaction(client, address)
  }
  val back = Button()
  back.text = "Back"
  back.onAction = EventHandler {
    client.goToMainScene(client)
  }
  root.children.add(addressText)
  root.children.add(send)
  root.children.add(back)
  return addressScene
}
