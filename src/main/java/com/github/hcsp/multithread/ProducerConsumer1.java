package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {

        Queue<Integer> que = new LinkedList();

        Producer producer = new Producer(que);
        Consumer consumer = new Consumer(que);


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        Queue que;

        public Producer(Queue que) {
            this.que = que;
        }

        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                synchronized (que) {
                    try {
                        while (que.size() > 0) {
                            que.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    que.add(num);
                    que.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Queue que;

        public Consumer(Queue que) {
            this.que = que;
        }

        @Override
        public void run() {
            for (int i = 0; i <= 10; i++) {
                synchronized (que) {
                    try {
                        while (que.isEmpty()) {
                            que.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Integer poll = (Integer) que.poll();
                    System.out.println("Consuming " + poll);
                    que.notify();
                }
            }
        }
    }

}

