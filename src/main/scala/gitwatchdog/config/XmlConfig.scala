package gitwatchdog.config

import java.io.File
import scala.xml.Elem
import scala.xml.NodeSeq
import scala.xml.XML
import scala.xml.Node
import scala.collection.mutable.ListBuffer
import gitwatchdog.git.Repository

class Config(val timeout: Long, val repositories: Seq[Repository])

object XmlConfig {
  def read(file: File) = {
    def xmlElemToRepository(elem: Node) = {
      val paths = ListBuffer.empty[String]
      for (path <- elem \ "path")
        paths += path.text.trim()
      new Repository((elem \ "root").text.trim(), paths.toList)
    }

    val xml = XML.loadFile(file)
    val timeout = (xml \\ "timeout").text.trim().toLong
    val repositories = 
      for (repo <- xml \\ "repository") yield xmlElemToRepository(repo)
    new Config(timeout, repositories)
  }
}