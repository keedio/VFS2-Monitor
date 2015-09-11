package org.keedio.vfs.monitor

import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder
import org.apache.commons.vfs2.{FileSystemOptions, VFS, FileObject}
import java.net.URI


/**
 * Created by luislazaro on 10/9/15.
 * lalazaro@keedio.com
 * Keedio
 */

/**
 * VFS2 supported file systems requiere is some cases to
 * specify config parameters
 */
object FileObjectBuilder {

    private val fsManager = VFS.getManager
    private val options: FileSystemOptions = new FileSystemOptions()

    /**
     * Get a FileObject for the supported file system.
     * @param uri
     * @return
     */
    def getFileObject(uri: String): FileObject = {
        val scheme = getScheme(uri)
        scheme match {
            case "ftp" => {
                val builder = FtpFileSystemConfigBuilder.getInstance()
                builder.setUserDirIsRoot(options, false)
                builder.setPassiveMode(options, false) //set to true if behind firewall
                fsManager.resolveFile(uri, options)
            }
            case _ => fsManager.resolveFile(uri)
        }
    }

    /**
     * Get the scheme of an URI.
     * @param uriString
     * @return
     */
    def getScheme(uriString: String): String = {
        val uri = new URI(uriString)
        uri.getScheme
    }

}
