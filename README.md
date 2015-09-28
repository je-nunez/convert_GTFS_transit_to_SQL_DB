# Convert GTFS CSV files to a SQL DB

Convert a `General Transit Feed Specification CSV Set` to a SQL DB using Slick on Scala.

# WIP

This project is a *work in progress*. The implementation is *incomplete* and
subject to change. The documentation can be inaccurate.

# How to use it

This version of the program converts the General Transit Feed Specification CSV Set
to a `SQLite` database. (To convert the GTFS set to a different SQL database, just
change in `./db/DbGtfs.scala` both the `jdbcDrvr` value to another `JDBC driver`
string, and the `Database.forURL` Slick call to have the connection parameters to
this other SQL database.)

    ./GtfsCsvToDb.scala  [--ignore-agencies]  \
                          <input-directory-containing-GTFS-CSV-files>  \
                          [ <output-directory-to-contain-SQLite-db> ]

where the input and output directories contain respectively the GTFS CSV file set,
and the output directory will contain a new `gtfs.db` file (this filename can be
changed in the global `./config/Config.scala` file, the `gtfsDbFilename` value.)

If the `<output-directory-to-contain-SQLite-db>` is omitted, then it is assumed by
default to be the same `<input-directory-containing-GTFS-CSV-files>`.

The option `--ignore-agencies` is to ignore the validation for a unique transit
`agency` in the GTFS `routes.txt` CSV file: if this GTFS file has only one transit
agency, then `--ignore-agencies` is a `no-op` validation. On the other hand, if
the GTFS `routes.txt` CSV file contains routes of multiple transit agencies, then
you need to specify the `--ignore-agencies` in the conversion, otherwise the
insertion to the SQL DB will have only those routes for the first agency appearing
in the `routes.txt` CSV file. (Probably this option should be always implicitly
assumed, and instead the explicit option should be similar to
`--insert-only-routes-for=transit-agency-value`, and then the insertion into
the DB would be only for those routes of the requested `transit-agency-value`.
This is the first version of the program though.)

# Required libraries

You should have in the `CLASSPATH` of your Java Virtual Machine:

   . Scala Slick JAR file

   . Xerial SQLite-JDBC JAR file

(other libraries required by `Scala Slick`, like `Reactive Streams`, `slf4j`, etc)

Note that the main program, `./GtfsCsvToDb.scala`, specifies the parameters to the
JVM:

       -J-Xmx4g -J-XX:NewRatio=4

respectively for the maximum JVM heap-size (for the GTFS File Set can be big), and
for the `New Generation` Heap ratio (for the program tries to allocate all its
data structures only in the New Generation to avoid costly Full Garbage Collections
in the Tenured Generations): both parameters can be changed according to your case.

