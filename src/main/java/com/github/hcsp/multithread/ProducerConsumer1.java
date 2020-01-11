package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {

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
                    while (Container1.getOptional().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int randomNum = new Random().nextInt();
                    Container1.setOptional(Optional.of(randomNum));
                    System.out.println("Producing " + randomNum);
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
                    while (!Container1.getOptional().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer getRandomNum = Container1.getOptional().get();
                    System.out.println("Consuming " + getRandomNum);
                    Container1.setOptional(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}

