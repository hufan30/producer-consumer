package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    private static Container container = new Container();

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

    static class  Container{
          Object value;
    }
    public static class Producer extends Thread {
        @Override
        public void run() {
            synchronized (container){

                while (container.value != null){
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int anInt = new Random().nextInt();
                container.value=anInt;
                System.out.println("Producing "+anInt);
                container.notify();

            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            synchronized (container){
                while (container.value == null){
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Object value = container.value;
                System.out.println("Consuming "+value);

                container.value=null;
                container.notify();
            }
        }
    }
}
