package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 第三种实现：Semaphore信号量
 */
public class ProducerConsumer3 {
    private static Semaphore emptySlot = new Semaphore(1);
    private static Semaphore fullSlot = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
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
                    synchronized (ProducerConsumer3.class) {
                        int value = new Random().nextInt();
                        System.out.println("Producing " + value);
                        queue.add(value);
                    }
                    fullSlot.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    synchronized (ProducerConsumer3.class) {
                        System.out.println("Producing " + queue.remove());
                    }
                    emptySlot.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
