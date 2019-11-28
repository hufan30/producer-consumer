package com.github.hcsp.multithread;

import java.util.Random;

/**
 * 第一种实现：Object.wait()/notify()
 */
public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static class Container {
        Object value;
    }

    public static class Producer extends Thread {
        Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            synchronized (container) {
                for (int i = 0; i < 10; i++) {
                    while (container.value != null) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    container.value = random;
                    container.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;

        public Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            synchronized (container) {
                for (int i = 0; i < 10; i++) {
                    while (container.value == null) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    container.notify();
                }
            }
        }
    }
}
