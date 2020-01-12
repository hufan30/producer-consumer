package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Semaphore
public class ProducerConsumer6 {
    static Semaphore emptySolt = new Semaphore(1);
    static Semaphore fullSolt = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        Lock lock = new ReentrantLock();

        Producer producer = new Producer(list, lock);
        Consumer consumer = new Consumer(list, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        final List<Integer> list;
        final Lock lock;

        Producer(List<Integer> list, Lock lock) {
            this.list = list;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    emptySolt.acquire();
                    try {
                        lock.lock();
                        int randomValue = new Random().nextInt();
                        System.out.println("Producing " + randomValue);
                        list.add(randomValue);
                    } finally {
                        lock.unlock();
                    }
                    fullSolt.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final List<Integer> list;
        final Lock lock;

        Consumer(List<Integer> list, Lock lock) {
            this.list = list;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    fullSolt.acquire();
                    try {
                        lock.lock();
                        System.out.println("Consuming " + list.get(0));
                        list.remove(0);
                    } finally {
                        lock.unlock();
                    }
                    emptySolt.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
