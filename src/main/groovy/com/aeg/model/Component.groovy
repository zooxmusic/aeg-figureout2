package com.aeg.model

/**
 * Created by bszucs on 4/6/2016.
 */
abstract class Component {
    String path = "";
    String name = "";
    Component parent = null;

    Component(){}
    public Component(String name) {
        this(name, null, null);
    }
    public Component(String name, String path) {
        this(name, path, null);
    }

    public Component(String name, Component parent) {
        this(name, null, parent);
    }

    public Component(String name, String path, Component parent) {
        this.name = name;
        this.path = path;
        this.parent = parent;
    }

    def toString(indent) {
        ("-" * indent) + (name + buildPath())
    }

    public String buildPath() {
        if(null == parent) {
            return String.format(" %s/", this.path);
        }
        return String.format(" %s%s/", parent.buildPath(), this.name);
    }
}