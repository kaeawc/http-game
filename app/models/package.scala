import java.util._
import java.text.SimpleDateFormat

package object models {
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  def now = utc(new Date)

  def utc(date:Date) = {
    val tz = TimeZone.getDefault()
    var ret = new Date( date.getTime() - tz.getRawOffset() )

    // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
    if ( tz.inDaylightTime( ret )){
      val dstDate = new Date( ret.getTime() - tz.getDSTSavings() )

      // check to make sure we have not crossed back into standard time
      // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
      if ( tz.inDaylightTime( dstDate )){
        ret = dstDate
      }
    }
    ret
  }
}