package com.aeg.model


interface Directory {
    public void setParent(Directory parent);
    public void addChild(Directory child);
    public void addMapping(LocalDir localDir, RemoteDir remoteDir);
    public Directory getParent();
    public String getAbsolutePath();
    public String getName();
    public Set<Directory> getChildren();
    public Map<LocalDir, RemoteDir> getMappings();
    public boolean isRoot();
    public boolean isRemote();
}