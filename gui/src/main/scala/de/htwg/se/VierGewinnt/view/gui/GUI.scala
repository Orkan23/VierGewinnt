package de.htwg.se.VierGewinnt.view.gui

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.google.inject.Inject
import scala.concurrent.Await
import scala.concurrent.Future
import scala.io.AnsiColor.BLUE_B
import scala.io.AnsiColor.RED_B
import scala.io.AnsiColor.YELLOW_B
import scala.language.postfixOps
import scala.util.Failure
import scala.util.Success
import scalafx.animation.*
import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.application.Platform.*
import scalafx.event.ActionEvent
import scalafx.scene.control.Menu
import scalafx.scene.control.MenuBar
import scalafx.scene.control.MenuItem
import scalafx.scene.control.TextInputDialog
import scalafx.scene.effect.BlendMode.Blue
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.*
import scalafx.scene.layout.GridPane.getRowIndex
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.shape.Circle.sfxCircle2jfx
import scalafx.scene.shape.Rectangle
import scalafx.scene.Scene
import scalafx.Includes.*
import scalafx.Includes.at

/** GUI class, the graphical user interface. Extends the Observer class to be compatible with the model-view-controller architecture.
  *
  * @param controller
  *   Controller as parameter, which controls this GUI.
  */
class GUI @Inject() (restController: GuiRestController) extends JFXApp3:

  var chips: Vector[Vector[Circle]] = emptyChips()
  var chipGrid: GridPane = emptyGrid()
  var playgroundstatus = new Menu(restController.getPlaygroundState)
  var statestatus = new Menu(restController.getGameState)

  restController.addToObserver

  /** Updates the GUI with chips and grid from the controller. */
  def update: Unit =
    checkChipSize()
    restController.getWinnerChips match { // restController.getWinnerChips
      case Some(v) => win(v._2, v._3, v._4, v._5)
      case None =>
        chips.zipWithIndex.map { case (subList, i) =>
          subList.zipWithIndex.map { case (element, j) =>
            restController.getChipColor(j, i) match { // restController.getChipColor(i, j)
              case BLUE_B => // Empty
                element.fill = Color.Gray
              case RED_B => // Red
                if element.getFill != Color.sfxColor2jfx(Color.Red) then animateDrop(element, Color.Red)
              case YELLOW_B => // Yellow
                if element.getFill != Color.sfxColor2jfx(Color.Yellow) then animateDrop(element, Color.Yellow)
            }
          }
        }
    }

  def win(a: (Int, Int), b: (Int, Int), c: (Int, Int), d: (Int, Int)): Unit =
    val tmpCol = if restController.getChipColor(a._1, a._2) == YELLOW_B then Color.Yellow else Color.Red
    val t = Timeline(
      Seq(
        at(0.0 s) {
          chips(a._2)(a._1).fill -> Color.Green
        },
        at(0.0 s) {
          chips(b._2)(b._1).fill -> Color.Green
        },
        at(0.0 s) {
          chips(c._2)(c._1).fill -> Color.Green
        },
        at(0.0 s) {
          chips(d._2)(d._1).fill -> Color.Green
        },
        at(1 s) {
          chips(a._2)(a._1).fill -> Color.sfxColor2jfx(tmpCol)
        },
        at(1 s) {
          chips(b._2)(b._1).fill -> Color.sfxColor2jfx(tmpCol)
        },
        at(1 s) {
          chips(c._2)(c._1).fill -> Color.sfxColor2jfx(tmpCol)
        },
        at(1 s) {
          chips(d._2)(d._1).fill -> Color.sfxColor2jfx(tmpCol)
        },
        at(1.5 s) {
          chips(a._2)(a._1).fill -> Color.Green
        },
        at(1.5 s) {
          chips(b._2)(b._1).fill -> Color.Green
        },
        at(1.5 s) {
          chips(c._2)(c._1).fill -> Color.Green
        },
        at(1.5 s) {
          chips(d._2)(d._1).fill -> Color.Green
        }
      )
    )
    t.setCycleCount(5)
    t.play()

  /** Animates the dropping of a chip into his position.
    *
    * @param element
    *   Choose the element type to be animated.
    * @param color
    *   Choose the element color to be animated.
    */
  def animateDrop(element: Circle, color: Color): Unit =
    Timeline(
      Seq(
        at(0.0 s) {
          element.translateY -> -500.0
        },
        at(0.0 s) {
          element.fill -> Color.Gray
        },
        at(0.3 s) {
          element.translateY -> 0.0
        },
        at(0.3 s) {
          element.fill -> color
        }
      )
    ).play()

  /** Builds the GUI application. */
  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title.value = "VierGewinnt"
      scene = new Scene:
        fill = Color.DarkBlue
        val menu = new MenuBar {
          menus = List(
            new Menu("File") {
              items = List(
                new MenuItem("New PvP") {
                  onAction = (event: ActionEvent) => {
                    val dialog = new TextInputDialog("7") {
                      initOwner(stage)
                      title = "Set a size"
                      this.setContentText("Enter a number min. 4:")
                    }
                    var result = dialog.showAndWait()
                    while (result.get.toInt < 4)
                      result = dialog.showAndWait()

                    restController.setupGame(0, result.get.toInt)
                    chipGrid = emptyGrid() // Update Grid to new Size
                    start()
                  }
                },
                new MenuItem("New PvE") {
                  onAction = (event: ActionEvent) => {
                    val dialog = new TextInputDialog("7") {
                      initOwner(stage)
                      title = "Set a size"
                      this.setContentText("Enter a number min. 4:")
                    }
                    var result = dialog.showAndWait()
                    while (result.get.toInt < 4)
                      result = dialog.showAndWait()

                    restController.setupGame(1, result.get.toInt)
                    chipGrid = emptyGrid() // Update Grid to new Size
                    start()
                  }
                },
                new MenuItem("Save") {
                  onAction = (event: ActionEvent) => restController.save
                },
                new MenuItem("Load") {
                  onAction = (event: ActionEvent) =>
                    restController.load
                    start()
                }
              )
            },
            new Menu("Control") {
              items = List(
                new MenuItem("Undo") {
                  onAction = (event: ActionEvent) => restController.doAndPublishUndo
                },
                new MenuItem("Redo") {
                  onAction = (event: ActionEvent) => restController.doAndPublishRedo
                }
              )
            },
            new Menu("Help") {
              items = List(
                new MenuItem("About")
              )
            },
            playgroundstatus,
            statestatus
          )
        }

        var vBox = new VBox():
          children = List(menu, chipGrid)

        content = new VBox() {
          children = List(menu, chipGrid)
        }
    playgroundstatus.setText(restController.getPlaygroundState)
    statestatus.setText(restController.getGameState)

  /** Places empty gray circles with the size 50 into a vector.
    *
    * @return
    *   Returns a new Matrix with circles.
    */
  def emptyChips(): Vector[Vector[Circle]] = Vector.fill(restController.getGridSize, restController.getGridSize)(Circle(50, fill = Color.Gray))

  /** Checks if the chips size equals the controller gridsize to prevent having a ScalaFX Threading error. If the chips size does not equal the
    * controller size, update the chips and the grid with an empty one.
    */
  def checkChipSize(): Unit =
    if (!chips.length.equals(restController.getGridSize)) {
      chips = emptyChips()
      chipGrid = emptyGrid()
    }

  /** Creates an empty grid with the size of chips. If mouse clicked on the grid, insert a new chip and update the status text.
    *
    * @return
    *   Returns an empty GridPane.
    */
  def emptyGrid(): GridPane =
    new GridPane:
      for ((subList, i) <- chips.zipWithIndex) {
        for ((element, j) <- subList.zipWithIndex) {
          element.onMouseClicked = (event: MouseEvent) =>
            restController.doAndPublishInsertChip(i)
            playgroundstatus.text = restController.getPlaygroundState;
            statestatus.text = restController.getGameState
          add(element, i, j)
        }
      }

  /** Stops the application and exit. */
  override def stopApp(): Unit =
    super.stopApp()
    System.exit(0)
