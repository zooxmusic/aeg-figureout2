package com.aeg.model
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
class DirectoryList {

    public void list() {
        Path dir = Paths.get('resources') //<1>
        BasicFileAttributes attrs = Files.readAttributes(dir, BasicFileAttributes) //<2>

        println """
AbstractDirectory name: ${dir.fileName}
Absolute path: ${dir.toAbsolutePath()}

The file exists: ${Files.exists(dir)}
The file is readable: ${Files.isReadable(dir)}
The file is writable: ${Files.isWritable(dir)}
The file is executable: ${Files.isExecutable(dir)}
The file is a directory: ${Files.isDirectory(dir)}

Created: ${attrs.creationTime()}
Last modified: ${attrs.lastModifiedTime()}
Last accessed: ${attrs.lastAccessTime()}

Children:"""

//<3>
        dir.eachFile {
            println "  - ${it.fileName}"
        }
    }
}
