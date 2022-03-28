package org.tonkushin;

public class Main {
    private static final int max = 20;

    public static void main(String[] args) {
        HashTable<Integer, Integer> hashtable = new HashTable<>();

        for (int i = 0; i < max; i++) {
            hashtable.put(i, i + 1);
            System.out.print(hashtable.get(i) + " ");
        }

        System.out.println();

        for (int i = 0; i < max; i++) {
            System.out.print(hashtable.get(i) + " ");
            if (i + 1 != hashtable.get(i)) {
                System.out.println("NOT OK");
            }
        }

        System.out.println();
        System.out.println("OK");

        for (int i = 0; i < max; i++) {
            System.out.print(hashtable.remove(i) + " ");
        }
        System.out.println();
        System.out.println("OK");
    }
}
