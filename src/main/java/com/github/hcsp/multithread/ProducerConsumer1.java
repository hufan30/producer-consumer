package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Object lock = new Object();
        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container{
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        private Container container;
        private Object lock;
        public Producer(Object lock, Container container){
            this.lock = lock;
            this.container = container;
        }
        @Override
        public void run() {
            for (int i=0; i<10; i++){
                synchronized (lock){
                    if (container.getValue().isPresent()){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int value = new Random().nextInt();
                    System.out.println("Producing "+value);
                    container.setValue(Optional.of(value));
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Object lock;
        public Consumer(Object lock, Container container){
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i=0; i<10; i++){
                synchronized (lock){
                    if (!container.getValue().isPresent()){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int value = container.getValue().get();
                    System.out.println("Consuming "+value);
                    container.setValue(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}
