import javafx.application.Platform
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.layout.GridPane

fun getMenu(root: GridPane): GridPane {
  val menuBar = MenuBar()

  root.children.add(menuBar)
  menuBar.useSystemMenuBarProperty().set(true)

  val fileMenu = Menu("File")
  val newMenuItem = MenuItem("New")
  val saveMenuItem = MenuItem("Save")
  val exitMenuItem = MenuItem("Exit")

  exitMenuItem.setOnAction({ actionEvent -> Platform.exit() })

  fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
    SeparatorMenuItem(), exitMenuItem)

  menuBar.getMenus().addAll(fileMenu)

  return root
}