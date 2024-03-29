package com.aeg.model

import java.io.File;

public class DisplayDirectoryAndFile {

    public void display() {

        displayIt(new File("C:\\Downloads"));
    }

    public void displayIt(File node){

        System.out.println(node.getAbsoluteFile());

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                displayIt(new File(node, filename));
            }
        }

    }
}
