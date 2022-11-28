import controllers.RssController

fun main() {
   val rssURL = "https://elpais.com/rss/elpais/portada.xml"

   val misNoticias = RssController.getNoticias(rssURL)

   misNoticias.forEachIndexed { index, noticia ->
      println("N${index + 1}. $noticia")
   }
}