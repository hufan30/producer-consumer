package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container1 container = new Container1();
        Object lock = new Object();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    synchronized (lock) {
                        while (container.getValue().isPresent()) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        int r = new Random().nextInt();
                        System.out.println("Producing " + r);
                        container.setValue(Optional.of(r));
                        lock.notify();
                    }
                }
            }
        });
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    synchronized (lock) {
                        while (!container.getValue().isPresent()) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Integer value = container.getValue().get();
                        System.out.println("Consuming " + value);
                        container.setValue(Optional.empty());
                        lock.notify();
                    }
                }
            }
        });
    }
}
