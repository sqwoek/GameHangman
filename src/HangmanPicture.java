public class HangmanPicture {
    private static final String[] PICTURES = {
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

    public static String[] getPictures() {
        return PICTURES;
    }
}
