package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.github.hcsp.multithread.ProducerConsumer1.producerCount;

public class ProducerConsumer3 {
    public static BlockingQueue<Integer> message = new LinkedBlockingQueue<>(1);

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
                    int consumerMessage = new Random().nextInt();
                    System.out.println("Producing " + consumerMessage);
                    message.put(consumerMessage);
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
                    System.out.println("Consuming " + message.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
