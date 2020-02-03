package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> signal = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(queue, signal);
        Consumer consumer = new Consumer(queue, signal);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signal;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signal) {
            this.queue = queue;
            this.signal = signal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int random = new Random().nextInt();
                System.out.println("Producing " + random);
                try {
                    queue.put(random);
                    signal.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signal;

        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signal) {
            this.queue = queue;
            this.signal = signal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signal.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
