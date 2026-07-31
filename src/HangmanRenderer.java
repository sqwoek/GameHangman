public class HangmanRenderer {
    private static final String[] STAGES = {
            """
      ----
    |/  |
    |   O
    |  /|\\
    |   |
    |  /\\
    """,
            """
     ----
    |/  |
    |   O
    |  /|\\
    |   |
    |
    """,
            """
     ----
    |/  |
    |   O
    |  /|\\
    |
    |
    """,
            """
     ----
    |/  |
    |   O
    |
    |
    |
    """,
            """
     ----
    |/  |
    |
    |
    |
    |
    """,
            """
    |
    |
    |
    |
    |
    """
    };

    public static void render(int remainingAttempts) {
        if (remainingAttempts >= STAGES.length || remainingAttempts < 0) {
            System.out.println("No stage available for " + remainingAttempts);
            return;
        }
        System.out.println(STAGES[remainingAttempts]);
    }
}
