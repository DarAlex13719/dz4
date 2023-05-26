import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {
    public static final BlockingQueue<String> text1 = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> text2 = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> text3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        String[] words = new String[10_000];

        Thread queue = new Thread(() -> {
            for (int i = 0; i < words.length; i++) {
                words[i] = generateText("abc", 100_000);

                try {
                    text1.put(words[i]);
                    text2.put(words[i]);
                    text3.put(words[i]);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        queue.start();

        Thread queue1 = new Thread(() -> calc('a', text1));
        queue1.start();

        Thread queue2 = new Thread(() -> calc('b', text2));
        queue2.start();

        Thread queue3 = new Thread(() -> calc('c', text3));
        queue3.start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();

    }

    public static void calc(char symbol, BlockingQueue<String> textsQueue) {
        int colvo = 0;
        int colvoMax = 0;

        for (int i = 0; i < 10_000; i++) {
            try {
                String word = textsQueue.take();
                for (int j = 0; j < word.length(); j++) {
                    if (word.charAt(j) == symbol) {
                        colvo++;
                    }
                }
                if (colvo > colvoMax) {
                    colvoMax = colvo;
                }
                colvo = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("Наибольшее количество символов " + symbol + ": " + colvoMax);
    }
    
}