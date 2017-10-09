package littlerat;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
/**
 * Make and store a HashMap that maps all words in
 * cmupron.txt and commonPron.txt to words from the
 * respective file that sound most similar to the word
 */
public class LittleMaker {
    /*
     * If this is set to true, the map will be made from the words in commonPron.txt
     * If it is set to false, the all the words in cmupron.txt will be used
     */
    private static final boolean commonOnly = true;
    // The highest number of syllables of any word in cmupron.txt
    private static final int maxSyls = 9;
    // The directory and filename of the pronunciation text
    private static String pronText;
    // The directory and filename of the file to which the map will be written as words
    private static String mapText;
    // The directory and filename of the file to which the serialized map will be written
    private static String serMap;
    // Map that maps Word objects to the Word object of the most similar-sounding word
    private static HashMap<Word, Word> wordMap = new HashMap<Word, Word>();
    // Map that maps word string to the word string of the most similar-sounding word
    private static HashMap<String, String> stringMap = new HashMap<String, String>();
    // Map used to store Word objects in ArrayLists according to the number of syllables the words have
    private static HashMap<Integer, ArrayList<Word>> sylMap = new HashMap<Integer, ArrayList<Word>>();
    // The same map as sylMap, but uses Arrays instead of ArrayLists
    private static HashMap<Integer, Word[]> arrMap = new HashMap<Integer, Word[]>();
    /**
     * Read all the lines of pronText and make a new
     * Word object for each line. The Word objects are stored
     * in sylMap according to their number of syllables
     */
    private static void readPron(){
        for( int i = 1; i < maxSyls; i++ ){
            sylMap.put(i, new ArrayList<Word>());
        }
        FileReader fr = null;
        try {
            fr = new FileReader(pronText);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while( line != null ){
                Word w = new Word(line);
                if( w.getWord().matches("[a-zA-Z']*") ){
                    sylMap.get(w.getSyls()).add(w);
                }
                line = br.readLine();
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Convert sylMap into arrMap
     */
    private static void fillArrMap(){
        for( int i = 1; i < maxSyls; i++ ){
            int siz = sylMap.get(i).size();
            Word[] temp = new Word[siz];
            for( int j = 0; j < siz; j++ ){
                temp[j] = sylMap.get(i).get(j);
            }
            arrMap.put(i, temp);
        }
    }
    /**
     * Fill wordMap with all Word object mapped to the
     * word object of each word's most similar-sounding word
     */
    private static void fillWordMap(){
        for( int i = 1; i < maxSyls; i++ ){
            Word[] wordArr = arrMap.get(i);
            for( int j = 0; j < wordArr.length - 1; j++ ){
                Word word1 = wordArr[j];
                for( int k = j + 1; k < wordArr.length; k++ ){
                    Word word2 = wordArr[k];
                    if( word1.diffWord(word2) ){
                        word2.setDiff(word1);
                        if( wordMap.containsKey(word1) ){
                            if( word2.getDiff() < wordMap.get(word1).getDiff() ){
                                wordMap.put(word1, word2);
                            }
                        }else{
                            wordMap.put(word1, word2);
                        }
                    }
                }
            }
            System.out.println("Done with "+i+" syllable words");
        }
    }
    /**
     * Convert wordMap, a map of Word objects to other Word objects,
     * to stringMap, a map of strings to strings
     */
    private static void fillStringMap() {
        Set<Word> words = wordMap.keySet();
        for( Word w : words ){
            stringMap.put(w.getWord(), wordMap.get(w).getWord());
        }
    }
    /**
     * Save stringMap with serialization
     */
    private static void serializeWordMap(){
        try{
            FileOutputStream fos = new FileOutputStream(serMap);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stringMap);
            oos.close();
            fos.close();
            System.out.println("Serialized HashMap data is saved in " + serMap);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    /**
     * Write a readable version of stringMap so that the output can
     * be inspected
     */
    private static void writeStringMap(){
        try{
            FileWriter writer = new FileWriter(mapText);
            Set<String> wors = stringMap.keySet();
            for( String s : wors ){
                writer.append(s);
                writer.append(" ");
                writer.append(stringMap.get(s));
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("HashMap values and keys have been written to " + mapText);
    }
    /**
     * Define the proper files to use and track time spent
     * @param args unused
     */
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        if( commonOnly ){
            pronText = "Texts/commonPron.txt";
            mapText = "Texts/coMap.txt";
            serMap = "Texts/coStringMap.ser";
        }else{
            pronText = "Texts/cmupron.txt";
            mapText = "Texts/map.txt";
            serMap = "Texts/stringMap.ser";
        }
        readPron();
        fillArrMap();
        fillWordMap();
        fillStringMap();
        serializeWordMap();
        writeStringMap();
        System.out.println("Seconds spent: " + (System.currentTimeMillis() - start )/1000);
    }
}
