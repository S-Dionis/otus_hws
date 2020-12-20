package ru.otus;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();

        Thread thread1 = new Thread(() -> main.startCount(0));
        Thread thread2 = new Thread(() -> main.startCount(1));

        thread1.start();
        thread2.start();

    }

    private int counter = 1;
    private int current = 0;

    private static final int CAP = 20;
    private static final int HALF = CAP / 2;

    private synchronized void startCount(int threadNumber) {
        do {
            try {
                while (threadNumber != current) {
                    wait();
                }

                if (counter < CAP) {
                    say(counter < HALF ? counter : CAP - counter);
                }

                counter = (threadNumber & current) == 1 ? ++counter : counter;
                current = current ^ 1;

                notifyAll();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        } while (counter < 20);
    }

    private void say(int number) {
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();

        System.out.println(number + " from " + name + " with " + id);
    }

}