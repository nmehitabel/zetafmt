using scala "3"
import $dep.`com.google.zetasql:zetasql-client:2021.09.1`
import $dep.`com.google.zetasql:zetasql-jni-channel:2021.09.1`
import $dep.`com.google.zetasql:zetasql-types:2021.09.1`

import com.google.zetasql.SqlException
import com.google.zetasql.SqlFormatter
import java.io.File
import scala.util.{Try, Success, Failure}
import scala.io.Source
import scala.io.StdIn.readLine

final case class ZetaFormatFailure(e: Throwable) extends Exception

object ZetaSqlFormat:

  private val zp = new SqlFormatter()

  def formatStatement(sqlStatement: String): Either[ZetaFormatFailure, String] =

    val f = zp.formatSql(sqlStatement)

    val formattedResult = Try(zp.formatSql(sqlStatement))

    formattedResult match
      case Success(s) => Right(s)
      case Failure(e) => Left(ZetaFormatFailure(e))


object Main:

  def open(path: String) = new File(path)

  extension(f: File)
    def readAll: List[String] = Try(Source.fromFile(f).getLines()) match
      case Success(t) => t.toList
      case Failure(f) => List[String]()

  def readStdin =
    Iterator
      .continually(readLine)
      .takeWhile(_ != null)
      .toList

  def main(args: Array[String]): Unit =

    val input: List[String] =
      if (args.length == 1)
        open(args(0)).readAll
      else
        readStdin

    if (input.isEmpty)
      System.err.println("empty or invalid input")
      System.exit(1)

    val fmt = ZetaSqlFormat.formatStatement(input.mkString("\n"))

    fmt match
      case Right(s) => System.out.println(s)
      case Left(e) =>
        System.err.println(s"Formatter error: ${e.e.getMessage}")
        System.exit(1)
