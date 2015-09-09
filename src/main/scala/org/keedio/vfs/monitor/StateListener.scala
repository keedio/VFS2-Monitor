package org.keedio.vfs.monitor

import org.apache.commons.vfs2.FileChangeEvent

/**
 * Created by luislazaro on 9/9/15.
 * lalazaro@keedio.com
 * Keedio
 */

/**
 * Mix-in this trait to become a listener, implementing
 * what to do where an event ins received.
 */
trait StateListener {
      def statusReceived(fileChangeEvent: FileChangeEvent): Unit
}
