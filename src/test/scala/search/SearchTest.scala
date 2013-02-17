package search

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 2/8/13
 */
@RunWith(classOf[JUnitRunner])
class SearchTest extends FunSuite {

  def getPath(path: String) = {
    val root = new File(".").getAbsolutePath
    root.substring(0, root.length - 1)  + path
  }

  test("single file search") {
    val file = new File(this.getClass.getResource("SingleText.txt").getFile)
    val path = file.getAbsolutePath // getPath("src/test/scala/search/SingleText.txt")
    val args = Array(path, "text")
    val results = search.run(args)
    assert(results === List(Result(file, List("text"))))
  }
}
