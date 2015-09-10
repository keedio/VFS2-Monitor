package org.keedio.vfs.monitor

/**
 * Created by luislazaro on 9/9/15.
 * lalazaro@keedio.com
 * Keedio
 */

/**
 * Mix-in this trait to become a listener, implementing
 * what to do when an event ins received.
 */
trait StateListener {
    def statusReceived(stateEvent: StateEvent): Unit
}
