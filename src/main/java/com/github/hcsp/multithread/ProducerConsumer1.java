package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Container container = new Container();
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        Object obj;

        public Producer(Container container, Object obj) {
            this.container = container;
            this.obj = obj;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (obj) {
                    while (container.getProduct().isPresent()) {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setProduct(Optional.of(r));
                    obj.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Object obj;

        public Consumer(Container container, Object obj) {
            this.container = container;
            this.obj = obj;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (obj) {
                    while (!container.getProduct().isPresent()) {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer integer = container.getProduct().get();
                    System.out.println("Consuming " + integer);
                    container.setProduct(Optional.empty());
                    obj.notify();
                }
            }
        }
    }
}
