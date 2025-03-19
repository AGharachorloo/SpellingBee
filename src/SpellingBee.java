import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Arya Gharachorloo
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }
    public void makeWords(String word, String letters) {
        words.add(word);
        if (letters.isEmpty()) {
            return;
        }
        for (int i = 0; i < letters.length(); i++) {
            String newWord = word + letters.charAt(i);
            String newLetters = letters.substring(0, i) + letters.substring(i + 1);
            makeWords(newWord, newLetters);
        }
    }

    public void sort() {
        // YOUR CODE HERE
        mergesort( 0, words.size()-1);
    }
    private void mergesort(int low, int high) {
        if (low >= high) {
            return;
        }
        int mid = (low+high)/2;
        mergesort(low, mid);
        mergesort(mid + 1, high);
        merge(low, mid, high);


    }
    private void merge(int low, int mid, int high) {
        ArrayList<String> temp = new ArrayList<>();
        int i = low;
        int j = mid+1;

        while (i <= mid && j <= high) {
            if (words.get(i).compareTo(words.get(j)) <= 0) {
                temp.add(words.get(i));
                i++;
            } else {
                temp.add(words.get(j));
                j++;
            }
        }
        while (i <= mid) {
            temp.add(words.get(i));
            i++;
        }
        while (j <= high) {
            temp.add(words.get(j));
            j++;
        }
        for (int k = 0; k < temp.size(); k++) {
            words.set(low + k, temp.get(k));
        }
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            if (!binarySearch(words.get(i), 0, DICTIONARY_SIZE - 1)) {
                words.remove(i);
                i--;
            }
        }
    }
    private boolean binarySearch(String word, int low, int high) {
        if (low > high) {
            return false;
        }
        int mid = low + (high - low) / 2;
        int result = word.compareTo(DICTIONARY[mid]);

        if (result == 0) {
            return true;
        } else if (result < 0) {
            return binarySearch(word, low, mid - 1);
        } else {
            return binarySearch(word, mid + 1, high);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
