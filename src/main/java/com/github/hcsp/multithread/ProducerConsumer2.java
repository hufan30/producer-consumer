package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        ReentrantLock lock;

        Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));
                    container.getNotProducedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        ReentrantLock lock;

        Consumer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getValue().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);
                    container.getNotConsumedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Condition notConsumedYet;
        private Condition notProducedYet;

        private Optional<Integer> value = Optional.empty();

        Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        Optional<Integer> getValue() {
            return value;
        }

        void setValue(Optional<Integer> value) {
            this.value = value;
        }

        Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        Condition getNotProducedYet() {
            return notProducedYet;
        }
    }
}
