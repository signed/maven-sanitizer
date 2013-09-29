package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.pom.Action;

import java.util.Iterator;

public class DropModule implements Action<Module> {
    private Iterable<Module> elements;

    @Override
    public void performOn(Iterable<Module> elements) {
        this.elements = elements;
    }

    @Override
    public void perform(Module element) {
        Iterator<Module> elementIterator = elements.iterator();
        while (elementIterator.hasNext()) {
            Module current = elementIterator.next();
            if (!element.equals(current)) {
                continue;
            }

            elementIterator.remove();
        }
    }
}
