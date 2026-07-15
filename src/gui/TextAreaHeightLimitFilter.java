package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TextAreaHeightLimitFilter extends DocumentFilter {

    private final JTextArea textArea;

    public TextAreaHeightLimitFilter(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void insertString(
            FilterBypass filterBypass,
            int offset,
            String text,
            AttributeSet attributes
    ) throws BadLocationException {

        if (text == null) {
            return;
        }

        String currentText =
                filterBypass.getDocument().getText(
                        0,
                        filterBypass.getDocument().getLength()
                );

        String newText =
                currentText.substring(0, offset)
                        + text
                        + currentText.substring(offset);

        if (fitsInsideTextArea(newText)) {
            super.insertString(
                    filterBypass,
                    offset,
                    text,
                    attributes
            );
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(
            FilterBypass filterBypass,
            int offset,
            int length,
            String text,
            AttributeSet attributes
    ) throws BadLocationException {

        String currentText =
                filterBypass.getDocument().getText(
                        0,
                        filterBypass.getDocument().getLength()
                );

        String replacement =
                text == null ? "" : text;

        String newText =
                currentText.substring(0, offset)
                        + replacement
                        + currentText.substring(offset + length);

        if (fitsInsideTextArea(newText)) {
            super.replace(
                    filterBypass,
                    offset,
                    length,
                    text,
                    attributes
            );
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean fitsInsideTextArea(String candidateText) {
        if (textArea.getWidth() <= 0 || textArea.getHeight() <= 0) {
            return true;
        }

        Insets insets = textArea.getInsets();

        int availableWidth =
                textArea.getWidth()
                        - insets.left
                        - insets.right;

        int availableHeight =
                textArea.getHeight()
                        - insets.top
                        - insets.bottom;

        if (availableWidth <= 0 || availableHeight <= 0) {
            return true;
        }

        JTextArea testArea = new JTextArea();

        testArea.setFont(textArea.getFont());
        testArea.setLineWrap(textArea.getLineWrap());
        testArea.setWrapStyleWord(textArea.getWrapStyleWord());
        testArea.setTabSize(textArea.getTabSize());
        testArea.setText(candidateText);

        /*
         * Το test area δεν χρειάζεται margin ή border,
         * γιατί αφαιρέσαμε ήδη τα πραγματικά insets.
         */
        testArea.setMargin(new Insets(0, 0, 0, 0));
        testArea.setBorder(null);

        testArea.setSize(
                availableWidth,
                Integer.MAX_VALUE
        );

        int requiredHeight =
                testArea.getPreferredSize().height;

        /*
         * Μικρή ανοχή λόγω rounding του Swing.
         * Όχι ολόκληρη επιπλέον γραμμή.
         */
        int tolerance = 3;

        return requiredHeight <= availableHeight + tolerance;
    }
}