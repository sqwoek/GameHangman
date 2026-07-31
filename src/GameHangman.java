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
    private static final String INPUT_LETTER_MESSAGE = "\nEnter a letter: ";
    private static final String DICTIONARY_PATH = "src/resources/dictionary.txt";
    private static final int MAX_ATTEMPTS = 6;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final HangmanPicture hangmanPicture = new HangmanPicture();
    private static List<String> dictionary;
    private static Set<Character> usedLetters = new LinkedHashSet<>();
    private static int remainingAttempts = MAX_ATTEMPTS;
    private static int correctGuesses = 0;

    public static void main(String[] args) {
        try {
            readDictionary();
        } catch (IOException | IllegalStateException ex) {
            System.out.println("Error loading file: " + ex.getMessage());
            return;
        }
        showMainMenu();
    }

    private static void readDictionary() throws IOException{
        Path dictionaryPath = Path.of(DICTIONARY_PATH);
        try {
            dictionary = Files.readAllLines(dictionaryPath);
        } catch (IOException e) {
            throw new IOException("Couldn't find a file in " + dictionaryPath.toAbsolutePath());
        }

        if (dictionary.isEmpty()) {
            throw new IllegalStateException("Dictionary is empty.");
        }
    }

    public static void showMainMenu() {
        System.out.println(START_MESSAGE);
        char letter = validateMenuLetter();
        if(letter == START_GAME_CHAR) {
            startGame();
        }
    }

    public static void startGame() {
        int wordIndex = random.nextInt(dictionary.size());
        char[] word = dictionary.get(wordIndex).toCharArray();
        char[] maskedWord = new char[word.length];
        Arrays.fill(maskedWord, ('*'));
        showWord(maskedWord);
        while (!(remainingAttempts == 0 || correctGuesses == word.length)) {
            guessLetter(word, maskedWord);
        }
        endGame(word);
    }

    private static void guessLetter(char[] word, char[] maskedWord) {
        System.out.println(INPUT_LETTER_MESSAGE);
        char letter = validateGuessedLetter();
        if (isUsedLetter(letter)) {
            System.out.println("You have already entered this letter.");
            showUsedLetters();
            return;
        }

        if (!isCorrectGuess(maskedWord, word, letter)) {
            System.out.println("OOps! There is no such letter.");
            remainingAttempts--;
            printAttemptsLeft();
            drawHangman();
        }
        showWord(maskedWord);
        showUsedLetters();
    }

    private static boolean isCorrectGuess(char[] maskedWord, char[] word, char letter) {
        boolean correctGuess = false;
        for (int i = 0; i < word.length; i++) {
            if (letter == word[i]) {
                maskedWord[i] = letter;
                correctGuesses++;
                correctGuess = true;
            }
        }
        return correctGuess;
    }

    private static char validateGuessedLetter() {
        String line = scanner.next();
        while (line.length() != 1) {
            System.out.println(INPUT_LETTER_MESSAGE);
            line = scanner.next();
        }
        line = line.toLowerCase();
        char letter = line.charAt(0);
        if (letter >= 'a' && letter <= 'z') {
            return letter;
        }
        System.out.print(INPUT_LETTER_MESSAGE);
        return validateGuessedLetter();
    }

    private static char validateMenuLetter() {
        String line = scanner.next().toUpperCase();
        while (line.length() != 1) {
            System.out.println(START_MESSAGE);
            line = scanner.next().toUpperCase();
        }
        while (!(line.charAt(0) == START_GAME_CHAR || line.charAt(0) == END_GAME_CHAR)) {
            System.out.println(START_MESSAGE);
            line = scanner.next();
        }
        return line.charAt(0);
    }

    private static void printAttemptsLeft() {
        for (int i = 0; i < remainingAttempts; i++) {
            System.out.print("❤\uFE0F");
        }
        System.out.println();
    }

    private static boolean isUsedLetter(char letter) {
        if (usedLetters.contains(letter)) {
            return true;
        }
        usedLetters.add(letter);
        return false;
    }

    private static void showUsedLetters() {
        System.out.println();
        System.out.print("Letters you have already used: ");
        for (char c : usedLetters) {
            System.out.print(c + " ");
        }
    }

    private static void showWord(char[] maskedWord) {
        System.out.print("The word is: ");
        for (char c : maskedWord) {
            System.out.print(c + " ");
        }
    }

    private static void endGame(char[] word) {
        if (remainingAttempts == 0) {
            printLoseMessage();
            System.out.println("The word was - " + new String(word));
        }
        if (correctGuesses == word.length) {
            printWinMessage();
        }
        resetGameState();
        showMainMenu();
    }

    private static void printLoseMessage() {
        System.out.println("You lost!");
    }

    private static void printWinMessage() {
        System.out.println("Congrats! You won.");
    }

    private static void resetGameState() {
        usedLetters = new LinkedHashSet<>();
        remainingAttempts = MAX_ATTEMPTS;
        correctGuesses = 0;
    }

    private static void drawHangman() {
        System.out.println(hangmanPicture.getPictures(remainingAttempts));
    }
}