import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    private static List<String> dictionary;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static Set<Character> usedLetters = new HashSet<>();
    private static int remainingAttempts = 6;
    private static int correctGuesses = 0;

    public static void main(String[] args) {
        if(!readDictionary()) return;
        showMainMenu();
    }

    private static boolean readDictionary() {
        try {
            dictionary = Files.readAllLines(Path.of("src/resources/dictionary.txt"));
        } catch (IOException e) {
            System.out.println("Файл не найден.");
            return false;
        }

        if (dictionary.isEmpty()) {
            System.out.println("Файл со словами пуст.");
            return false;
        }
        return true;
    }

    public static void showMainMenu() {
        System.out.println("Enter 'S' to Start a new game or 'Q' to Quit");
        String line = scanner.nextLine().toLowerCase();
        while (!(line.equals("s")) || line.equals("q")) {
            System.out.println("Enter 'S' to Start a new game or 'Q' to Quit");
            line = scanner.nextLine().toLowerCase();
        }
        if(line.equals("s")) {
            startGame();
        } else {
            System.exit(0);
        }
    }

    public static void startGame() {
        int wordIndex = random.nextInt(dictionary.size());
        char[] word = dictionary.get(wordIndex).toCharArray();
        char[] visibleWord = new char[word.length];
        Arrays.fill(visibleWord, '*');

        showWord(visibleWord);
        while (!(remainingAttempts == 0 || correctGuesses == word.length)) {
            guessLetter(word, visibleWord);
        }
        
        endGame(word);
    }

    private static boolean addUsedLetter(char letter) {
        if (usedLetters.contains(letter)) {
            return false;
        }
        usedLetters.add(letter);
        return true;
    }

    private static void guessLetter(char[] word, char[] visibleWord) {
        System.out.println();
        System.out.print("Enter a character: ");
        char letter = validateLetter();
        if (!addUsedLetter(letter)) {
            System.out.println("You have already entered this letter!");
            showUsedLetters();
            return;
        }
        boolean isGuessRight = false;

        for (int i = 0; i < word.length; i++) {
            if (letter == word[i]) {
                visibleWord[i] = letter;
                correctGuesses++;
                isGuessRight = true;
            }
        }

        if (!isGuessRight) {
            System.out.println("OOps! There is no such letter!");
            remainingAttempts--;
            printAttemptsLeft();
            drawHangman();
        }
        showWord(visibleWord);
        System.out.println();
        showUsedLetters();
    }

    private static char validateLetter() {
        String line = scanner.next();
        while (line.length() != 1) {
            System.out.println("Enter a character: ");
            line = scanner.next();
        }
        char letter = line.charAt(0);
        if (letter >= 'A' && letter <= 'Z') {
            line = line.toLowerCase();
            return line.charAt(0);
        }
        if (letter >= 'a' && letter <= 'z') {
            return letter;
        }
        System.out.print("Enter a letter: ");
        return validateLetter();
    }

    private static void printAttemptsLeft() {
        for (int i = 0; i < remainingAttempts; i++) {
            System.out.print("❤\uFE0F");
        }
    }

    private static void endGame(char[] word) {
        if (remainingAttempts == 0) {
            System.out.println();
            System.out.println("You lose!" + " The word was: " + word.toString());
        } else {
            if (correctGuesses == word.length) {
                System.out.println();
                System.out.println("Wow! You won!");
            }
        }
        usedLetters = new HashSet<>();
        remainingAttempts = 6;
        correctGuesses = 0;
        showMainMenu();
    }

    private static void showUsedLetters() {
        System.out.println();
        System.out.print("Letters you have already used: ");
        for (char c : usedLetters) {
            System.out.print(c + " ");
        }
    }

    private static void showWord(char[] visibleWord) {
        System.out.println();
        System.out.print("The word is: ");
        for (int i = 0; i < visibleWord.length; i++) {
            System.out.print(visibleWord[i]);
        }
    }

    private static void drawHangman() {
        System.out.println();
        switch (remainingAttempts) {
            case 5:
                System.out.println(" ---");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                break;
            case 4:
                System.out.println(" ---");
                System.out.println("|/  |");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                break;
            case 3:
                System.out.println(" ---");
                System.out.println("|/  |");
                System.out.println("|   O");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                break;
            case 2:
                System.out.println(" ---");
                System.out.println("|/  |");
                System.out.println("|   O");
                System.out.println("|  /|\\");
                System.out.println("|");
                System.out.println("|");
                break;
            case 1:
                System.out.println(" ---");
                System.out.println("|/  |");
                System.out.println("|   O");
                System.out.println("|  /|\\");
                System.out.println("|   |");
                System.out.println("|");
                break;
            case 0:
                System.out.println(" ---");
                System.out.println("|/  |");
                System.out.println("|   O");
                System.out.println("|  /|\\");
                System.out.println("|   |");
                System.out.println("|  /\\");
                break;
        }
    }
}