package ru.netology;


import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
    public static Thread filler;

    public static void main(String[] args) throws InterruptedException {
        filler = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        filler.start();


        Thread threadA = new Thread(() -> {
            int result = 0;

            result = finderMax(queueA, 'a');

            System.out.println("символ \'a\' встречается " + result);
        });

        Thread threadB = new Thread(() -> {
            int result = 0;

            result = finderMax(queueB, 'b');

            System.out.println("символ \'b\' встречается " + result);
        });

        Thread threadC = new Thread(() -> {
            int result = 0;
            result = finderMax(queueC, 'c');

            System.out.println("символ \'c\' встречается " + result);

        });

        threadA.start();
        threadB.start();
        threadC.start();


        threadA.join();
        threadB.join();
        threadC.join();
        filler.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    static public int finderMax(BlockingQueue<String> queue, char sumbol) {
        int max = 0;

        String text = "";
        try {
            while (filler.isAlive()) {

                text = queue.take();


                int count = 0;
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == sumbol) {
                        count++;
                    }
                }
                if (max < count) {
                    max = count;
                }


            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return max;
    }
}
