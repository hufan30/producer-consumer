package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);
        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container{
        private Optional<Integer> value = Optional.empty();
        private Condition valueExistCondition;
        private Condition valueEmptyCondition;

        public Container(ReentrantLock lock){
            this.valueEmptyCondition = lock.newCondition();
            this.valueExistCondition = lock.newCondition();
        }

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        public Condition getValueEmptyCondition() {
            return valueEmptyCondition;
        }

        public Condition getValueExistCondition() {
            return valueExistCondition;
        }
    }

    public static class Producer extends Thread {
        private Container container;
        private ReentrantLock lock;
        public Producer(ReentrantLock lock, Container container){
            this.lock = lock;
            this.container = container;
        }
        @Override
        public void run() {
            for (int i=0; i<10; i++){
                try{
                    lock.lock();
                    if (container.getValue().isPresent()){
                        try {
                            container.getValueEmptyCondition().await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int value = new Random().nextInt();
                    System.out.println("Producing "+value);
                    container.setValue(Optional.of(value));
                    container.getValueExistCondition().signal();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        ReentrantLock lock;
        public Consumer(ReentrantLock lock, Container container){
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i=0; i<10; i++){
                try{
                    lock.lock();
                    if (!container.getValue().isPresent()){
                        try {
                            container.getValueExistCondition().await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int value = container.getValue().get();
                    System.out.println("Consuming "+value);
                    container.setValue(Optional.empty());
                    container.getValueEmptyCondition().signal();
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
