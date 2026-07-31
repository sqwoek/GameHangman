import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GameHangman {
    private static final Character START_GAME_CHAR = 'S';
    private static final Character END_GAME_CHAR = 'Q';
    private static final String START_MESSAGE = String.format(
            "Enter '%s' to start a game or '%s' to quit.",
            START_GAME_CHAR,
            END_GAME_CHAR
    );
    private static final char HIDDEN_LETTER_SYMBOL = '*';
    private static final String INPUT_LETTER_MESSAGE = "\nEnter a letter: ";
    private static final String DICTIONARY_PATH = "src/resources/dictionary.txt";
    private static final int MAX_ATTEMPTS = 6;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static List<String> dictionary;
    private static Set<Character> usedLetters = new LinkedHashSet<>();
    private static int remainingAttempts = MAX_ATTEMPTS;

    public static void main(String[] args) {
        try {
            showMainMenu();
        } catch (RuntimeException ex) {
            System.out.println("Error loading file " + ex.getMessage());
        }
    }

    private static void readDictionary() {
        Path dictionaryPath = Path.of(DICTIONARY_PATH);
        try {
            dictionary = Files.readAllLines(dictionaryPath);
        } catch (IOException e) {
            throw new RuntimeException("Dictionary file not found: " + dictionaryPath.toAbsolutePath());
        }

        if (dictionary.isEmpty()) {
            throw new IllegalStateException("Dictionary is empty: " + dictionaryPath.toAbsolutePath());
        }
    }

    public static void showMainMenu() {
        while (true) {
            System.out.println(START_MESSAGE);
            char letter = readMenuChoice();
            if (letter == START_GAME_CHAR) {
                startGame();
            }
            if (letter == END_GAME_CHAR) {
                return;
            }
        }
    }

    public static void startGame() {
        readDictionary();
        String secretWord = getSecretWord();
        char[] secretWordMask = createMask(secretWord);

        while (!isGameOver(secretWordMask)) {
            showGameStatus(secretWordMask);
            processGuess(secretWordMask, secretWord);
        }
        endGame(secretWord, secretWordMask);
    }

    private static char[] createMask(String secretWord) {
        char[] secretWordMask = new char[secretWord.length()];
        Arrays.fill(secretWordMask, HIDDEN_LETTER_SYMBOL);
        return secretWordMask;
    }

    private static String getSecretWord() {
        int wordIndex = random.nextInt(dictionary.size());
        return dictionary.get(wordIndex);
    }

    private static void processGuess(char[] secretWordMask, String secretWord) {
        System.out.println();
        System.out.println(INPUT_LETTER_MESSAGE);
        char letter = readGuessedLetter();
        if (isUsedLetter(letter)) {
            System.out.println("You have already entered this letter");
            return;
        }
        if (isCorrectGuess(secretWord, letter)) {
            usedLetters.add(letter);
            openLetterInMask(secretWordMask, secretWord, letter);
            return;
        }
        System.out.println("Oops! There is no such letter");
        remainingAttempts--;
    }

    private static boolean isCorrectGuess(String secretWord, char letter) {
        for (int i = 0; i < secretWord.length(); i++) {
            if (letter == secretWord.charAt(i)) {
                return true;
            }
        }
        return false;
    }


    private static void showGameStatus(char[] secretWordMask) {
        drawHangman();
        showWord(secretWordMask);
        printAttemptsLeft();
        showUsedLetters();
    }

    private static void openLetterInMask(char[] secretWordMask, String secretWord, char letter) {
        for (int i = 0; i < secretWord.length(); i++) {
            if (letter == secretWord.charAt(i)) {
                secretWordMask[i] = letter;
            }
        }
    }


    private static char readGuessedLetter() {
        while (true) {
            String line = scanner.next();
            while (line.length() != 1) {
                System.out.println();
                System.out.println(INPUT_LETTER_MESSAGE);
                line = scanner.next();
            }
            line = line.toLowerCase();
            char letter = line.charAt(0);
            if (letter >= 'a' && letter <= 'z') {
                return letter;
            }
            System.out.println();
            System.out.println(INPUT_LETTER_MESSAGE);
        }
    }

    private static char readMenuChoice() {
        while (true) {
            String line = scanner.next();
            if (line.length() != 1) {
                System.out.println(START_MESSAGE);
                continue;
            }
            char letter = Character.toUpperCase(line.charAt(0));
            if (letter == START_GAME_CHAR || letter == END_GAME_CHAR) {
                return letter;
            }
            System.out.println(START_MESSAGE);
        }
    }

    private static void printAttemptsLeft() {
        System.out.println();
        for (int i = 0; i < remainingAttempts; i++) {
            System.out.print("❤\uFE0F");
        }
    }

    private static boolean isUsedLetter(char letter) {
        return usedLetters.contains(letter);
    }

    private static void showUsedLetters() {
        System.out.println();
        System.out.print("Letters you have already used: ");
        for (char c : usedLetters) {
            System.out.print(c + " ");
        }
    }

    private static void showWord(char[] secretWordMask) {
        System.out.print("The word is: ");
        for (char c : secretWordMask) {
            System.out.print(c + " ");
        }
    }

    private static void endGame(String secretWord, char[] secretWordMask) {
        if (isLose()) {
            printLoseMessage(secretWord);
        }
        if (isWin(secretWordMask)) {
            printWinMessage();
        }
        resetGameState();
    }

    private static boolean isGameOver(char[] secretWordMask) {
        return isLose() || isWin(secretWordMask);
    }

    private static boolean isWin(char[] secretWordMask) {
        for (char c : secretWordMask) {
            if (c == HIDDEN_LETTER_SYMBOL) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLose() {
        return remainingAttempts == 0;
    }

    private static void printLoseMessage(String word) {
        System.out.println("\nYou lost! The word was: " + word);
    }

    private static void printWinMessage() {
        System.out.println("\nCongrats! You won.");
    }

    private static void resetGameState() {
        usedLetters = new LinkedHashSet<>();
        remainingAttempts = MAX_ATTEMPTS;
    }

    private static void drawHangman() {
        HangmanRenderer.render(remainingAttempts);
    }
}