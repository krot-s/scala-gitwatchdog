import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Matchers._
import org.scalatest.matchers.BePropertyMatcher
import org.scalatest.matchers.BePropertyMatchResult

class FunTests extends FunSuite with BeforeAndAfter with ShouldMatchers {
	class Bean(id: Int, _hasText: Boolean){
	  def hasText = _hasText
	}
	
	class BeanCustomMatchers extends BePropertyMatcher[Bean] {
	  def apply(left: Bean) = BePropertyMatchResult(left.hasText, "hasText")
	}
	val hasText = new BeanCustomMatchers 
	
	before {
	  println("Before executed")
	}
	
	test("do stuff") {
	  println("soing stuff")
	  val x = new Bean(1, true)
	  x should be a (hasText)
	}
}