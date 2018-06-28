package wallet_app


import clienting.getBlockchain
import clienting.postBlockchain
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.text.Text
//import com.sun.tools.corba.se.idl.Util.getAbsolutePath
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import jsoning.parseJsonFile
import networking.primitiveToBlockchain
import pando.*
import persistence.PandoDatabase
import java.io.File
import java.security.PrivateKey



fun newTransactionScene(client: Client,  address: String, db: PandoDatabase): Scene {

  val root = getRoot()
  getMenu(root, client)

  val transactionScene = Scene(root, 800.0, 500.0)

  val header = Label("New Transaction")
  header.setFont(Font.font("Arial", FontPosture.REGULAR, 24.0))
  GridPane.setHalignment(header, HPos.CENTER)
  GridPane.setMargin(header, Insets(20.0, 0.0,20.0,0.0))

  val fromLabel = Label("From Address: ")
  val fromAddress = Text(address)

  val sendLabel = Label("Send Qty: ")
  val sendQty = TextField()

  val toLabel = Label("To Address: ")
  val toAddress = TextField()
  GridPane.setMargin(toAddress, Insets(0.0, 0.0,20.0,0.0))
  GridPane.setMargin(toLabel, Insets(0.0, 0.0,20.0,0.0))


  val send = Button()
  GridPane.setHalignment(send, HPos.RIGHT)
  send.text = "Send"

  send.onAction = EventHandler {

    File(configDirectory).walk().forEach {
      if (it.extension == "json") {
        val config = parseJsonFile<PublisherConfig>(it)
        val url = config.publisherUrl
        val toBlockchain = primitiveToBlockchain(getBlockchain(url, toAddress.text))
        val fromBlockchain = db.loadBlockchain(address)
        if (fromBlockchain is Blockchain) {
          File(keyDirectory).walk().forEach {
            if (it.extension == "json") {
              val fileAddress = File(it.toString()).nameWithoutExtension
              val keys = parseJsonFile<Keys>(it)
              if (fileAddress == address) {
                val privateKey = stringToPrivateKey(keys.privateKey)
                val send = sendTokens(toBlockchain, fromBlockchain, sendQty.text.toLong(), privateKey)
                val (fromBlock, _) = validateBlock(send.first(), fromBlockchain.publicKey, fromBlockchain)
                val (toBlock, _) = validateBlock(send.last(), fromBlockchain.publicKey, fromBlockchain)
                val newSend = addBlockWithoutValidation(toBlockchain, toBlock!!)
                val newFrom = addBlockWithoutValidation(fromBlockchain, fromBlock!!)
                postBlockchain(newSend)
                postBlockchain(newFrom)
//                db.saveBlockchain(newFrom)

              }
            }
          }
        }
      }
    }
   client.goToMainScene(client, db)
  }
  val cancel = Button()
  cancel.text = "Cancel"
  cancel.onAction = EventHandler {
   client.goToMainScene(client, db)
  }

  root.add(header, 0, 0, 4, 1)
  root.add(fromLabel, 0, 1, 2, 1)
  root.add(fromAddress, 2, 1)
  root.add(sendLabel, 0, 2, 2, 1)
  root.add(sendQty, 2,2)
  root.add(toLabel, 0, 3, 2, 1)
  root.add(toAddress, 2, 3)
  root.add(send, 1, 4)
  root.add(cancel, 2, 4)
  return transactionScene
 }
