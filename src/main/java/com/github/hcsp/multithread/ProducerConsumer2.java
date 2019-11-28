package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 第二种实现： Lock/Condition
 */
public class ProducerConsumer2 {
    private static int MAX_QUEUE_SIZE = 100;
    private static Lock lock = new ReentrantLock(true); // 公平可重用锁
    private static Condition queueEmpty = lock.newCondition();
    private static Condition queueFull = lock.newCondition();

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
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (queue.size() >= 100) {
                        queueEmpty.await();
                    }
                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    queue.add(value);
                    queueFull.signalAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
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
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (queue.size() == 0) {
                        queueFull.await();
                    }
                    System.out.println("Consumint " + queue.remove());
                    queueEmpty.signalAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
