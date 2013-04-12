import java.util.Date
import java.util.Calendar

object DateUtils {
  private final val calendar = Calendar.getInstance()
  def day(date: Date) = calendar.get(Calendar.DAY_OF_MONTH)
  def month(date: Date) = calendar.get(Calendar.MONTH)  
  def year(date: Date) = calendar.get(Calendar.YEAR)
  def hour(date: Date) = calendar.get(Calendar.HOUR_OF_DAY)
  def minute(date: Date) = calendar.get(Calendar.MINUTE)
}