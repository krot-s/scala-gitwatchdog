package gitwatchdog.git
import java.io.File

/**
 * Watched GIT repository description. Root is path to repository folder (where '.git' folder is located).
 * Paths - list of relative to root folder paths where commits are watched. 
 */
class Repository(val root: File, val paths: List[String] = List.empty) {
  def this(root: String, paths: List[String]) = this(new File(root), paths)
}