package com.aeg.model

import lombok.Getter

import java.nio.file.Path
import java.nio.file.Paths

@Getter
class LocalDir extends AbstractDirectory {

    private LocalDir(Path path) {
        super(null, path);
    }
    private LocalDir(String name) {
        super(null, Paths.get(String.format("%s", name)))
    }

    private LocalDir(LocalDir parent, String name) {
        super(parent, Paths.get(String.format("%s/%s", parent.getAbsolutePath(), name)))
    }


    private LocalDir(LocalDir parent, Path path) {
        super(parent, path)
    }
    public static LocalDir create(String name) {
        return new LocalDir(name);
    }
    public static LocalDir create(LocalDir parent, String name) {
        return new LocalDir(parent, Paths.get(String.format("%s/%s", parent.toAbsolutePath(), name)));
    }
}
