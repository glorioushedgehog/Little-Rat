package littlerat;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 * GUI class
 * Use Swing to allow easy input of text
 */
@SuppressWarnings("serial")
public class LittleWindow extends JPanel {
    private JTextArea inArea;
    /*
     * This is static so that LittleDoer can set the outArea text
     * when it finishes replacing words
     */
    private static JTextArea outArea;

    /**
     * This allows LittleDoer to set outArea's text
     * when it is done replacing words
     * @param text the text that will be shown in outArea
     */
    static void setNewText(String text){
        outArea.setText(text);
    }
    /**
     * Define the GUI
     */
    public LittleWindow() {
        // This is will be applied to components for aesthetics
        EmptyBorder cushion = new EmptyBorder(10, 10, 10, 10);
        setBorder(cushion);
        inArea = new JTextArea(40, 50);
        Font boxFont = new Font("Arial", Font.PLAIN, 17);
        inArea.setFont(boxFont);
        inArea.setEditable(true);
        inArea.setBorder(cushion);
        JButton goButton = new JButton("Go");
        Font buttonFont = new Font("Arial", Font.PLAIN, 27);
        goButton.setFont(buttonFont);
        Color buttonColor = Color.WHITE;
        goButton.setBackground(buttonColor);
        goButton.setPreferredSize(new Dimension(140, 40));
        goButton.addActionListener(event -> {
            // Start a new job of replacing the words in inArea
            JobManager.add(inArea.getText());
        });
        JCheckBox comOnly = new JCheckBox("Common words only");
        comOnly.setFont(new java.awt.Font("Arial", Font.PLAIN, 17));
        comOnly.setBorder(cushion);
        comOnly.setSelected(true);
        comOnly.addChangeListener(event -> {
            // Switch between using all words in cmupron or just those in commonPron
            LittleDoer.switchMap();
        });
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(buttonFont);
        clearButton.setBackground(buttonColor);
        clearButton.setPreferredSize(new Dimension(140, 40));
        clearButton.addActionListener(event -> {
            // Clear both text areas and stop any running jobs
            inArea.setText("");
            outArea.setText("");
            JobManager.clear();
        });
        outArea = new JTextArea(40, 50);
        outArea.setFont(boxFont);
        outArea.setEditable(false);
        outArea.setBorder(cushion);
        // Support scrolling in text areas
        JScrollPane scrollPane1 = new JScrollPane(inArea);
        JScrollPane scrollPane2 = new JScrollPane(outArea);
        JPanel fieldPanel = new JPanel();
        fieldPanel.add(scrollPane1);
        fieldPanel.add(scrollPane2);
        JPanel wholePanel = new JPanel(new BorderLayout());
        wholePanel.add(fieldPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(goButton);
        buttonPanel.add(comOnly);
        buttonPanel.add(clearButton);
        wholePanel.add(buttonPanel, BorderLayout.SOUTH);
        add(wholePanel);
    }
    /**
     * Create the JFrame and add LittleWindow to it
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Little Rat");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new LittleWindow());
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Fill LittleDoer's HashMap's of words and make GUI
     * @param args unused
     */
    public static void main(String[] args) {
        LittleDoer.getStringMaps();
        javax.swing.SwingUtilities.invokeLater(LittleWindow::createAndShowGUI);
    }
}