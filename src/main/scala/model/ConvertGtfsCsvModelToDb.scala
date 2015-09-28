package src.main.scala.model

import scala.collection.JavaConversions._

import src.main.scala.logging.Logging._
import src.main.scala.config.Config
import src.main.scala.db.DbGtfs
import src.main.scala.opendata._

class ConvertGtfsCsvModelToDb(
     val srcGtfsCvsDirectory: String,
     val dstGtfsDb: DbGtfs
  ) {


  def loadGtfsModelIntoDb(beLaxValidatingTransitAgencies: Boolean) {

    // Convert the Bus-Stops geometries in the GTFS CSV file

    val busStops = new ConvertBusStopsCsvToDbTable(srcGtfsCvsDirectory)

    busStops.etlGtfsCsvToDbTable(dstGtfsDb)

    // Convert the Bus-Routes in the GTFS CSV file

    val busRoutes = new ConvertRoutesCsvToDbTable(srcGtfsCvsDirectory)

    busRoutes.etlGtfsCsvToDbTable(dstGtfsDb, beLaxValidatingTransitAgencies)

    // Convert the Route-Shapes geometries in the GTFS CSV file

    val routeShapes = new ConvertRouteShapesCsvToDbTable(srcGtfsCvsDirectory)

    routeShapes.etlGtfsCsvToDbTable(dstGtfsDb)

    // Convert the Bus-Trips geometries in the GTFS CSV file

    val busTrips = new ConvertBusTripsCsvToDbTable(srcGtfsCvsDirectory)

    busTrips.etlGtfsCsvToDbTable(dstGtfsDb)

    // Convert the Bus-Stop Times in GTFS

    val busStopTimes = new ConvertBusStopTimesCsvToDbTable(srcGtfsCvsDirectory)

    busStopTimes.etlGtfsCsvToDbTable(dstGtfsDb)
  } // end of method loadGtfsModel()

} // end of class TrafficSpeedBetweenStops

