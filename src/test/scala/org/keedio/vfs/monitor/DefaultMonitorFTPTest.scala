package org.keedio.vfs.monitor

import java.io.IOException

import org.apache.commons.vfs2._
import org.apache.commons.vfs2.impl._
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder
import org.junit._


/**
 * Created by luislazaro on 8/9/15.
 * lalazaro@keedio.com
 * Keedio
 */
class DefaultMonitorFTPTest {

      /**
       * Test vfs2 on FTP filesytem
       */
      @Test
      def testApiFileMonitorFTPFileSystem(): Unit = {
            val fsManager = VFS.getManager

            val opts: FileSystemOptions = new FileSystemOptions()
            val builder = FtpFileSystemConfigBuilder.getInstance()
            builder.setUserDirIsRoot(opts, false)
            builder.setPassiveMode(opts, false)

            //just accesing ftp server
            val fileObject = fsManager.resolveFile("ftp://mortadelo:filemon@IP/home/mortadelo/ftp/", opts)
            val children = fileObject.getChildren
            children.foreach(f => println(f.getName.getBaseName))

            val file1 = fsManager.resolveFile("ftp://mortadelo:filemon@IP/home/mortadelo/ftp/file4.txt", opts)

            //monitoring
            val listendir: FileObject = fsManager.resolveFile("ftp://mortadelo:filemon@IP/home/mortadelo/ftp", opts)

            val fm = new DefaultFileMonitor(new FileListener {
                  override def fileDeleted(fileChangeEvent: FileChangeEvent): Unit = println("file deleted")

                  override def fileChanged(fileChangeEvent: FileChangeEvent): Unit = println("file changed")

                  override def fileCreated(fileChangeEvent: FileChangeEvent): Unit = println("file created")
            })

            fm.setRecursive(true)
            fm.addFile(listendir)
            fm.setDelay(1) //if not set or set to 0 seconds, file changed is not fired so it is not detected.
            fm.start()

            try {
                  file1.createFile()
                  Thread.sleep(1000)
                  file1.delete()
            } catch {
                  case e: IOException => println("I/O: ", e)
            }

            fm.stop()

      }
}
