package wallet_app


import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.VBox


fun newTransactionScene(client: Client): Scene {
   val root = VBox()
   val trasactionScene = Scene(root, 800.0, 500.0)

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
   root.children.add(send)
   root.children.add(cancel)
   return trasactionScene
 }
