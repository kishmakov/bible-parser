import java.io.{File, PrintWriter}
import scala.io.Source

import scalaj.http._

object Parser {
  def downloadChapters(baseDir: String, chapters: Int, url: StringContext): Unit = {
    new File(baseDir) mkdir()
    for (chapterId <- 1 to chapters) {
      val response: HttpResponse[String] = Http(url.s(chapterId)).asString
      val writer = new PrintWriter(baseDir + s"/$chapterId.html")
      writer.print(response.body)
      writer.close()
      Thread sleep 1000
    }

  }

  def main(args: Array[String]): Unit = {
//    val pattern = """<span class=snosCit>[^<]+</span><font color=green>\*</font>""".r
//
//    for(line <- Source.fromFile("inp.txt").getLines) {
//      val matches = pattern.findAllIn(line)
//      for (m <- matches)
//        println(m)
//    }

//    downloadChapters("Mt", 28, StringContext("http://azbyka.ru/biblia/?Mt.", "&ucs"))
//    downloadChapters("Lc", 24, StringContext("http://azbyka.ru/biblia/?Lk.", "&ucs"))


//    for (chapterId <- 1 to 28) {

//      val lines = response.body.split("\n")
//
//
//      var flag = false
//      val prefix = "<li class=\"verse visible-1\"><div class=\"verse-inner\">"
//      val suffix = "</div></li>"
//      var verseId = 0
//
//      lines.foreach(line => {
//        flag = flag || (line contains "<ul id=\"list-verses-r\" class=\"verses\">")
//        flag = flag && !(line contains "</ul>")
//        if (flag && (line contains prefix) && (line contains suffix)) {
//          val beg = (line indexOf prefix) + prefix.length
//          val end = line indexOf suffix
//          verseId += 1
//          writer.printf("    (\"ru\", \"Mt\", %d, %d, \"%s\"),\n",
//            int2Integer(chapterId), int2Integer(verseId), line.substring(beg, end))
//        }
//      })
//    }
  }
}
