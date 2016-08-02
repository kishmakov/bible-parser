import java.io.{File, PrintWriter}
import scala.io.Source

import scalaj.http._

object Parser {
  def downloadChapters(baseDir: String, firstId: Int, lastId: Int, url: StringContext): Unit = {
    new File(baseDir) mkdir()
    for (chapterId <- firstId to lastId) {
      val response: HttpResponse[String] = Http(url.s(chapterId)).asString
      val writer = new PrintWriter(baseDir + s"/$chapterId.html")
      writer.print(response.body)
      writer.close()
      Thread sleep 5000
    }
  }

  //    val pattern = """<span class=snosCit>[^<]+</span><font color=green>\*</font>""".r
  //
  //    for(line <- Source.fromFile("inp.txt").getLines) {
  //      val matches = pattern.findAllIn(line)
  //      for (m <- matches)
  //        println(m)
  //    }


  def parseRussian(bookCode: String, lines: Iterator[String], chapterId: Int, writer: PrintWriter): Unit = {
    var flag = false
    val prefix = "<li class=\"verse visible-1\"><div class=\"verse-inner\">"
    val suffix = "</div></li>"
    var verseId = 0

    lines.foreach(line => {
      flag = flag || (line contains "<ul id=\"list-verses-r\" class=\"verses\">")
      flag = flag && !(line contains "</ul>")
      if (flag && (line contains prefix) && (line contains suffix)) {
        val beg = (line indexOf prefix) + prefix.length
        val end = line indexOf suffix
        val verse = line.substring(beg, end).replace(";", ";;").replaceAll("""<[^>]+>""", "")
        verseId += 1
        writer.printf("    (\"ru\", \"%s\", %d, %d, \"%s\"),\n",
          bookCode, int2Integer(chapterId), int2Integer(verseId), verse)
      }
    })

    writer.println()
  }

  def parseSlavonic(bookCode: String, lines: Iterator[String], chapterId: Int, writer: PrintWriter): Unit = {
    var flag = false
    val prefix = "<li class=\"verse visible-1\"><div class=\"verse-inner\">"
    val suffix = "</div></li>"
    var verseId = 0


    lines.foreach(line => {
      flag = flag || (line contains "<ul id=\"list-verses-ucs\" class=\"verses sublang lang-ucs\" >")
      flag = flag && !(line contains "</ul>")
      if (flag && (line contains prefix) && (line contains suffix)) {
        val beg = (line indexOf prefix) + prefix.length
        val end = line indexOf suffix
        val verse = line.substring(beg, end).replace(";", ";;").replaceAll("""<[^>]+>""", "")
        verseId += 1
        writer.printf("    (\"chu\", \"%s\", %d, %d, \"%s\"),\n",
          bookCode, int2Integer(chapterId), int2Integer(verseId), verse)
      }
    })

    writer.println()
  }

  def main(args: Array[String]): Unit = {
//    downloadChapters("Mt", 25, 28, StringContext("http://azbyka.ru/biblia/?Mt.", "&ucs")
//    downloadChapters("Lc", 1, 24, StringContext("http://azbyka.ru/biblia/?Lk.", "&ucs"))
//    downloadChapters("Mc", 8, 16, StringContext("http://azbyka.ru/biblia/?Mk.", "&ucs"))
//    downloadChapters("In", 1, 21, StringContext("http://azbyka.ru/biblia/?Jn.", "&ucs"))
//    downloadChapters("Act", 15, 28, StringContext("http://azbyka.ru/biblia/?Act.", "&ucs"))
//    downloadChapters("Apoc", 1, 22, StringContext("http://azbyka.ru/biblia/?Apok.", "&ucs"))

    val writer = new PrintWriter("some.sql")

    for (chapterId <- 1 to 22) {
      val lines = Source.fromFile(s"Apoc/$chapterId.html").getLines
      parseSlavonic("Apoc", lines, chapterId, writer)
    }

    writer.close()
  }
}
