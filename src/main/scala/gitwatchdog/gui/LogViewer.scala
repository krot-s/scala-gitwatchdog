package gitwatchdog.gui


import java.awt.Dimension
import scala.collection.immutable.List
import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication
import scala.swing.Table
import scala.swing.Window
import scala.swing.Orientation
import scala.swing.Swing
import java.awt.BorderLayout
import javax.swing.text.Position
import scala.swing.GridBagPanel
import java.awt.GraphicsConfiguration
import java.awt.GraphicsEnvironment
import scala.swing.Reactions
import javax.swing.Action
import scala.swing.event.ActionEvent
import scala.swing.event.ButtonClicked


object Swing3 extends SimpleSwingApplication {
	def top = new MainFrame {
	  title = "Commits accepted"
	  
	  val button = new Button("Close")
	  listenTo(button)
	  this.reactions += {
	    case ButtonClicked(b) => println("sdfsdf")
	  } 	  
	  
	  val records = List.fill(10)(Rec("1ffffffffffffffffffffffffdddddddddd", "2"))	  	  
	  val table = new Table(records.map(_.toArray).toArray , Array("col1", "col2"))
	  
	  contents = new GridBagPanel {
	    val top = new Constraints {
	    	fill = GridBagPanel.Fill.Both
	    	weightx = 1
	    	weighty = 1
	    }
		layout(table) = top
		
		val bottom = new Constraints {
			fill = GridBagPanel.Fill.Horizontal
			gridx = 0
			gridy = 1
		}
		layout(new FlowPanel(FlowPanel.Alignment.Right)(button)) = bottom
	  }
    
	  val screenSize = toolkit.getScreenSize()
	  val screensCount = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length
	  size = new Dimension(screenSize.width / (2 * screensCount), screenSize.height / 2)
	  centerOnScreen
	}
	
	case class Rec(val col1: String, val col2: String){
	  def toArray : Array[Any] = Array(col1, col2)
	}
}