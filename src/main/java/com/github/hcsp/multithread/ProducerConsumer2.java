package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    static Lock lock = new ReentrantLock();
    static Condition queueFull = lock.newCondition();
    static Condition queueEmpty = lock.newCondition();

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
                lock.lock();
                try {
                    while (!queue.isEmpty()) {
                        queueEmpty.await();
                    }
                    int random = new Random().nextInt();
                    queue.add(random);
                    System.out.println("Producing " + random);
                    queueFull.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
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
                lock.lock();
                try {
                    while (queue.isEmpty()) {
                        queueFull.await();
                    }
                    System.out.println("Consuming " + queue.remove());
                    queueEmpty.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        }
    }
}
