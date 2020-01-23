package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//采用传统Object类的wait()/notify()
public class ProducerConsumer2 {
        private static final Object lock = new Object();

        public static void main(String[] args) throws InterruptedException {
            List<Integer> capacity = new ArrayList<>();

            Producer producer = new Producer(capacity);
            Consumer consumer = new Consumer(capacity);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {

        List<Integer> capacity;

        public Producer(List<Integer> capacity) {
            this.capacity = capacity;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!capacity.isEmpty()){ //若容器有数据则等待
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("error");
                            e.printStackTrace();
                        }
                    }
                    int value = new Random().nextInt();
                    System.out.println("Producing "+ value);
                    capacity.add(value);
                    lock.notify();  //唤醒消费者线程
                }
            }
        }
    }

    public static class Consumer extends Thread {

        List<Integer> capacity;

        public Consumer(List<Integer> capacity) {
            this.capacity = capacity;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (capacity.isEmpty()){ //若容器无数据
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("error");
                            e.printStackTrace();
                        }
                    }
                    int value = new Random().nextInt();
                    System.out.println("Producing "+ capacity.get(0));
                    capacity.clear();
                    lock.notify();
                }
            }
        }
    }
}
