package de.htwg.se.beads_util.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.beads_util.util.Command
import de.htwg.se.beads_util.util.UndoManager

class UndoManagerSpec extends AnyWordSpec with Matchers {

  "An UndoManager" when {

    "doing a step" should {

      "add the command to the undo stack and execute the command" in {
        var executed = false
        val command = new Command {
          override def doStep(): Unit = executed = true
          override def undoStep(): Unit = executed = false
          override def redoStep(): Unit = executed = true
        }
        val undoManager = new UndoManager
        undoManager.doStep(command)
        executed shouldBe true
      }
    }

    "undoing a step" should {

      "undo the last executed command and move it to the redo stack" in {
        var executed = false
        val command = new Command {
          override def doStep(): Unit = executed = true
          override def undoStep(): Unit = executed = false
          override def redoStep(): Unit = executed = true
        }
        val undoManager = new UndoManager
        undoManager.doStep(command)
        undoManager.undoStep()
        executed shouldBe false
      }

      "redoing a step" should {

        "redo the last undone command and move it back to the undo stack" in {
          var executed = false
          val command = new Command {
            override def doStep(): Unit = executed = true
            override def undoStep(): Unit = executed = false
            override def redoStep(): Unit = executed = true
          }
          val undoManager = new UndoManager
          undoManager.doStep(command)
          undoManager.undoStep()
          undoManager.redoStep()
          executed shouldBe true
        }
      }
    }
  }
}
