package com.github.signed.maven.model;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;

import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class PluginConfigurationBuilder {
    private final List<String> elements = new ArrayList<>();

    public PluginConfigurationBuilder addElement(String element, Path path){
        return addElement(element, path.toString());
    }

    public PluginConfigurationBuilder addElement(String element, String value) {
        StringBuilder builder = new StringBuilder();
        builder.append(format("<%s>", element));
        builder.append(value);
        builder.append(format("</%s>", element));
        elements.add(builder.toString());
        return this;
    }


    public Xpp3Dom toConfiguration() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<configuration>");
        for (String element : elements) {
            builder.append(element).append("\n");
        }
        builder.append("</configuration>");

        try {
            return Xpp3DomBuilder.build(new StringReader(builder.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
