package org.keedio.vfs.monitor.watchable

import java.io.IOException
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.util.{Calendar, Date}

import org.junit._
import org.keedio.vfs.monitor.{StateEvent, StateListener, WatchablePath}
import org.slf4j.{Logger, LoggerFactory}

import scala.util.matching.Regex


/**
 * Created by luislazaro on 9/9/15.
 * lalazaro@keedio.com
 * Keedio
 */
class WatchablePathTest {

    val LOG: Logger = LoggerFactory.getLogger(classOf[WatchablePathTest])
    val csvRegex: Regex = """[^.]*\.csv?""".r

    val userPath = System.getProperty("user.dir")
    val fileSeparator = System.getProperty("file.separator")
    val csvDir = userPath + fileSeparator + "src/test/resources/csv/"


    /**
     * For 20 seconds (10 iterations * 2 seconds) and every
     * 2 seconds, csv's directory will be checked. Each two iterations
     * and action will be taken over the files, i.e, delete file, append file,
     * create file. According the action a event will be fired.
     */
    @Test
    def testWatchPath(): Unit = {
        println("##### testWatchPath : monitor directory a send events according actions  ")
        val refreshTime = 2
        val startTime = 2
        val watchable = new WatchablePath(csvDir, refreshTime, startTime, csvRegex)
        val listener = new StateListener {
            override def statusReceived(event: StateEvent): Unit = {
                println("listener received event: " + event.getState.toString()
                    + " on element " + event.getFileChangeEvent.getFile.getName )
            }
        }
        watchable.addEventListener(listener)
        conditionsGenerator(10, 2000) //(10 iterations * 2 seconds)
        watchable.removeEventListener(listener)
    }

    /**
     * Take actions over a directory to produce a response over time
     * @param iterations
     * @param timeToSleep
     */
    def conditionsGenerator(iterations: Int, timeToSleep: Long): Unit = {
        for (i <- 1 to iterations) {
            Thread.sleep(timeToSleep)
            println("iteration " + i)
            i match {
                case 3 =>
                    println("Action taken is creating new files")
                    try {
                        for (i <- 1 to 5)
                            Files.createFile(Paths.get(s"src/test/resources/csv/file_Created${i}.csv"))

                    } catch {
                        case e: IOException => LOG.error("I/O: conditionsGenerator", e)
                            assert(false)
                    }

                case 5 =>
                    println("Action taken is appending to files")
                    try {
                        for (i <- 1 to 5)
                        Files.write(Paths.get(s"src/test/resources/csv/file_Created${i}.csv"),
                            (getCurrentDate + "\n").getBytes(),
                            StandardOpenOption.APPEND
                        )

                    } catch {
                        case e: IOException => LOG.error("I/O: conditionsGenerator", e)
                            assert(false)
                    }

                case 7 =>
                    println("Action taken is deleting files")
                    try {
                        for (i <- 1 to 5)
                            Files.deleteIfExists(Paths.get(s"src/test/resources/csv/file_Created${i}.csv"))

                    } catch {
                        case e: IOException => LOG.error("I/O: conditionsGenerator", e)
                            assert(false)
                    }

                case 10 => println("end")
                    try {
                        Files.deleteIfExists(Paths.get("src/test/resources/csv/file1.csv"))
                    } catch {
                        case e: IOException => LOG.error("I/O: conditionsGenerator", e)
                            assert(false)
                    }
                case _ => ()

            }

        }
    }

    /**
     * get current date
     * @return
     */
    def getCurrentDate: String = {
        val today: Date = Calendar.getInstance().getTime()
        today.toString + System.currentTimeMillis()
    }

}
