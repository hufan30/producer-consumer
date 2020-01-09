package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumer2 {
    static ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(1);

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
            for (int i = 0; i < 10; i++) {
                int rand = new Random().nextInt();
                try {
                    blockingQueue.put(rand);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producing " + rand);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + blockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
