

/**
 * CharacterFrequency
 * Object which holds the character and its corresponding frequency
 *
 * 2/12/16
 * @author Azhar Hussain
 */
public class CharacterFrequency {

    // the character of the object
    private Character ch;

    // the frequency the character appears
    private Integer freq;

    /**
     * Constructor
     * creates the data object to hold values
     * @param character holds the character
     * @param frequency holds the frequency
     */
    public CharacterFrequency(char character, int frequency) {

        // Store the character and its frequency in the object
        ch = character;
        freq = frequency;
    }

    /**
     * Constructor
     * creates the data object to hold values
     * @param frequency holds the frequency
     */
    public CharacterFrequency(int frequency) {
        // If no character specified, just store frequency
        ch = null;
        freq = frequency;
    }

    /**
     * getFrequency
     * returns the frequency the character appears
     * @return int of frequency
     */
    public int getFrequency() {
        return freq;
    }

    /**
     * setFrequency
     * sets the frequency a character appears
     * @param frequency the frequency the character appears
     */
    public void setFrequency(int frequency) {

        this.freq = frequency;
    }

    /**
     * getCharacter
     * returns the character
     * @return char of the character
     */
    public char getCharacter() {

        return ch;
    }

    /**
     * setCharacter
     * set the character
     * @param character being set
     */
    public void setCharacter(char character) {

        this.ch = character;
    }

    /**
     * toString
     * creates a string of character and frequency
     * @return String of character and frequency
     */
    public String toString() {
        if (ch != null) {
            return ch.toString() + ": " + freq.toString();
        }
        else {
            return "null: " + freq.toString();
        }
    }
}