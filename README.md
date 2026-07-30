## Description



A console implementation of the classic Hangman game. Guess the hidden word one letter at a time before the hangman is fully drawn.



## Rules



- The game randomly selects a word and hides it with asterisks (`*`).

- Enter one letter at a time.

- If the guessed letter is correct, it is revealed in the word.

- If the guessed letter is incorrect, a new part of the hangman is drawn.

- The game displays a list of letters that have already been used.

- Repeated guesses of previously entered letters do not count as mistakes.

- You have a maximum of 6 incorrect guesses.

- The game ends when:

    - you reveal the entire word (**You Win!**), or

    - you make 6 incorrect guesses (**Game Over**).



This project was implemented as part of the [Java Backend Learning Course by Sergey Zhukov](https://zhukovsd.github.io/java-backend-learning-course/).