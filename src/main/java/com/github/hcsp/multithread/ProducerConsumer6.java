package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer6 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1, true);
        Container6 container=new Container6(semaphore);
        Producer producer = new Producer(container, semaphore);
        Consumer consumer = new Consumer(container, semaphore);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container6 container;
        Semaphore semaphore;

        public Producer(Container6 container, Semaphore semaphore) {
            this.container = container;
            this.semaphore=semaphore;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                    while (container.getValue().isPresent()) {
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));
                    semaphore.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Container6 container;
        Semaphore semaphore;

        public Consumer(Container6 container, Semaphore semaphore) {
            this.container = container;
            this.semaphore=semaphore;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                    while (!container.getValue().isPresent()) {
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    System.out.println("Consuming " + value);
                    container.setValue(Optional.empty());
                    semaphore.release();
            }
        }
    }
}
