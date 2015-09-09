package org.keedio.vfs.monitor

import org.apache.commons.vfs2._
import org.apache.commons.vfs2.impl.DefaultFileMonitor
import org.junit.Test


/**
 * Created by luislazaro on 8/9/15.
 * lalazaro@keedio.com
 * Keedio
 */
class DefaultMonitorHDFSTest {

      /**
       * @see https://commons.apache.org/proper/commons-vfs/filesystems.html
       */
      @Test
      def testApiFileMonitorHDFSFileSystem(): Unit = {
            val fsManager = VFS.getManager

            //        val opts: FileSystemOptions = new FileSystemOptions()
            //        val hdfsBuilder = HdfsFileSystemConfigBuilder.getInstance()
            //        hdfsBuilder.setConfigName(opts, "hdfsSystem")

            //monitoring
            val eventSource: FileObject = fsManager.resolveFile("hdfs://10.129.135.140:8020/tmp/")
            val children: Array[FileObject] = eventSource.getChildren
            children.foreach(f => println(f.getName.getBaseName))

            val defaultFileMonitor = new DefaultFileMonitor(new FileListener {
                  override def fileCreated(fileChangeEvent: FileChangeEvent): Unit = {
                        println("file created")
                        println("ruta: " + fileChangeEvent.getFile.getName)
                        println("nombre: " + fileChangeEvent.getFile.getName.getBaseName)

                  }

                  override def fileDeleted(fileChangeEvent: FileChangeEvent): Unit = {
                        println("file deleted")

                  }

                  override def fileChanged(fileChangeEvent: FileChangeEvent): Unit = println("file changed")


            })

            defaultFileMonitor.addFile(eventSource)
            defaultFileMonitor.setRecursive(true)
            defaultFileMonitor.setDelay(0) //if not set or set to 0 seconds, file changed is not fired so it is not detected.
            defaultFileMonitor.start()
            Thread.sleep(10000)
            defaultFileMonitor.stop()

      }


}
