package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

// Exchanger
public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();
        Producer producer = new Producer(exchanger);
        Consumer consumer = new Consumer(exchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Exchanger<Integer> exchanger;

        public Producer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    int randomValue = new Random().nextInt();
                    System.out.println("Producing " + randomValue);
                    exchanger.exchange(randomValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Exchanger<Integer> exchanger;

        public Consumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    System.out.println("Consuming " + exchanger.exchange(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
