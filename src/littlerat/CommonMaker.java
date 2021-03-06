package littlerat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Make a text file that contains some lines of cmupron.txt.
 * The lines chosen from cmupron.txt correspond to words in the common.txt file.
 * The result is a subset of cmupron.txt that only contains about 10000 words, all
 * of which are common in modern English.
 */
public class CommonMaker {
    // This HashMap will be filled with all the words in common.txt (the integer values are not used)
    private static HashMap<String, Integer> coMap = new HashMap<>();
    // This ArrayList will be filled with all desired lines of cmupron.txt
    private static ArrayList<String> coLines = new ArrayList<>();
    // The directory and name of common
    private static final String common = "Texts/common.txt";
    // The directory and name of cmupron
    private static final String cmupron = "Texts/cmupron.txt";
    /**
     * Read common into coMap
     */
    private static void readCommon(){
        FileReader fr;
        try {
            fr = new FileReader(common);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while( line != null ){
                coMap.put(line.trim(), 0);
                line = br.readLine();
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Read through cmupron and decide which lines should be added to coLines
     */
    private static void readPron(){
        FileReader fr;
        try {
            fr = new FileReader(cmupron);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while( line != null ){
                if( coMap.containsKey(line.trim().split(" ", 2)[0].toLowerCase().replaceAll("'", "")) ){
                    coLines.add(line);
                }
                line = br.readLine();
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Write all the strings in coLines to commonPron.txt
     */
    private static void writeCommon(){
        try{
            FileWriter writer = new FileWriter("Texts/commonPron.txt");
            for( String s : coLines ){
                writer.append(s);
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Call the methods
     * @param args unused
     */
    public static void main(String[] args) {
        readCommon();
        readPron();
        writeCommon();
    }

}
