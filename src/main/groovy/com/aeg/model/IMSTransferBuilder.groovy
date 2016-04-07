package com.aeg.model

import groovy.json.JsonBuilder

/**
 * Created by bszucs on 4/6/2016.
 */
class IMSTransferBuilder {

    public void execute() {
        String aegHome = System.env['AEG_HOME']
        Composite root = new Composite("AEG", aegHome)
        Composite imsTransfers = new Composite("IMSTransferFiles", root);
        //root << imsTransfers
        Composite inbound =new Composite( "Inbound",  imsTransfers);
        //imsTransfers << inbound
        Composite outbound =new Composite( "Outbound",  imsTransfers);
        //imsTransfers << outbound
        Composite cr = new Composite( "CR",  inbound);
        //inbound << cr;
        addInboundSubstructure(cr);
        println root.toString(0)





       /* def imsTransfers = IMSTransfers.create();
        LocalDir inbound = LocalDir.create(imsTransfers, "Inbound");
        LocalDir outbound = LocalDir.create(imsTransfers, "outbound");
        println new JsonBuilder( imsTransfers ).toPrettyString()*/
    }

    private void addInboundSubstructure(Composite pm) {
        new Composite("New", pm);
        new Composite("Working", pm);
        new Composite("XML_Applications", pm);
    }
}
