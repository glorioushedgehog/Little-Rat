# Little-Rat
Replaces words in text with similar-sounding words

Uses a scoring system and a global alignment algorithm to choose pairs of distinct words that sound more similar to each other than any other word.

Also includes a GUI for easily testing the output of its algorithm.

## Components

### LittleMaker

Generates two .ser files of two HashMaps: stringMap.ser maps words to the most similar-sounding word, while coStringMap.ser maps words to the most similar-sounding common English word.

### LittleWindow

GUI application that allows user to turn formatted text into text with the same formatting but with words replaced according to LittleMaker's algorithm.