package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

//BlockingQueue
public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> signalQueue = new LinkedBlockingQueue<>();
        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static class Producer extends Thread {
        private final BlockingQueue<Integer> queue;
        private final BlockingQueue<Integer> signalQueue;

        Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            IntStream.rangeClosed(1, 10).forEach(x -> {
                try {
                    int randomValue = new Random().nextInt();
                    System.out.println("Producing " + randomValue);
                    queue.put(randomValue);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static class Consumer extends Thread {
        private final BlockingQueue<Integer> queue;
        private final BlockingQueue<Integer> signalQueue;

        Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {

            IntStream.rangeClosed(1, 10).forEach(x -> {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
