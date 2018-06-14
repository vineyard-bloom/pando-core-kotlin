package wallet_app
import javafx.stage.Stage


class Client(private val stage: Stage) {

  fun sendTransaction(client: Client, address: String) {
    stage.setScene(newTransactionScene(client, address))
  }

  fun goToMainScene(client: Client) {
    stage.setScene(addressesScene(client))
  }

  fun goToAddressScene(client: Client, address: String) {
    stage.setScene(addressScene(client, address))
  }


}