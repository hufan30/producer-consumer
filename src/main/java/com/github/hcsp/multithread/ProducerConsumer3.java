package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer3 {
    static Lock lock = new ReentrantLock();

    static Semaphore emptySlot = new Semaphore(1);
    static Semaphore fullSlot = new Semaphore(0);
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();
        Producer producer = new Producer(queue);
        producer.start();
        Consumer consumer = new Consumer(queue);
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        Queue<Integer> queue;

        public Producer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    emptySlot.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (ProducerConsumer3.class) {
                    int random = new Random().nextInt();
                    queue.add(random);
                    System.out.println("Producing " + random);
                }
                fullSlot.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Queue<Integer> queue;

        public Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    fullSlot.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (ProducerConsumer3.class) {
                    System.out.println("Consuming " + queue.remove());
                }
                emptySlot.release();
            }
        }
    }
}
