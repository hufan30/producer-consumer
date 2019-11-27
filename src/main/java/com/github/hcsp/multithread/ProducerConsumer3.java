package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer3 {
    private static Lock lock = new ReentrantLock();
    private static Condition quefull = lock.newCondition();
    private static Condition queempty = lock.newCondition();
    private static LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(1);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Producer producer = new Producer();
            Consumer consumer = new Consumer();

            producer.start();
            consumer.start();

            producer.join();
            consumer.join();
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            lock.lock();

            while (!linkedBlockingQueue.isEmpty()) {
                try {
                    queempty.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int res = new Random().nextInt();

            linkedBlockingQueue.add(res);

            System.out.println("Producing " + res);

            quefull.signal();

            lock.unlock();

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            lock.lock();
            while (linkedBlockingQueue.isEmpty()) {

                try {
                    quefull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Integer integer = linkedBlockingQueue.remove();
            System.out.println("Consuming " + integer);

            queempty.signal();

            lock.unlock();


        }
    }
}
