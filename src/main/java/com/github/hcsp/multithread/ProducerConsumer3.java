package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
        public static void main(String[] args) throws InterruptedException {
            BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>();
            Producer producer = new Producer(blockingQueue);
            Consumer consumer = new Consumer(blockingQueue);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {
        private BlockingQueue<Integer> blockingQueue;
        Producer (BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer randomNum = new Random().nextInt();
                try {
                    blockingQueue.put(randomNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producing " + randomNum);
            }
        }
    }

    public static class Consumer extends Thread {
        private BlockingQueue<Integer> blockingQueue;
        Consumer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int headNum = 0;
                try {
                    headNum = blockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consuming " + headNum);
            }
        }
    }
}
