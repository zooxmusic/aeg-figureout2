package com.aeg.model

import java.nio.file.Path


abstract class AbstractDirectory implements Directory {
    private Directory parent = null;
    private String absolutePath;
    private String name;
    private Set<Directory> children;
    private Map<LocalDir, RemoteDir> mappings;

    public AbstractDirectory(Directory parent, Path path) {
        this(parent, path, new HashSet<AbstractDirectory>(), new HashMap<LocalDir, RemoteDir>());
    }


    public AbstractDirectory(Directory parent, Path path, Set<Directory> children, Map<LocalDir, RemoteDir> mappings) {
        this.parent = parent;
        this.absolutePath = path.toAbsolutePath();
        this.name = path.fileName;
        this.children = children;
        this.mappings = mappings;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public void addChild(Directory child) {
        child.setParent(this);
        children.add(child);
    }

    public void addMapping(LocalDir localDir, RemoteDir remoteDir) {
        mappings.add(localDir, remoteDir);
    }

    @Override
    Directory getParent() {
        return this.parent;
    }

    @Override
    String getAbsolutePath() {
        return this.absolutePath
    }

    @Override
    String getName() {
        return this.name;
    }

    @Override
    Set<Directory> getChildren() {
        return this.children;
    }

    @Override
    Map<LocalDir, RemoteDir> getMappings() {
        return mappings;
    }

    public boolean isRemote() {
        return false;
    }

    @Override
    boolean isRoot() {
        return null == getParent();
    }
}
