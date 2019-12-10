package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {

        Container container = new Container();
        Object lock = new Object();

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);
        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private Container container;
        private Object lock;

        Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.getValues().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer randomNum = new Random().nextInt();
                    System.out.println("Producing " + randomNum);
                    container.setValues(Optional.of(randomNum));
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Container container;
        private Object lock;

        Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getValues().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.getValues().get());
                    container.setValues(Optional.empty());
                    lock.notify();
                }
            }
        }
    }

    private static class Container {
        private Optional<Integer> values = Optional.empty();

        public Optional<Integer> getValues() {
            return values;
        }

        public void setValues(Optional<Integer> values) {
            this.values = values;
        }
    }
}
