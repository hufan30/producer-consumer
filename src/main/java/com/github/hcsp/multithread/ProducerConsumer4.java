package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Data data = new Data();
        SignalData signalData = new SignalData();
        Producer producer = new Producer(data, signalData);
        Consumer consumer = new Consumer(data, signalData);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static ReentrantLock lock = new ReentrantLock();

    public static class Producer extends Thread {
        private Data data;
        private SignalData signalData;

        public Producer(Data data, SignalData signalData) {
            this.data = data;
            this.signalData = signalData;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {

                    int value = new Random().nextInt();
                    data.put(value);
                    System.out.println("Producing " + value);
                    signalData.take();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static class Consumer extends Thread {
        private Data data;
        private SignalData signalData;

        public Consumer(Data data, SignalData signalData) {
            this.data = data;
            this.signalData = signalData;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int value = data.take();
                    System.out.println("Consuming " + value);
                    signalData.put(-1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class SignalData {
        private Integer value;
        Semaphore isFull = new Semaphore(1);
        Semaphore isEmpty = new Semaphore(0);

        private void put(int value) throws InterruptedException {
            isFull.acquire(); //-1 会一直阻塞
            this.value = value;
            isEmpty.release(1);
        }

        private Integer take() throws InterruptedException {
            isEmpty.acquire();
            int val = this.value;
            this.value = null;
            isFull.release(1);
            return val;
        }
    }

    private static class Data {
        private Integer value;
        Semaphore isFull = new Semaphore(1);
        Semaphore isEmpty = new Semaphore(0);

        private void put(int value) throws InterruptedException {
            isFull.acquire(); //-1 会一直阻塞
            this.value = value;
            isEmpty.release(1);
        }

        private Integer take() throws InterruptedException {
            isEmpty.acquire();
            int val = this.value;
            this.value = null;
            isFull.release(1);
            return val;
        }
    }
}
