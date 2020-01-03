package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        LinkedBlockingQueue<Integer> signalQueue = new LinkedBlockingQueue<>(1);


        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalQueue;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    queue.put(r);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class Consumer extends Thread {
        BlockingQueue queue;
        BlockingQueue signalQueue;

        Consumer(BlockingQueue queue, BlockingQueue signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
