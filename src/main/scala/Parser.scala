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

  def parseChapters(bookCode: String, booksNum: Int): Unit = {
    val writerChu = new PrintWriter("chu.sql")
    val writerRu = new PrintWriter("ru.sql")

    for (chapterId <- 1 to booksNum) {
      val (linesChu, linesRu) = Source.fromFile(s"$bookCode/$chapterId.html").getLines.duplicate
      parseSlavonic(bookCode, linesChu, chapterId, writerChu)
      parseRussian(bookCode, linesRu, chapterId, writerRu)
    }

    writerChu.close()
    writerRu.close()
  }

  def main(args: Array[String]): Unit = {
//    downloadChapters("Gen", 1, 50, StringContext("http://azbyka.ru/biblia/?Gen.", "&ucs"))
//    downloadChapters("Ex", 1, 40, StringContext("http://azbyka.ru/biblia/?Ex.", "&ucs"))
//    downloadChapters("Lev", 1, 27, StringContext("http://azbyka.ru/biblia/?Lev.", "&ucs"))

//    downloadChapters("Mt", 25, 28, StringContext("http://azbyka.ru/biblia/?Mt.", "&ucs")
//    downloadChapters("Lc", 1, 24, StringContext("http://azbyka.ru/biblia/?Lk.", "&ucs"))
//    downloadChapters("Mc", 8, 16, StringContext("http://azbyka.ru/biblia/?Mk.", "&ucs"))
//    downloadChapters("In", 1, 21, StringContext("http://azbyka.ru/biblia/?Jn.", "&ucs"))
//    downloadChapters("Act", 15, 28, StringContext("http://azbyka.ru/biblia/?Act.", "&ucs"))

//    downloadChapters("Jac", 3, 5, StringContext("http://azbyka.ru/biblia/?Jac.", "&ucs"))
//    downloadChapters("1Pet", 2, 5, StringContext("http://azbyka.ru/biblia/?1Pet.", "&ucs"))
//    downloadChapters("2Pet", 1, 3, StringContext("http://azbyka.ru/biblia/?2Pet.", "&ucs"))
//    downloadChapters("1In", 2, 5, StringContext("http://azbyka.ru/biblia/?1Jn.", "&ucs"))
//    downloadChapters("2In", 1, 1, StringContext("http://azbyka.ru/biblia/?2Jn.", "&ucs"))
//    downloadChapters("3In", 1, 1, StringContext("http://azbyka.ru/biblia/?3Jn.", "&ucs"))
//    downloadChapters("Jd", 1, 1, StringContext("http://azbyka.ru/biblia/?Juda.", "&ucs"))
//    downloadChapters("Rom", 2, 16, StringContext("http://azbyka.ru/biblia/?Rom.", "&ucs"))
//    downloadChapters("1Cor", 2, 16, StringContext("http://azbyka.ru/biblia/?1Cor.", "&ucs"))
//    downloadChapters("2Cor", 9, 13, StringContext("http://azbyka.ru/biblia/?2Cor.", "&ucs"))
//    downloadChapters("Gal", 2, 6, StringContext("http://azbyka.ru/biblia/?Gal.", "&ucs"))
//    downloadChapters("Eph", 2, 6, StringContext("http://azbyka.ru/biblia/?Eph.", "&ucs"))
//    downloadChapters("Php", 2, 4, StringContext("http://azbyka.ru/biblia/?Phil.", "&ucs"))
//    downloadChapters("Col", 2, 4, StringContext("http://azbyka.ru/biblia/?Col.", "&ucs"))
//    downloadChapters("1Thess", 2, 5, StringContext("http://azbyka.ru/biblia/?1Thes.", "&ucs"))
//    downloadChapters("2Thess", 2, 3, StringContext("http://azbyka.ru/biblia/?2Thes.", "&ucs"))
//    downloadChapters("1Tim", 2, 6, StringContext("http://azbyka.ru/biblia/?1Tim.", "&ucs"))
//    downloadChapters("2Tim", 2, 4, StringContext("http://azbyka.ru/biblia/?2Tim.", "&ucs"))
//    downloadChapters("Tit", 2, 3, StringContext("http://azbyka.ru/biblia/?Tit.", "&ucs"))
//    downloadChapters("Phm", 1, 1, StringContext("http://azbyka.ru/biblia/?Phlm.", "&ucs"))
//    downloadChapters("Hebr", 2, 13, StringContext("http://azbyka.ru/biblia/?Hebr.", "&ucs"))

//    downloadChapters("Apoc", 1, 22, StringContext("http://azbyka.ru/biblia/?Apok.", "&ucs"))

    parseChapters("Hebr", 13)
  }
}
