package com.aeg.model

/**
 * Created by bszucs on 4/6/2016.
 */
class Composite extends Component {
    private children = []

    Composite(){}

    Composite(String name) {
        super(name)
    }
    Composite(String name, String path) {
        super(name, path)
    }

    Composite(String name, Composite parent) {
        this(name, null, parent)
    }
    Composite(String name, String path, Composite parent) {
        super(name, path, parent)
        parent.children.add(this);
    }

    def toString(indent) {
        def s = super.toString(indent)
        children.each { child ->
            s += "\r\n" + child.toString(indent + 1);
        }
        s

    }


    /*def leftShift(component) {
        children << component
        component.parent = this;
    }*/
}
