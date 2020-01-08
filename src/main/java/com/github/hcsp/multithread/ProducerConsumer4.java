package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore isEmpty = new Semaphore(1);
        Semaphore isFull = new Semaphore(0);
        Semaphore mutex = new Semaphore(1);
        Container container = new Container();
        Producer producer = new Producer(isEmpty, isFull, mutex, container);
        Consumer consumer = new Consumer(isEmpty, isFull, mutex, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Semaphore isEmpty;
        Semaphore isFull;
        Semaphore mutex;
        Container container;

        Producer(Semaphore isEmpty, Semaphore isFull, Semaphore mutex, Container container) {
            this.isEmpty = isEmpty;
            this.isFull = isFull;
            this.mutex = mutex;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    isEmpty.acquire();
                    mutex.acquire();
                    int r = new Random().nextInt();
                    container.setValue(Optional.of(r));
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    isFull.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Semaphore isEmpty;
        Semaphore isFull;
        Semaphore mutex;
        Container container;

        Consumer(Semaphore isEmpty, Semaphore isFull, Semaphore mutex, Container container) {
            this.isEmpty = isEmpty;
            this.isFull = isFull;
            this.mutex = mutex;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    isFull.acquire();
                    mutex.acquire();
                    int value = container.getValue().get();
                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    isEmpty.release();
                }
            }
        }
    }
    public static class Container {
        private Optional<Integer> value = Optional.empty();

        Optional<Integer> getValue() {
            return value;
        }

        void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}

