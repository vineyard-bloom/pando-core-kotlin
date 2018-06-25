import javafx.application.Platform
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.layout.GridPane
import wallet_app.Client

fun getMenu(root: GridPane, client: Client): GridPane {
  val menuBar = MenuBar()

  root.children.add(menuBar)
  menuBar.useSystemMenuBarProperty().set(true)

  val fileMenu = Menu("File")
  val configureMenuItem = MenuItem("Configure")
  val exitMenuItem = MenuItem("Exit")

  exitMenuItem.setOnAction({ actionEvent -> Platform.exit() })
  configureMenuItem.setOnAction({ actionEvent -> configureScene(client) })

  fileMenu.getItems().addAll(configureMenuItem, SeparatorMenuItem(), exitMenuItem)

  menuBar.getMenus().addAll(fileMenu)

  return root
}