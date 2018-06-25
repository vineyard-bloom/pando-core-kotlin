package wallet_app

import grounded.DatabaseConfig
import grounded.Dialect
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import jsoning.loadJsonFile
import org.jetbrains.exposed.sql.Database
import persistence.AppConfig
import persistence.PandoDatabase
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.layout.*


class AppWindow : Application() {

  override fun start(primaryStage: Stage) {
    primaryStage.title = "Pando Wallet"

    val root = BorderPane()

    primaryStage.scene = Scene(root, 800.0, 500.0)

    val client = Client(primaryStage)

    val db = initDatabase()

    primaryStage.show()

    client.goToMainScene(client, db)

  }

  companion object {
    @JvmStatic
    fun main() {
      Application.launch(AppWindow::class.java)
    }
  }
}



object App {
  @JvmStatic
  fun main(args: Array<String>) {
    AppWindow.main()
  }
}

fun getRoot():GridPane {
  val root = GridPane()
  root.padding = Insets(10.0, 10.0, 10.0, 10.0)
  root.hgap = 10.0
  root.vgap = 10.0

  val columnOneConstraints = ColumnConstraints(100.0, 100.0, Double.MAX_VALUE)
  columnOneConstraints.halignment = HPos.RIGHT
  val columnTwoConstraints = ColumnConstraints(200.0, 200.0, Double.MAX_VALUE)
  columnTwoConstraints.hgrow = Priority.ALWAYS
  val columnThreeConstraints = ColumnConstraints(100.0, 100.0, Double.MAX_VALUE)
  columnThreeConstraints.hgrow = Priority.ALWAYS
  val columnFourConstraints = ColumnConstraints(200.0, 200.0, Double.MAX_VALUE)
  columnFourConstraints.hgrow = Priority.ALWAYS
  root.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstraints, columnThreeConstraints, columnFourConstraints)
  return root
}


fun initDatabase(): PandoDatabase {
  val appConfig = DatabaseConfig(
    "",
    "out/bin/pando",
    "",
    "",
    Dialect.sqlite,
    null
  )
  val db = PandoDatabase(appConfig)
//  if ()
  db.fixtureInit()
  return db
}
