package org.example;

public class Main {

    private static int x = 0;
    private static int count = 0;
    private static Object lock = new Object(); // Не final

    static class LuckyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                int localX;
                synchronized (lock) {
                    if (x >= 999999) {
                        break;
                    }
                    localX = x;
                    x++;
                }

                if (isLucky(localX)) {
                    synchronized (lock) {
                        count++;
                    }
                    System.out.println(localX);
                }
            }
        }

        private boolean isLucky(int number) {
            return (number % 10) + (number / 10) % 10 + (number / 100) % 10
                    == (number / 1000) % 10 + (number / 10000) % 10 + (number / 100000) % 10;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new LuckyThread();
        Thread t2 = new LuckyThread();
        Thread t3 = new LuckyThread();

        t1.start();
        t2.start();
        t3.start();

        // Предположим, что один из потоков изменяет ссылку lock
        t1.join();
        lock = new Object(); // Это изменение ссылки может привести к ошибкам синхронизации
        t2.join();
        t3.join();

        System.out.println("Total: " + count);
    }
}
