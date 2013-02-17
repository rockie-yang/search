package search

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 2/7/13
 */
class SearchConfig(options: Map[String, Any], val pattern: List[String]) {
  val filePattern = options("filePattern").toString
  val searchDepth = options("searchDepth").toString.toInt
  val searchLines = options("searchLines").toString.toInt
//  val pattern = options("pattern").toString
  val path = options("path").toString
  val ignoreCase = options("caseSensitive").toString.toBoolean
  val verbose = options("verbose").toString.toBoolean
  val debug = options("debug").toString.toBoolean

  val invertMatch = options("invertMatch").toString
}
