# VFS2-Monitor

## Main goal
**Keedio's VFS2-Monitor** is a feature for monitoring changes in directories of supported file systems. When a change occurs in the observed directory a event is fired. All listeners subscribed to the event generator will be notified.

## Description
VFS2-Monitor is built from Apache-Commons-vfs. 
A single action such as create, delete or modify will trigger an event.

## Supported File Systems
Although Commons VFS directly supports the following file systems:

[Supported File Systems](https://commons.apache.org/proper/commons-vfs/filesystems.html)
 
***actually Keedio's VFS2-Monitor has only been tested in the following one***: 

* **File**: `file:///home/someuser/somedir`
* **FTP**:  `ftp://myusername:mypassword@somehost/somedir`
* **HDFS**: `hdfs://somehost:8020/somedir`

## How To Use
Normally a object interested in events (listener) from a directory should instance WatchablePath with:

* String path to be watched.(example:  hdfs://somehost:someport/somedir)
* Start time in seconds.
* Refresh time in seconds.
* Regexp for matching files.

The listener will have to register himself to the watchable object and will implement what to do, when something happens in the monitored path.

The path will be checked periodically for new files or changes in its modification times.
If the path contains subpaths, changes under subdirectories will all be watched.

* * *
