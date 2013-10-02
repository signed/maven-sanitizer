package com.github.signed.maven.sanitizer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {
    public static <T> Matcher<T> always(){
        return new TypeSafeMatcher<T>() {
            @Override
            protected boolean matchesSafely(T item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("just true");
            }
        };
    }
}
