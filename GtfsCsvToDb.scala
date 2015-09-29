#!/usr/bin/env scala -deprecation -J-Xmx4g -J-XX:NewRatio=4

import _root_.java.util.Calendar

import scala.collection.immutable.Map
import scala.collection.JavaConversions._

/* Internal packages of this project. In this very first version, they
 * have this simple prefix, but will surely change. */

import src.main.scala.logging.Logging._
import src.main.scala.config.Config
import src.main.scala.model.ConvertGtfsCsvControllerToDb
import src.main.scala.db.DbGtfs

/*
 *   MAIN PROGRAM
 */

object convertGtfsCsvToDb {

  type OptionMap = Map[String, String]

  def processCmdLineArgs(map: OptionMap, list: List[String]): OptionMap = {

    def invalidCommandLineOptions(options: List[String]) {
      logMsg(EMERGENCY, "Unknown command-line argument: " +
                         options.mkString(" "))
      sys.exit(1)
    }

    list match {
      case Nil => map
      case "--ignore-agencies" :: tail =>
            processCmdLineArgs(map ++ Map("ignore-agencies" -> "true"), tail)
      case string :: tail if (! map.contains("gtfs-dir")) =>
            processCmdLineArgs(map ++ Map("gtfs-dir" -> string), tail)
      case string :: tail if (! map.contains("result-dir")) =>
            processCmdLineArgs(map ++ Map("result-dir" -> string), tail)
      case aRemainder => {
                  invalidCommandLineOptions(aRemainder)
                  map
                }
    }
  }


  def main(cmdLineArgs: Array[String]) {

    val cmdLineOptions = processCmdLineArgs(Map(),cmdLineArgs.toList)
    logMsg(DEBUG, "Parsed command-line options: " +
                   cmdLineOptions.mkString("\n\t"))

    val download_time = Calendar.getInstance().getTime()

    // In what directory we should find the GTFS CSV files

    var gtfsInputDirect: String = "."

    if (cmdLineOptions.contains("gtfs-dir"))
      gtfsInputDirect = cmdLineOptions("gtfs-dir")

    var dstOutputDirect: String = gtfsInputDirect  // same input and output

    if (cmdLineOptions.contains("result-dir"))
      dstOutputDirect = cmdLineOptions("result-dir")

    val ignoreAgencyValidation = cmdLineOptions.contains("ignore-agencies")

    val gtfsDb = new DbGtfs(dstOutputDirect)

    val gtfsConversionModel =
      new ConvertGtfsCsvControllerToDb(gtfsInputDirect, gtfsDb)

    gtfsConversionModel.loadGtfsModelIntoDb(ignoreAgencyValidation)

  }
} // end of object convertGtfsCsvToDb

