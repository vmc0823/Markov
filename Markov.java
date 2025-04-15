import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Markov Text Generator
 * The program will read a text file, generate a collection of words and words that follow that word, and will then use those words to generate new text
 * @author Valentina Waltman
 * created: 3/16/2025
 * version 1.2.0
 */

public class Markov {

    //fields
    private static final String BEGINS_SENTENCE = "__$";
    private static final String PUNCTUATION_MARKS = ".!?$";

    private HashMap<String, ArrayList<String>> words = new HashMap<>();
    private String prevWord;
    private Random random;

    /**
     * constructor initializes the HashMap words with key BEGINS_SENTENCE and the prevWord = BEGINS_SENTENCE
     */
    public Markov() {
        words.put(BEGINS_SENTENCE, new ArrayList<>());
        random = new Random();
        prevWord = BEGINS_SENTENCE;
    }

    /**
     * internal mapping of words
     * @return map of words to the list followers
     */
    HashMap<String, ArrayList<String>> getWords() {
        return words;
    }

    /**
     * reads files as txt line by line, trims each one, passes non-empty lines to addLine
     * @param filename name of the read txt file
     */
    public void addFromFile(String filename) {
        File file = new File(filename);

        Scanner scan = null;

        try{
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.printf("Could not open %s%n", filename);
        }
        while(scan.hasNextLine()){
            String line = scan.nextLine().trim();
            if (!line.isEmpty()) {
                addLine(line);
            }
        }
        scan.close();
    }
    /**
     * takes a line and splits it into words, trims the words, and calls addWord
     * @param line this is the line of text that gets processed
     */
    void addLine(String line){
        if (line == null || line.trim().isEmpty()) {
           return;
        }
        String[] tokens = line.trim().split("\\s+");
        for (String token : tokens) {
            addWord(token);
        }
    }

    /**
     * adds the word to the arrayList indexed by prevWord.
     * @param word this is the current word to add
     */
    void addWord(String word){
        if (endsWithPunctuation(prevWord)) {
            words.get(BEGINS_SENTENCE).add(word);
        } else {
            if (!words.containsKey(prevWord)) {
                words.put(prevWord, new ArrayList<>());
            }
            words.get(prevWord).add(word);
        }
        prevWord = word;
    }

    /**
     * uses dictionary  to generate sentences
     * @return a string from the words array indexed by prevWord
     */
    public String getSentence(){
        ArrayList<String> startList = words.get(BEGINS_SENTENCE);
        if (startList == null || startList.isEmpty()) {
            return "";
        }
        String currentWord = randomWord(BEGINS_SENTENCE);
        if (currentWord == null) {
            return "";
        }
        StringBuilder sentence = new StringBuilder(currentWord);
        while (!endsWithPunctuation(currentWord)) {
            String nextWord = randomWord(currentWord);
            if (nextWord == null) {
                break;
            }
            sentence.append(" ").append(nextWord);
            currentWord = nextWord;
        }
        return sentence.toString();
    }

    /**
     * Selects random word from the arrayList indexed by prevWord in the hashMap words.
     * @param prevWord is an object key different from the instance variable prevWord
     * @return a random word.
     */
    String randomWord(String prevWord){
        ArrayList<String> list = words.get(prevWord);
        if (list == null || list.isEmpty()) {
            return "";
        }
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    /**
     * checks the passed in word for punctuation and returns if the last character is in the string of punctuation
     * @return true if word ends in punctuation
     */
    public static boolean endsWithPunctuation(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        try {
            char lastChar = word.charAt(word.length() - 1);
            return PUNCTUATION_MARKS.indexOf(lastChar) != -1;
        } catch (Exception e) {
            System.out.println("Error checking punctuation for word: " + word);
            return false;
        }

    }

    /**
     * @return toString of HashMap words
     */
    @Override
    public String toString() {
        return words.toString();
    }
}
