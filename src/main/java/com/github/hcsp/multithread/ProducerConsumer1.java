package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    private static Integer random;

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        //分别雇佣了一个生产者和消费者工人
        Producer producer = new Producer(lock);
        Consumer consumer = new Consumer(lock);

        //开始工作
        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Object lock;

        //任何一个对象构造的时候需要先构造它的父亲


        public Producer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                //线程所有的工作都是在run里面进行的
                synchronized (lock) {
                    while (random != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    random = new Random().nextInt();
                    System.out.println("Producing" + random);
                    lock.notify();

                }
            }

        }
    }

    public static class Consumer extends Thread {
        Object lock;

        public Consumer(Object lock) {
            this.lock = lock;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (random == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming" + random);
                    random = null;
                    lock.notify();
                }
            }
        }
    }
}
