package wallet_app


import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.Text


fun addressScene(client: Client, address: String): Scene {
  val root = getRoot()
  val addressScene = Scene(root, 800.0, 500.0)
  val addressText = Text("Address: $address")
  addressText.setFont(Font.font("Arial", FontPosture.REGULAR, 16.0))
  GridPane.setHalignment(addressText, HPos.CENTER)
  GridPane.setMargin(addressText, Insets(20.0, 0.0,20.0,0.0))
  val newTransaction = Button()
  GridPane.setHalignment(newTransaction, HPos.LEFT)
  newTransaction.text = "New Transaction"
  newTransaction.onAction = EventHandler {
    client.sendTransaction(client, address)
  }
  val back = Button()
  GridPane.setHalignment(back, HPos.RIGHT)
  back.text = "Back"
  back.onAction = EventHandler {
    client.goToMainScene(client)
  }
  root.add(addressText, 0, 0, 2, 1)
  root.add(newTransaction, 1, 2, 2, 1)
  root.add(back, 0,2)
  return addressScene
}
