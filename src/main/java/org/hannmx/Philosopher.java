package org.hannmx;
import java.util.concurrent.locks.Lock;

class Philosopher extends Thread {
    private int id;
    private Lock leftFork;
    private Lock rightFork;
    private int eatCount = 0;

    public Philosopher(int id, Lock leftFork, Lock rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public void run() {
        try {
            while (eatCount < 3) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking");
        Thread.sleep((long) (Math.random() * 1000)); // Thinking takes random time
    }

    private void eat() throws InterruptedException {
        leftFork.lock();
        if (!rightFork.tryLock()) { // Attempt to acquire the right fork
            leftFork.unlock(); // Release the left fork if the right fork is not available
            return;
        }

        try {
            System.out.println("Philosopher " + id + " is eating");
            eatCount++;
            Thread.sleep(500); // Eating takes 500 milliseconds
        } finally {
            rightFork.unlock();
            leftFork.unlock();
        }
    }
}
