package com.github.hcsp.multithread;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    private static Optional<Integer> value;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        value = Optional.empty();

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();


        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (value.isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    value = Optional.of(random);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!value.isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + value.get());
                    value = Optional.empty();
                    lock.notify();
                }
            }
        }
    }
}
