package com.github.signed.maven.sanitizer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class MavenMatchers {
    public static<T> Matcher<T> anything(){
        return new TypeSafeMatcher<T>() {
            @Override
            protected boolean matchesSafely(T item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                throw new RuntimeException("should never be called");
            }
        };
    }
}
