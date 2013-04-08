import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Matchers._

class FunTests extends FunSuite with BeforeAndAfter with ShouldMatchers {
	before {
	  println("Before executed")
	}
	
	test("do stuff") {
	  println("soing stuff")
	  1 should equal (1)
	}
}