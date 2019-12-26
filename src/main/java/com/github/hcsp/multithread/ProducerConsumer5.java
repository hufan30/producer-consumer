package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
        public static void main(String[] args) throws InterruptedException {
            Exchanger<Integer> exgr = new Exchanger<>();
            Exchanger<String> exgr1 = new Exchanger<>();

            Producer producer = new Producer(exgr, exgr1);
            Consumer consumer = new Consumer(exgr, exgr1);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {
        Exchanger<String> exgr1;
        Exchanger<Integer> exgr;

        public Producer(Exchanger<Integer> exgr, Exchanger<String> exgr1) {
            this.exgr=exgr;
            this.exgr1=exgr1;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                        exgr1.exchange("a");
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                        exgr.exchange(r);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<String> exgr1;
        Exchanger<Integer> exgr;

        public Consumer(Exchanger<Integer> exgr, Exchanger<String> exgr1) {
            this.exgr = exgr;
            this.exgr1=exgr1;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    exgr1.exchange("b");
                            Integer value =exgr.exchange(0);
                            System.out.println("Consuming " + value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

            }

    }
}
