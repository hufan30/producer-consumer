package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    private static Exchanger<Integer> exchanger = new Exchanger();

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
                int res = new Random().nextInt();
                exchanger.exchange(res);
                System.out.println("Producing " + res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                Integer integer = exchanger.exchange(null);
                System.out.println("Consuming " + integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
