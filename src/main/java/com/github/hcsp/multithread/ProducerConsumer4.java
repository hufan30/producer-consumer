package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import static com.github.hcsp.multithread.ProducerConsumer1.producerCount;

public class ProducerConsumer4 {
    public static List<Integer> messages = new ArrayList<>(1);
    public static Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < producerCount; i++) {
                    semaphore.acquire();
                    while (messages.size() != 0) {
                        semaphore.release();
                    }
                    int consumerMessage = new Random().nextInt();
                    System.out.println("Producing " + consumerMessage);
                    messages.add(consumerMessage);
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < producerCount; i++) {
                    semaphore.acquire();
                    while (messages.size() == 0) {
                        semaphore.release();
                    }
                    System.out.println("Consuming " + messages.remove(0));
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
