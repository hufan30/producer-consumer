package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {

    static Optional<Integer> container = Optional.empty();

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Producer producer = new Producer(lock);
        Consumer consumer = new Consumer(lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Object lock;

        public Producer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int rand = new Random().nextInt();
                    System.out.println("Producing " + rand);
                    container = Optional.of(rand);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        Object lock;

        public Consumer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.get());
                    container = Optional.empty();
                    lock.notify();
                }
            }
        }
    }


}
