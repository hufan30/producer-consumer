package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container2 {
    static Condition producer;
    static Condition consumer;
    static Optional<Integer> optional = Optional.empty();

    public Container2(ReentrantLock lock) {
        producer = lock.newCondition();
        consumer = lock.newCondition();
    }


    public static Optional<Integer> getOptional() {
        return optional;
    }

    public static void setOptional(Optional<Integer> optionalSet) {
        optional = optionalSet;
    }

    public static Condition getProducer() {
        return producer;
    }

    public static Condition getConsumer() {
        return consumer;
    }

}
