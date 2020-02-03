package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    static BlockingDeque que = new LinkedBlockingDeque(1);

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
            for (int i = 0; i <= 10; i++) {
                int num = new Random().nextInt();
                System.out.println("Producing " + num);
                try {
                    que.put(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                Integer poll = null;
                try {
                    poll = (Integer) que.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consuming " + poll);
            }
        }
    }
}
