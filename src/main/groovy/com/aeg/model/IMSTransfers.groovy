package com.aeg.model
/**
 * Created by bszucs on 4/6/2016.
 */
class IMSTransfers extends LocalDir {
    private static final String NAME = "IMSTransferFiles";
    private def AbstractDirectory root;

    public static IMSTransfers create() {
        String aegHome = System.env['AEG_HOME'];
        if(null == aegHome || "".equalsIgnoreCase(aegHome)) {
            aegHome = "C:/AEG";
        }
        return new IMSTransfers(LocalDir.create(aegHome), NAME)
    }

    private IMSTransfers(LocalDir aegHome, String name) {
        super(aegHome, name)
    }
}
