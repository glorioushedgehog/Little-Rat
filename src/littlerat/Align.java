package littlerat;
/**
 * Use global string alignment to determine the
 * phonetic differences between two arrays of phonemes.
 */
class Align {
    // Two arrays of phonemes
    private static String[] arr1 = null;
    private static String[] arr2 = null;
    /**
     * Get lowest score possible for the two arrays of phonemes
     * @param ar1 an array of phonemes
     * @param ar2 an array of phonemes
     * @param i the number of phonemes in ar1
     * @param j the number of phonemes in ar2
     * @return the score
     */
    static int getScore(String[] ar1, String[] ar2, int i, int j){
        arr1 = ar1;
        arr2 = ar2;
        return optimal(i, j);
    }
    /**
     * Get the lowest possible score for arr1 up to the the index of i
     * and arr2 up to the index of j
     * @param i the lowest index of arr1 that is not considered
     * @param j the lowest index of arr2 that is not considered
     * @return the lowest possible score for the subsets of the arrays
     */
    private static int optimal(int i, int j){
        if( i == 0 ){
            return j * Score.gap;
        }
        if( j == 0 ){
            return i * Score.gap;
        }
        int s = optimal(i - 1, j) + Score.gap;
        s = Math.min(s, optimal(i, j - 1) + Score.gap);
        return s = Math.min(s, optimal(i - 1, j - 1) + Score.score(arr1[i-1], arr2[j-1]));
    }
}
