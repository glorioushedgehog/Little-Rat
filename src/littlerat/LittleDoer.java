package littlerat;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
/**
 * Class with runnable that replaces words in a string
 * with similar sounding words
 */
public class LittleDoer implements Runnable{
    // Maps all the words in cmupron to the most similar-sounding word
    private static HashMap<String, String> bigStringMap = null;
    // Maps all the words in commonPron to the most similar-sounding word
    private static HashMap<String, String> coStringMap = null;
    // True if bigStringMap is to be used for word replacement, False if coStringMap is to be used
    private static boolean useBig;
    // This map will either equal bigStringMap or coStringMap depending on useBig
    private HashMap<String, String> stringMap = null;
    // The text whose's words will be replaced
    private String text;
    /**
     * Get both maps from their respective files
     */
    static void getStringMaps(){
        bigStringMap = getStringMap("Texts/stringMap.ser");
        coStringMap = getStringMap("Texts/coStringMap.ser");
    }
    /**
     * Set this instance of LittleDoer's stringMap and text
     * @param text the text whose words will be replaced
     */
    public LittleDoer(String text){
        if( useBig ){
            this.stringMap = bigStringMap;
        }else{
            this.stringMap = coStringMap;
        }
        this.text = text;
    }
    /**
     * Replace all the words in the text String with the
     * most similar-sounding word. This is a runnable so
     * it can be stopped with Thread.interrupt()
     */
    @Override public void run() {
        boolean interrupted = false;
        // Split around all non-words
        String[] words = text.split("[^a-zA-Z']+");
        // Split around all words
        String[] nonWords = text.split("[a-zA-Z']+");
        String[] newWords = new String[words.length];
        for( int i = 0; i < words.length; i++ ){
            if( Thread.interrupted() ){
                interrupted = true;
                break;
            }
            String w = words[i].toLowerCase();
            if( stringMap.containsKey(w) ){
                String aNew = stringMap.get(w);
                newWords[i] = copyCase(words[i], aNew);
            }else if( w.contains("'") ){
				/*
				 * Apostrophes must be handled as
				 * a special case because they are
				 * often parts of words, such as "it's"
				 */
                String[] subWords = w.split("'");
                String aNew = "";
                for( String sw : subWords ){
                    if( stringMap.containsKey(sw) ){
                        aNew += stringMap.get(sw);
                    }else{
                        aNew += sw;
                    }
                    aNew += "'";
                }
                if( aNew.length() > 0 ){
                    aNew = aNew.substring(0, aNew.length() - 1);
                }
                newWords[i] = copyCase(words[i], aNew);
            }else{
                newWords[i] = words[i];
            }
        }
        String newText = "";
        if( newWords.length > nonWords.length ){
            for( int i = 0; i < nonWords.length; i++ ){
                if( Thread.interrupted() || interrupted ){
                    interrupted = true;
                    break;
                }
                newText += newWords[i];
                newText += nonWords[i];
            }
            newText += newWords[words.length - 1];
        }else{
            for( int i = 0; i < newWords.length; i++ ){
                if( Thread.interrupted() || interrupted ){
                    interrupted = true;
                    break;
                }
                newText += nonWords[i];
                newText += newWords[i];
            }
            newText += nonWords[nonWords.length - 1];
        }
        if( !Thread.interrupted() && !interrupted  ){
            LittleWindow.setNewText(newText);;
        }
    }
    /**
     * Fetch a HashMap from the specified file
     * @param fileLoc the directory and file name of the serialized HashMap
     * @return the HashMap
     */
    @SuppressWarnings("unchecked")
    static HashMap<String, String> getStringMap(String fileLoc){
        HashMap<String, String> map = new HashMap<String, String>();
        try{
            FileInputStream fis = new FileInputStream(fileLoc);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap<String, String>) ois.readObject();
            ois.close();
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }
    /**
     * Makes s2 imitate the capitalization of s1
     * @param s1 a word
     * @param s2 another word
     * @return s2, but with capitalization similar to s1
     */
    private static String copyCase(String s1, String s2) {
        if( s1.matches(".+[A-Z]$") ){
            return s2.toUpperCase();
        }
        if( s1.matches("^[A-Z].*") ){
            return s2.substring(0, 1).toUpperCase() + s2.substring(1);
        }
        return s2;
    }
    /**
     * Switch between using words from commonPron or cmupron
     */
    public static void switchMap(){
        useBig = !useBig;
    }
}