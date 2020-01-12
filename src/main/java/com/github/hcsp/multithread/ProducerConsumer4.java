package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// volatile
public class ProducerConsumer4 {
    static volatile Integer flag = null;

    public static void main(String[] args) throws InterruptedException {

        List<Integer> list = new ArrayList<>();
        Producer producer = new Producer(list);
        Consumer consumer = new Consumer(list);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
        flag = null;
    }

    public static class Producer extends Thread {
        final List<Integer> list;

        public Producer(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (flag != null) {
                }
                int randomValue = new Random().nextInt();
                System.out.println("Producing " + randomValue);
                list.add(randomValue);
                flag = i;
            }

        }
    }

    public static class Consumer extends Thread {
        final List<Integer> list;

        public Consumer(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (flag == null) {
                }
                System.out.println("Consuming " + list.get(0));
                list.remove(0);
                flag = null;
            }
        }
    }
}
