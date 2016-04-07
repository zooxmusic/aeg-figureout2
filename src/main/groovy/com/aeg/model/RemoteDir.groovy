package com.aeg.model

/**
 * Created by bszucs on 4/6/2016.
 */
class RemoteDir extends AbstractDirectory {

    RemoteDir(AbstractDirectory parent, String absolutePath, String name, Set<AbstractDirectory> children, Map<LocalDir, RemoteDir> mappings) {
        super(parent, absolutePath, name, children, mappings)
    }

    @Override
    public boolean isRemote() {
        return true;
    }
}
