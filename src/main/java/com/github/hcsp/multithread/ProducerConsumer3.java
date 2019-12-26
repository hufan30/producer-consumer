package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
        public static void main(String[] args) throws InterruptedException {

            BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(1);
            BlockingQueue<Integer> singleQueue = new LinkedBlockingDeque<>(1);

            Producer producer = new Producer(queue, singleQueue);
            Consumer consumer = new Consumer(queue, singleQueue);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {

        private BlockingQueue<Integer> queue;
        private BlockingQueue<Integer> singleQueue;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> singleQueue) {
            this.queue = queue;
            this.singleQueue = singleQueue;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    singleQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        private BlockingQueue<Integer> queue;
        private BlockingQueue<Integer> singleQueue;


        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> singleQueue) {
            this.queue = queue;
            this.singleQueue = singleQueue;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                Integer r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    queue.put(r);
                    singleQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
