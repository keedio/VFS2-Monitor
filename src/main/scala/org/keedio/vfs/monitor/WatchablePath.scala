package org.keedio.vfs.monitor

import java.util.concurrent.{Executors, ScheduledExecutorService, ScheduledFuture, TimeUnit}

import org.apache.commons.vfs2._
import org.apache.commons.vfs2.impl.DefaultFileMonitor

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex


/**
 * Created by luislazaro on 4/9/15.
 * lalazaro@keedio.com
 * Keedio
 */

class WatchablePath(uri: String, refresh: Int, start: Int, regex: Regex) {

    private val fileObject: FileObject = FileObjectBuilder.getFileObject(uri)

    //list of susbcribers(observers) for changes in fileObject
    private val listeners: ListBuffer[StateListener] = new ListBuffer[StateListener]

    //observer for changes to a file
    private val fileListener = new FileListener {
        override def fileDeleted(fileChangeEvent: FileChangeEvent): Unit = {
            val eventDelete: StateEvent = new StateEvent(fileChangeEvent, State.ENTRY_DELETE)
            fireEvent(eventDelete)
        }

        override def fileChanged(fileChangeEvent: FileChangeEvent): Unit = {
            val eventChanged: StateEvent = new StateEvent(fileChangeEvent, State.ENTRY_MODIFY)
            fireEvent(eventChanged)
        }

        override def fileCreated(fileChangeEvent: FileChangeEvent): Unit = {
            val eventCreate: StateEvent = new StateEvent(fileChangeEvent, State.ENTRY_CREATE)
            fireEvent(eventCreate)
        }
    }

    //Thread based polling file system monitor with a 1 second delay.

    private val defaultMonitor: DefaultFileMonitor = new DefaultFileMonitor(fileListener)
    defaultMonitor.addFile(fileObject)
    defaultMonitor.setRecursive(true)
    defaultMonitor.setDelay(secondsToMiliseconds(refresh))


    // the number of threads to keep in the pool, even if they are idle
    private val corePoolSize = 1
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize)
    //Creates and executes a one-shot action that becomes enabled after the given delay
    private val tasks: ScheduledFuture[_] = scheduler.schedule(
        getTaskToSchedule(),
        start,
        TimeUnit.SECONDS
    )

    /**
     * Call this method whenever you want to notify the event listeners about a
     * FileChangeEvent.
     * Filtering monitored files via regex is made after and event is fired.
     */
    def fireEvent(stateEvent: StateEvent): Unit = {
        val fileName: String = stateEvent.getFileChangeEvent.getFile.getName.getBaseName
        regex.findFirstIn(fileName).isDefined match {
            case true => listeners foreach (_.statusReceived(stateEvent))
            case false => ()
        }
    }

    /**
     * Add element to list of registered listeners
     * @param listener
     */
    def addEventListener(listener: StateListener): Unit = {
        listener +=: listeners
    }

    /**
     * Remove element from list of registered listeners
     * @param listener
     */
    def removeEventListener(listener: StateListener): Unit = {
        listeners.find(_ == listener) match {
            case Some(listener) => {
                listeners.remove(listeners.indexOf(listener))
            }
            case None => ()
        }
    }

    /**
     *
     * auxiliar for using seconds where miliseconds is requiered
     * @param seconds
     * @return
     */
    implicit def secondsToMiliseconds(seconds: Int): Long = {
        seconds * 1000
    }


    /**
     * Make a method runnable and schedule for one-shot
     * @return
     */
    def getTaskToSchedule(): Runnable = {
        new Runnable {
            override def run(): Unit = {
                defaultMonitor.start()
            }
        }
    }


    def getPathForMonitor = fileObject

    def getDefaultMonitor = defaultMonitor

}
