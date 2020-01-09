package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumer4 {

    static ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(1);
    static ArrayBlockingQueue<Integer> signalQueue = new ArrayBlockingQueue<>(1);

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
                System.out.println("Producing " + rand);
                try {
                    blockingQueue.put(rand);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + blockingQueue.take());
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
