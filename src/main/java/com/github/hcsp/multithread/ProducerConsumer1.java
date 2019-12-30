package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer1 {
    public static List<Integer> messages = new ArrayList<>(1);
    public static final Integer producerCount = 10;

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

            for (int i = 0; i < producerCount; i++) {
                synchronized (ProducerConsumer1.class) {
                    while (messages.size() != 0) {
                        try {
                            ProducerConsumer1.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int consumerMessage = new Random().nextInt();
                    System.out.println("Producing " + consumerMessage);
                    ;
                    messages.add(consumerMessage);
                    ProducerConsumer1.class.notifyAll();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < producerCount; i++) {
                synchronized (ProducerConsumer1.class) {
                    while (messages.size() == 0) {
                        try {
                            ProducerConsumer1.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + messages.remove(0));
                    ;
                    ProducerConsumer1.class.notifyAll();
                }
            }
        }
    }
}
