package littlerat;

import java.util.HashMap;
/**
 * Return a number representing how similar two phonemes are.
 * The numbers are used to score two words by their phonetic differences.
 */
public class Score {
    /*
     * This HashMap will store pairs of similar-sounding phonemes as arrays.
     * The integer values are not used.
     */
    private static HashMap<String[], Integer> closeMap = new HashMap<String[], Integer>();
    // The score for a missing or added phoneme
    protected static final int gap = 3;
    // The score for replacing a phoneme with a similar-sounding phoneme
    private static final int close = 1;
    // The score for replacing a phoneme with a very different sounding phoneme
    private static final int far = 2;
    // True if closeMap has been filled, false if it has not
    private static boolean mapBuilt;
    /**
     * Fill closeMap with pairs of similar-sounding phonemes
     */
    private static void buildMap() {
        String[][] pairs = {
                {"D", "T"}, {"T", "D"},
                {"SH", "ZH"}, {"ZH", "SH"},
                {"DH", "TH"}, {"TH", "DH"},
                {"AO", "AA"}, {"AA", "AO"},
                {"Z", "ZH"}, {"ZH", "Z"},
                {"V", "W"}, {"W", "V"},
                {"Y", "W"}, {"W", "Y"},
                {"M", "N"}, {"N", "M"},
                {"CH", "K"}, {"K", "CH"},
                {"S", "SH"}, {"SH", "S"},
                {"B", "P"}, {"P", "B"},
                {"G", "JH"}, {"JH", "G"},
                {"UH", "UW"}, {"UW", "UH"},
                {"N", "NG"}, {"NG", "N"}
        };
        for( String[] p : pairs ){
            closeMap.put(p, 0);
        }
        mapBuilt = true;
    }
    /**
     * Give the score for replacing one phoneme with another
     * @param s1 a phoneme
     * @param s2 another phoneme
     * @return the score for the two phonemes
     */
    public static int score(String s1, String s2) {
        if( !mapBuilt ){
            buildMap();
        }
        if( s1.equals(s2) ){
            return 0;
        }
        String[] pair = {s1, s2};
        if( closeMap.containsKey(pair) ){
            return close;
        }
        return far;
    }


}
