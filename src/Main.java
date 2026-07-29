import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static List<String> dictionary = List.of("cat", "dog", "car", "baglan", "naomi");
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static char[] usedLetters = new char[33];
    static int indexOfUsedLetters = 0;
    static int incorrectGuesses = 6;
    static int correctGuesses = 0;

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        System.out.println("Enter 'S' to Start a new game or 'Q' to Quit");
        String line = scanner.nextLine().toLowerCase();
        while (!(line.equals("s")) || line.equals("q")) {
            System.out.println("Enter 'S' to Start a new game or 'Q' to Quit");
            line = scanner.nextLine().toLowerCase();
        }
        if(line.equals("s")) {
            midGame();
        } else {
            System.exit(0);
        }
    }

    public static void midGame() {
        int wordIndex = random.nextInt(dictionary.size());
        char[] word = dictionary.get(wordIndex).toCharArray();
        char[] visibleWord = new char[word.length];
        Arrays.fill(visibleWord, '*');

        showWord(visibleWord);
        while (incorrectGuesses != 0 && correctGuesses != word.length) {
            repeat(word, visibleWord, word.length);
        }
        
        endGame(word);
    }

    private static void repeat(char[] word, char[] visibleWord, int length) {
        System.out.println();
        System.out.print("Enter a character: ");
        String line = scanner.nextLine();
        while (line.length() != 1) {
            System.out.print("Enter a character: ");
            line = scanner.nextLine();
        }

        char letter = line.charAt(0);
        usedLetters[indexOfUsedLetters++] = letter;
        boolean isGuessRight = false;

        for (int i = 0; i < length; i++) {
            if (letter == word[i]) {
                visibleWord[i] = letter;
                correctGuesses++;
                isGuessRight = true;
            }
        }

        if (!isGuessRight) {
            System.out.println("OOps! There is no such letter!");
            incorrectGuesses--;
            System.out.println(incorrectGuesses + " attempts left");
            drawHangman();
        }
        showWord(visibleWord);
        System.out.println();
        showUsedLetters();
    }

    private static void endGame(char[] word) {
        if (incorrectGuesses == 0) {
            System.out.println("You lose!");
        } else {
            if (correctGuesses == word.length) {
                System.out.println("Wow! You won!");
            }
        }
        usedLetters = new char[33];
        indexOfUsedLetters = 0;
        incorrectGuesses = 6;
        correctGuesses = 0;
        startGame();
    }

    private static void showUsedLetters() {
        System.out.println();
        System.out.print("Letters you have already used: ");
        for (int i = 0; i < indexOfUsedLetters; i++) {
            System.out.print(usedLetters[i] + " ");
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
        switch (incorrectGuesses) {
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