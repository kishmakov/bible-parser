import java.io.PrintWriter

import scalaj.http._

object Parser {
  def main(args: Array[String]): Unit = {
    val writer = new PrintWriter("4.sql")

    for (chapterId <- 1 to 28) {
      val response: HttpResponse[String] = Http(s"http://azbyka.ru/biblia/?Mt.$chapterId").asString

      val lines = response.body.split("\n")

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
          verseId += 1
          writer.printf("    (\"ru\", \"Mt\", %d, %d, \"%s\"),\n",
            int2Integer(chapterId), int2Integer(verseId), line.substring(beg, end))
        }
      })

      writer.println("");
      Thread sleep 1000
    }

    writer.close()
  }
}
