package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    private static Semaphore semaphoreempty=new Semaphore(1);
    private static Semaphore semaphorefull=new Semaphore(0);
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
            try {
                semaphoreempty.acquire();
                int res = new Random().nextInt();
                linkedBlockingQueue.add(res);
                semaphorefull.release(1);
                System.out.println("Producing " + res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphoreempty.release();

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                semaphorefull.acquire();
                Integer integer = linkedBlockingQueue.remove();
                System.out.println("Consuming " + integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphoreempty.release(1);
            semaphorefull.release();

        }
    }
}
