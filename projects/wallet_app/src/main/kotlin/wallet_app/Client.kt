package wallet_app
import javafx.stage.Stage
import persistence.PandoDatabase


class Client(private val stage: Stage) {

  fun sendTransaction(client: Client, address: String, db: PandoDatabase) {
    stage.scene = newTransactionScene(client, address, db)
  }

  fun goToMainScene(client: Client, db: PandoDatabase) {
    stage.scene = addressesScene(client, db)
  }

  fun goToAddressScene(client: Client, address: String, db: PandoDatabase) {
    stage.scene = addressScene(client, address, db)
  }

  fun goToRegisterScene(client: Client, address: String, db: PandoDatabase) {
    stage.scene = registerScene(client, address, db)
  }


}