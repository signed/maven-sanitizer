package com.github.signed.maven.sanitizer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class MavenMatchers {
    public static<T> Matcher<T> anything(){
        return new BaseMatcher<T>() {
            @Override
            public boolean matches(Object item) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void describeTo(Description description) {
                throw new RuntimeException("should never be called");
            }
        };
    }
}