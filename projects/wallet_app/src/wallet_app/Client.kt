package wallet_app
import javafx.stage.Stage
import persistence.PandoDatabase


class Client(private val stage: Stage) {

  fun sendTransaction(client: Client, address: String) {
    stage.setScene(newTransactionScene(client, address))
  }

  fun goToMainScene(client: Client, db: PandoDatabase) {
    stage.setScene(addressesScene(client, db))
  }

  fun goToAddressScene(client: Client, address: String) {
    stage.setScene(addressScene(client, address))
  }


}