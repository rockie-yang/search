package search

import java.io.File

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/29/13
 */


trait FileProcessor {

  def apply(file: File)
}

class SleepProcessor(sleepMillSecond: Int = 1) extends FileProcessor {
  def apply(file: File) {
    Thread.sleep(sleepMillSecond)
  }
}

class FileProcessors(processors: FileProcessor*) extends FileProcessor {
  def apply(file: File) {
    processors.foreach(processor => processor(file))
  }
}

