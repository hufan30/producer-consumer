package com.github.hcsp.multithread;

import java.util.Optional;

public class Container1 {

    static Optional<Integer> optional = Optional.empty();

    public static Optional<Integer> getOptional() {
        return optional;
    }

    public static void setOptional(Optional<Integer> optionalSet) {
        optional = optionalSet;
    }
}
