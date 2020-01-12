package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Lock/Condition
public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Producer producer = new Producer(list, lock, condition);
        Consumer consumer = new Consumer(list, lock, condition);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    private static class Producer extends Thread {
        final List<Integer> list;
        final Lock lock;
        final Condition condition;

        Producer(List<Integer> list, Lock lock, Condition condition) {
            this.list = list;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                lock.lock();
                try {
                    while (!list.isEmpty()) {
                        condition.await();
                    }
                    int randomValue = new Random().nextInt();
                    System.out.println("Producing " + randomValue);
                    list.add(randomValue);
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class Consumer extends Thread {
        final List<Integer> list;
        final Lock lock;
        final Condition condition;

        Consumer(List<Integer> list, Lock lock, Condition condition) {
            this.list = list;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (list.isEmpty()) {
                        condition.await();
                    }
                    System.out.println("Consuming " + list.get(0));
                    list.remove(0);
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
