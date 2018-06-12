package wallet_app
import javafx.stage.Stage


class Client(private val stage: Stage) {

  fun sendTransaction(client: Client) {
    stage.setScene(newTransactionScene(client))
  }

  fun goToMainScene(client: Client) {
    stage.setScene(addressesScene(client))
  }


}