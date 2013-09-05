package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Model;
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

   public static Matcher<Model> projectWith(final String groupId, final String artifactId){
       return new TypeSafeMatcher<Model>() {
           @Override
           protected boolean matchesSafely(Model item) {
               return groupId.equals(item.getGroupId()) && artifactId.equals(item.getArtifactId());
           }

           @Override
           public void describeTo(Description description) {
               description.appendText("a project model with groupId").appendValue(groupId).appendText("and artifactId").appendValue(artifactId);
           }
       };
   }
}
