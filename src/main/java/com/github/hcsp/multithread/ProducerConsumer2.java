package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
        public static void main(String[] args) throws InterruptedException {
            ReentrantLock lock = new ReentrantLock();
            Container2 container2 = new Container2(lock);

            Producer producer = new Producer(lock, container2);
            Consumer consumer = new Consumer(lock, container2);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {
        ReentrantLock lock;
        Container2 container2;

        public Producer(ReentrantLock lock, Container2 container2) {
            this.lock = lock;
            this.container2 = container2;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (Container2.getOptional().isPresent()) {
                        try {
                            Container2.getProducer().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int randomNum = new Random().nextInt();
                    Container2.setOptional(Optional.of(randomNum));
                    System.out.println("Producing " + randomNum);
                    Container2.getConsumer().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        ReentrantLock lock;
        Container2 container2;

        public Consumer(ReentrantLock lock, Container2 container2) {
            this.lock = lock;
            this.container2 = container2;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!Container2.getOptional().isPresent()) {
                        try {
                            Container2.getConsumer().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int getRandomNum = Container2.getOptional().get();
                    System.out.println("Consuming " + getRandomNum);
                    Container2.setOptional(Optional.empty());
                    Container2.getProducer().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
