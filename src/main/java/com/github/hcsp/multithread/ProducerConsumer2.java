package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container2 container = new Container2(lock);

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container2 container;
        ReentrantLock lock;

        public Producer(Container2 container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    try {
                        while (container.getValue().isPresent()) {
                            container.getNotProducedYet().await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int r = new Random().nextInt();
                    container.setValue(Optional.of(r));
                    System.out.println("Producing " + r);
                    container.getNotConsumedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container2 container;
        ReentrantLock lock;

        Consumer(Container2 container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    try {
                        while (!container.getValue().isPresent()) {
                            container.getNotConsumedYet().await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int r = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + r);
                    container.getNotProducedYet().signal();
                } finally {
                    lock.unlock();
                }
            }

        }
    }

    static class Container2 {
        private Condition notConsumedYet;
        private Condition notProducedYet;
        private Optional<Integer> value = Optional.empty();

        Container2(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
