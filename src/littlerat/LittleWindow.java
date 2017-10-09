package littlerat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private JButton goButton = new JButton("Go");;
    private JButton clearButton = new JButton("Clear");
    /*
     * This checkbox allows the user to choose between using all
     * the words in cmupron or just the words in commonPron
     */
    private JCheckBox comOnly = new JCheckBox("Common words only");
    // The font used in the text boxes
    private Font boxFont = new java.awt.Font("Arial", Font.ROMAN_BASELINE, 17);
    // The font used in the text on the button
    private Font buttonFont = new java.awt.Font("Arial", Font.ROMAN_BASELINE, 27);
    private Color buttonColor = Color.WHITE;
    /**
     * This allows LittleDoer to set outArea's text
     * when it is done replacing words
     * @param text
     */
    public static void setNewText(String text){
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
        inArea.setFont(boxFont);
        inArea.setEditable(true);
        inArea.setBorder(cushion);
        goButton.setFont(buttonFont);
        goButton.setBackground(buttonColor);
        goButton.setPreferredSize(new Dimension(140, 40));
        goButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                // Start a new job of replacing the words in inArea
                JobManager.add(inArea.getText());
            }
        });
        comOnly.setFont(new java.awt.Font("Arial", Font.ROMAN_BASELINE, 17));
        comOnly.setBorder(cushion);
        comOnly.setSelected(true);
        comOnly.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent event) {
                // Switch between using all words in cmupron or just those in commonPron
                LittleDoer.switchMap();
            }
        });
        clearButton.setFont(buttonFont);
        clearButton.setBackground(buttonColor);
        clearButton.setPreferredSize(new Dimension(140, 40));
        clearButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                // Clear both text areas and stop any running jobs
                inArea.setText("");
                outArea.setText("");
                JobManager.clear();
            }
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new LittleWindow());
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Fill LittleDoer's HashMap's of words and make GUI
     * @param args
     */
    public static void main(String[] args) {
        LittleDoer.getStringMaps();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}