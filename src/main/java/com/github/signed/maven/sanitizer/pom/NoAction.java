package com.github.signed.maven.sanitizer.pom;

public class NoAction<T> implements Action<T> {

    public static <T> Action<T> noAction() {
        return new NoAction<>();
    }
    @Override
    public void performOn(Iterable<T> elements) {
        //do nothing
    }

    @Override
    public void perform(T element) {
        //do nothing;
    }
}
