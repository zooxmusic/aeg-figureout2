package com.aeg.model

/**
 * Created by bszucs on 4/6/2016.
 */
class SftpMappingDecorator extends Composite {

    def composite;
    def mappings;

    SftpMappingDecorator(composite) {
        this.composite = composite;
        this.mappings = new HashMap<LocalDir, RemoteDir>();
    }

    def addMapping(local, remote) {
        mappings.put(local, remote)
    }


}
