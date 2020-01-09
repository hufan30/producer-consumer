package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer5 {

    static Semaphore semProducer = new Semaphore(1);
    static Semaphore semConsumer = new Semaphore(0);
    static Optional<Integer> container = Optional.empty();

    public static void main(String[] args) throws InterruptedException {
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
                int rand = new Random().nextInt();
                try {
                    semProducer.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producing " + rand);
                container = Optional.of(rand);
                semConsumer.release();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semConsumer.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consuming " + container.get());
                container = Optional.empty();
                semProducer.release();
            }
        }
    }
}
