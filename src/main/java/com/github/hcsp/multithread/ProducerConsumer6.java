package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer6 {
//    static ReentrantLock reentrantLock = new ReentrantLock();
//    static Condition condition = reentrantLock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        Queue<Integer> que = new LinkedList();
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        Producer producer = new Producer(que,reentrantLock,condition);
        Consumer consumer = new Consumer(que,reentrantLock,condition);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        Queue que;
        ReentrantLock reentrantLock;
        Condition condition;

        public Producer(Queue que,ReentrantLock reentrantLock,Condition condition) {
            this.reentrantLock = reentrantLock;
            this.condition = condition;
            this.que = que;
        }

        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                try {
                    reentrantLock.lock();
                    while (que.size() > 0) {
                        condition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    int num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    que.add(num);
                    condition.signal();
                    reentrantLock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Queue que;
        ReentrantLock reentrantLock;
        Condition condition;

        public Consumer(Queue que,ReentrantLock reentrantLock,Condition condition) {
            this.reentrantLock = reentrantLock;
            this.condition = condition;
            this.que = que;
        }

        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                try {
                    reentrantLock.lock();
                    while (que.isEmpty()) {
                        condition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Integer poll = (Integer) que.poll();
                    System.out.println("Consuming " + poll);
                    condition.signal();
                    reentrantLock.unlock();
                }
            }
        }
    }
}
