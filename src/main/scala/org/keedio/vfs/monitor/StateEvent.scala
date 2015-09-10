package org.keedio.vfs.monitor

import org.apache.commons.vfs2.FileChangeEvent

/**
 * Created by luislazaro on 10/9/15.
 * lalazaro@keedio.com
 * Keedio
 */


class StateEvent(fileChangeEvent: FileChangeEvent, state: State) {
    def getState = state
    def getFileChangeEvent = fileChangeEvent
}
