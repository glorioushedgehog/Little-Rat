package littlerat;

import java.util.ArrayList;
/**
 * Class for processing and storing data for each
 * individual word
 */
class Word {
    // the string of the word, all lower case
    private String word = "";
    /*
     * A matrix of all the consonant sounds in the word.
     * There is one String[] for each space surrounding a vowel.
     * For example, the one-syllable word happy would have two String[]'s:
     * one for the consonants before the "a" sound, and another for the
     * consonants after the "a" sound
     */
    private String[][] consts = null;
    // Stores the number of consonants in each space surrounding the word's vowels
    private int[] numConsts = null;
    // The word's vowels
    private String[] vows = null;
    // The number of syllables in the word
    private int syls = 0;
    // The difference score of this word and the most similar sounding word found so far
    private int diff = Integer.MAX_VALUE;
    // Getters
    int getDiff() {
        return this.diff;
    }
    String getWord() {
        return word;
    }
    private String[][] getConsts() {
        return consts;
    }
    private int[] getNumConsts() {
        return numConsts;
    }
    private String[] getVows() {
        return vows;
    }
    int getSyls(){
        return syls;
    }
    /**
     * Set this word's diff to be its difference score
     * between another word
     * @param word2 the word that this word will be compared to
     */
    void setDiff(Word word2) {
        int score = 0;
        for( int i = 0; i < this.syls + 1; i++ ){
            int num1 = this.numConsts[i];
            int num2 = word2.getNumConsts()[i];
            if( num1 == 0 ){
                score += num2;
            }else if( num2 == 0 ){
                score += num1;
            }else{
                score += Align.getScore(this.consts[i], word2.getConsts()[i], num1, num2);
            }
        }
        for( int i = 0; i < this.syls; i++ ){
            score += Score.score(this.vows[i], word2.getVows()[i]);
        }
        this.diff = score;
    }
    /**
     * Tell if this word is just a different grammatical form of another word.
     * This allows prevention of matching "reason" with "reasoned", for instance.
     * @param w the word this word will be compared to
     * @return true if w is a form of this word, false if it is not
     */
    boolean diffWord(Word w){
        return !this.formOf(w) && !w.formOf(this);
    }
    /**
     * Tell if the word is another word with an "s", "'s", "s'", "d", "ed"
     * at the end
     * @param w the word this word will be compared to
     * @return true if the word is w with a certain suffix added, false if it is not
     */
    private boolean formOf(Word w){
        int leng = this.word.length();
        if( this.word.endsWith("'s") || this.word.endsWith("s'") || this.word.endsWith("ed") ){
            return w.getWord().startsWith(this.word.substring(0, leng - 2));
        }
        if( this.word.endsWith("s") || this.word.endsWith("d") ){
            return w.getWord().startsWith(this.word.substring(0, leng - 1));
        }
        return false;
    }
    /**
     * Fill the word's fields according to a line of cmupron
     * @param cmuline the line from cmupron on which this word will be based
     */
    Word(String cmuline){
        String[] splitLine = cmuline.split(" ", 2);
        this.word = splitLine[0].trim().toLowerCase();
        String[] pron = splitLine[1].trim().split(" ");
        ArrayList<ArrayList<String>> tempConsts = new ArrayList<ArrayList<String>>();
        ArrayList<String> tempVows = new ArrayList<String>();
        ArrayList<String> tempTempConsts = new ArrayList<String>();
        for( String pho : pron ){
            if( pho.length() == 3 ){
                tempVows.add(pho);
                tempConsts.add(tempTempConsts);
                tempTempConsts = new ArrayList<String>();
            }else{
                tempTempConsts.add(pho);
            }
        }
        tempConsts.add(tempTempConsts);
        this.vows = new String[tempVows.size()];
        for( int i = 0; i < getVows().length; i++ ){
            getVows()[i] = tempVows.get(i);
        }
        syls = getVows().length;
        consts = new String[syls+1][5];
        numConsts = new int[syls+1];
        for( int i = 0; i < consts.length; i++ ){
            numConsts[i] = tempConsts.get(i).size();
            for( int j = 0; j < tempConsts.get(i).size(); j++ ){
                consts[i][j] = tempConsts.get(i).get(j);
            }
        }
    }
}
