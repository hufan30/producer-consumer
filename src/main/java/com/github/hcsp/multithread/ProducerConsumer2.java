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
        private Container container;
        private ReentrantLock lock;

        Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getValues().isPresent()) {
                        try {
                            container.getConsumed().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer randomNum = new Random().nextInt();
                    System.out.println("Producing " + randomNum);
                    container.setValues(Optional.of(randomNum));
                    container.getProduced().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Container container;
        private ReentrantLock lock;

        Consumer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getValues().isPresent()) {
                        try {
                            container.getProduced().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.getValues().get());
                    container.setValues(Optional.empty());
                    container.getConsumed().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class Container {

        private Optional<Integer> values = Optional.empty();
        private Condition produced; // 消费线程
        private Condition consumed; // 生产线程

        Container(ReentrantLock lock) {
            produced = lock.newCondition();
            consumed = lock.newCondition();
        }

        public Condition getProduced() {
            return produced;
        }

        public Condition getConsumed() {
            return consumed;
        }

        public Optional<Integer> getValues() {
            return values;
        }

        public void setValues(Optional<Integer> values) {
            this.values = values;
        }
    }
}
