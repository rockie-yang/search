package search

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */

@RunWith(classOf[JUnitRunner])
class TextGreperTest extends FunSuite {
  test("using factory") {
    println("test")


    val file = new File(this.getClass.getResource("SingleText.txt").getFile)

    val existListener = new ResultPrintOutListener(holdResult = true)
    val grepWithMatcher = new TextGreper(existListener, StringMatcher("contain", caseSensitive = true, List("text")))
    grepWithMatcher(file)
    println(existListener)
    assert(existListener.results === List(Result(file, List("text"))))

    val noneExistListener = new ResultPrintOutListener(holdResult = true)
    val grepWithInverseMatcher = new TextGreper(noneExistListener, StringInverseMatcher("contain", ignoreCase = true, List("text")))
    grepWithInverseMatcher(file)
    println(noneExistListener)
    assert(noneExistListener.results === List[Result]())
  }
}
