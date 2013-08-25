package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;

import java.io.IOException;
import java.io.StringWriter;

public class ModelSerializer {
    public String serializeModelToString(Model model) {
        try {
            DefaultModelWriter modelWriter = new DefaultModelWriter();
            StringWriter writer = new StringWriter();
            modelWriter.write(writer, null, model);
            return writer.toString();
        } catch (IOException e) {
            //should never occur because we are writing into a String writer
            throw new RuntimeException(e);
        }
    }
}