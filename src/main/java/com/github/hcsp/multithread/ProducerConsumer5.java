package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<Integer>();
        Exchanger<Integer> signalExchanger = new Exchanger<Integer>();
        Producer producer = new Producer(exchanger, signalExchanger);
        Consumer consumer = new Consumer(exchanger, signalExchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private Exchanger<Integer> exchanger;
        private Exchanger<Integer> signalExchanger;

        public Producer(Exchanger<Integer> exchanger, Exchanger<Integer> signalExchanger) {
            this.exchanger = exchanger;
            this.signalExchanger = signalExchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {

                    Integer value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    exchanger.exchange(value);
                    signalExchanger.exchange(null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Exchanger<Integer> exchanger;
        private Exchanger<Integer> signalExchanger;

        public Consumer(Exchanger<Integer> exchanger, Exchanger<Integer> signalExchanger) {
            this.exchanger = exchanger;
            this.signalExchanger = signalExchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int value = exchanger.exchange(null);
                    System.out.println("Consuming " + value);

                    signalExchanger.exchange(value);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
    }
}
