package wallet_app
import javafx.stage.Stage
import persistence.PandoDatabase


class Client(private val stage: Stage) {

  fun sendTransaction(client: Client, address: String, db: PandoDatabase) {
    stage.setScene(newTransactionScene(client, address, db))
  }

  fun goToMainScene(client: Client, db: PandoDatabase) {
    stage.setScene(addressesScene(client, db))
  }

  fun goToAddressScene(client: Client, address: String, db: PandoDatabase) {
    stage.setScene(addressScene(client, address, db))
  }


}