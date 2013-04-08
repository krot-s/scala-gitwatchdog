import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
class MyTests extends FlatSpec with ShouldMatchers {
	"Some shit" should "rock" in {
	  1 should equal (1)
	}
}