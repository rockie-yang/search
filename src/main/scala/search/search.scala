package search

import java.io.File
import scala.Predef._


object search {
  def main(args: Array[String]) {
    run(args)
  }

  // this is just a test for search
  val default = Map(
    "verbose" -> "true",
    "filePattern" -> ".*",
    "searchDepth" -> Int.MaxValue,
    "searchLines" -> Int.MaxValue,
    "caseSensitive" -> "false",
    "debug" -> "true",
    "invertMatch" -> ""
  )

  val usage = """
                |Usage: search [-p filePattern] [-d searchDepth] [-l searchLines] [-v invertMatchStr] [-i]
                |  [--verbose]
                |  path pattern [pattern ...]
                |   if don't specify -p the filePattern, then try to search all files
                |   if don't specify -d the searchDepth, then search all sub folders
                |   if don't specify -f the searchLine, then search all lines
                |   if don't specify -i, match with case, otherwice ignore case
              """.stripMargin

  def run(args: Array[String]): List[Result] = {
    val options = parseArgs(args)

    val result = grep(options)

    println("search finished")

    result
  }


  def parseArgs(args: Array[String]): SearchConfig = {
    require(args.length >= 2, usage)

    def nextOption(map: Map[String, Any], list: List[String]): (Map[String, Any], List[String]) =
      list match {
        case "-p" :: filePattern :: tail =>
          nextOption(map ++ Map("filePattern" -> filePattern), tail)
        case "-d" :: searchDepth :: tail =>
          nextOption(map ++ Map("searchDepth" -> searchDepth.toInt), tail)
        case "-l" :: searchLines :: tail =>
          nextOption(map ++ Map("searchLines" -> searchLines.toInt), tail)
        case "--verbose" :: tail =>
          nextOption(map ++ Map("verbose" -> "true"), tail)
        case "-i" :: tail =>
          nextOption(map ++ Map("caseSensitive" -> "true"), tail)
        case "-v" :: invertMatch :: tail =>
          nextOption(map ++ Map("invertMatch" -> invertMatch), tail)
        case others => (map, others)
      }


    var (options, left) = nextOption(default, args.toList)

    require(left.length >= 2, usage)

    options += ("path" -> left.head)
    options += ("pattern" -> left.tail)

    options

    val config = new SearchConfig(options, left.tail)

    config
  }

  def grep(config: SearchConfig): List[Result] = {


//    val filePattern = options("filePattern").toString
//    val searchDepth = options("searchDepth").toString.toInt
//    val searchLines = options("searchLines").toString.toInt
//    val pattern = options("pattern").toString
//    val path = options("path").toString
//    val caseSensitive = options("caseSensitive").toString.toBoolean
//    val verbose = options("verbose").toString.toBoolean

//    val debug = options("debug").toString.toBoolean
    val listener = new ResultPrintOutListener(config.debug)

    val includes = StringMatcher("contain", config.ignoreCase, config.pattern)
    val invertMatch = config.invertMatch
    val excludes = StringInverseMatcher("contains", config.ignoreCase, List(invertMatch))

    val matchers = new StringMatchers(includes, excludes)

    val filePredicate = (file: File) => file.getName.matches(config.filePattern)
    val textGreper = new TextGreper(listener, matchers, config.searchLines)
    val traverser = new FileTraverser(filePredicate, textGreper, config.searchDepth, config.verbose)

    traverser.traverse(config.path)

    listener.results
  }
}
