package littlerat;
/**
 * Start new word-replacement threads and interrupt old ones
 */
public class JobManager {
    // This will be set to null whenever no job is being done
    private static Thread job = null;
    /**
     * Make a new job of replacing all the words in the given text
     * with the most similar-sounding words
     * @param text
     */
    public static void add(String text){
        clear();
        Runnable newJob = new LittleDoer(text);
        job = new Thread(newJob);
        job.start();
    }
    /**
     * Stop any jobs currently being done
     */
    public static void clear() {
        if( job != null ){
            job.interrupt();
            job = null;
        }
    }
}