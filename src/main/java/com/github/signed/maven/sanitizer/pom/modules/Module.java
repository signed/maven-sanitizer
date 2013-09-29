package com.github.signed.maven.sanitizer.pom.modules;

public class Module {
    public final String name;

    public Module(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof Module)){
            return false;
        }
        Module that = (Module)obj;
        return this.name.equals(that.name);
    }

    @Override
    public String toString() {
        return "Module: "+name;
    }
}
