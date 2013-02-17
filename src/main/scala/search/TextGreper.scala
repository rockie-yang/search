package search

import collection.Iterator
import java.io.File

/**
 * Grep a text file by the @matcher, send the result to the @listener
 *
 * In order to avoid get all file data to memory for large files, can specify the @grepLines
 * By default, it will search all lines in the file
 *
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */

class TextGreper(listener: ResultListener, matcher: StringPredicate, grepLines: Int = Int.MaxValue) extends FileProcessor {
  def apply(file: File) {
    if (FileTypeDetector.isTextFile(file)) {
      try {
        // getLines function use lazy evaluation
        val lines = scala.io.Source.fromFile(file).getLines()
        val matches = lines.toList.filter((line: String) => matcher(line.trim()))

        // TODO why using iterator seems getting slower
        //        // if need grep all lines, then get it into memory, otherwise keep lazy load
        //        val iterator = if (grepLines == Int.MaxValue) lines.toList.toIterator else lines
        //
        //        val matches = filter(iterator, (line: String) => matcher(line.trim()), grepLines)

        if (!matches.isEmpty)
          listener(Result(file, matches))
      }
      catch {
        case ex: Exception => println(ex.toString)
      }
    }
  }

  /**
   * filter on the iterator directly with specified @maxFilter.
   *
   * It is mainly use for filter very large collections,
   * since it does not need traverse all through the whole collection
   */
  private def filter[T](it: Iterator[T], predicate: T => Boolean, maxFilter: Int = Int.MaxValue): List[T] = {
    val result =
      for {i <- 0 until maxFilter
           if (it.hasNext)
           item = it.next()
           if predicate(item)
      } yield item
    result.toList
  }
}
