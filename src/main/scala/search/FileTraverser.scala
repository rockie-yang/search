package search

import java.io.File

/**
 * Traverse to a file or a directory by apply the @processor to max @depth level
 *
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/26/13
 */
class FileTraverser(filePred: File => Boolean, processor: FileProcessor, depth: Int = Int.MaxValue, verbose: Boolean = false) {

  def traverse(path: File, level: Int): Unit = {
    if (path.exists()) {

      if (path.isFile) {
        if (filePred(path)) processor(path)
      }
      else {
        // if still need traverse more level
        if (level < depth) {
          if (verbose)
            println("traverse directory " + path.getAbsolutePath)

          val children = path.listFiles()

          if (children != null) {
            // search files in the directory first, then search the sub directory
            val (files, dirs) = children.partition(f => f.isFile)

            files foreach (file => traverse(file, level + 1))
            dirs foreach (dir => traverse(dir, level + 1))
          }
        }
      }
    }
    else {
      // the file or directory just been deleted after trigger the the search
      println(path.getAbsolutePath + "just been deleted after trigger the the search")
    }
  }

  def traverse(path: String): Unit = {
    traverse(new File(path), 0)
  }
}
