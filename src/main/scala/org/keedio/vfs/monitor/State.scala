package org.keedio.vfs.monitor

/**
 * Created by luislazaro on 10/9/15.
 * lalazaro@keedio.com
 * Keedio
 */

/**
 * Available file's states.
 */
class State(name: String) {
    override def toString(): String = {
        name
    }
}

object State extends Serializable {
    final val ENTRY_CREATE: State = new State("entry_create")
    final val ENTRY_DELETE: State = new State("entry_delete")
    final val ENTRY_MODIFY: State = new State("entry_modify")
}

