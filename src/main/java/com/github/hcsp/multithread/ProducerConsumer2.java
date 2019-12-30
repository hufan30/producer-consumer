package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.hcsp.multithread.ProducerConsumer1.producerCount;

public class ProducerConsumer2 {
    public static List<Integer> messages = new ArrayList<>(1);
    public static Lock lock = new ReentrantLock();
    public static Condition consumerCondition = lock.newCondition();
    public static Condition producerCondition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < producerCount; i++) {
                lock.lock();
                try {
                    while (messages.size() != 0) {
                        try {
                            consumerCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int consumerMessage = new Random().nextInt();
                    System.out.println("Producing " + consumerMessage);
                    messages.add(consumerMessage);
                    producerCondition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < producerCount; i++) {
                lock.lock();
                try {
                    while (messages.size() == 0) {
                        try {
                            producerCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + messages.remove(0));
                    consumerCondition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
