package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

import static com.github.hcsp.multithread.ProducerConsumer1.producerCount;

public class ProducerConsumer5 {
    public static Exchanger<Integer> message = new Exchanger<>();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < producerCount; i++) {
                    int consumerMessage = new Random().nextInt();
                    System.out.println("Producing " + consumerMessage);
                    message.exchange(consumerMessage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < producerCount; i++) {
                    System.out.println("Consuming " + message.exchange(null));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
