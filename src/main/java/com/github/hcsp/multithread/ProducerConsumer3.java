package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue(1);
        BlockingQueue<Integer> signalQueue = new LinkedBlockingQueue(1);
        Producer producer = new Producer(blockingQueue, signalQueue);
        Consumer consumer = new Consumer(blockingQueue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static ReentrantLock lock = new ReentrantLock();

    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalQueue;
        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue){
            this.queue = queue;
            this.signalQueue = signalQueue;
        }
        @Override
        public void run() {
            for (int i=0; i<10; i++){
                try {
                    int value = new Random().nextInt();
                    queue.put( value );
                    System.out.println("Producing "+value);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static class Consumer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalQueue;
        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue){
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i=0; i<10; i++){
                try {
                    System.out.println("Consuming "+queue.take());
                    signalQueue.put(-1);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
