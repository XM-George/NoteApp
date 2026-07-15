package api;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextRelated {

    public void enableFreePositionClick(JTextArea textArea) {
        textArea.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                moveCaretToClickedPosition(textArea, event);
            }
        });
    }

    private void moveCaretToClickedPosition(
            JTextArea textArea,
            MouseEvent event
    ) {
        try {
            textArea.requestFocusInWindow();

            FontMetrics fontMetrics =
                    textArea.getFontMetrics(textArea.getFont());

            Insets insets = textArea.getInsets();

            int lineHeight = fontMetrics.getHeight();
            int characterWidth = fontMetrics.charWidth(' ');

            if (lineHeight <= 0 || characterWidth <= 0) {
                return;
            }

            /*
             * Μετατρέπουμε το mouse position σε θέση
             * μέσα στην πραγματική περιοχή κειμένου.
             */
            int relativeX = Math.max(
                    0,
                    event.getX() - insets.left
            );

            int relativeY = Math.max(
                    0,
                    event.getY() - insets.top
            );

            int clickedRow = relativeY / lineHeight;
            int clickedColumn = relativeX / characterWidth;

            int usableWidth =
                    textArea.getWidth()
                            - insets.left
                            - insets.right;

            int maximumColumn =
                    Math.max(0, usableWidth / characterWidth - 1);

            clickedColumn = Math.min(
                    clickedColumn,
                    maximumColumn
            );

            ensureLineExists(textArea, clickedRow);
            ensureColumnExists(
                    textArea,
                    clickedRow,
                    clickedColumn
            );

        } catch (BadLocationException exception) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void ensureLineExists(
            JTextArea textArea,
            int targetRow
    ) throws BadLocationException {

        int currentLineCount = textArea.getLineCount();

        if (targetRow < currentLineCount) {
            return;
        }

        int missingLines =
                targetRow - currentLineCount + 1;

        Document document = textArea.getDocument();

        for (int i = 0; i < missingLines; i++) {
            int previousLength = document.getLength();

            document.insertString(
                    document.getLength(),
                    "\n",
                    null
            );

            /*
             * Αν το DocumentFilter μπλοκάρει τη νέα
             * γραμμή λόγω ύψους, σταματάμε.
             */
            if (document.getLength() == previousLength) {
                break;
            }
        }
    }

    private void ensureColumnExists(
            JTextArea textArea,
            int targetRow,
            int targetColumn
    ) throws BadLocationException {

        int availableLines = textArea.getLineCount();

        /*
         * Η γραμμή ίσως δεν δημιουργήθηκε επειδή
         * το height limit την μπλόκαρε.
         */
        if (targetRow >= availableLines) {
            textArea.setCaretPosition(
                    textArea.getDocument().getLength()
            );

            Toolkit.getDefaultToolkit().beep();
            return;
        }

        int lineStart =
                textArea.getLineStartOffset(targetRow);

        int lineEnd =
                textArea.getLineEndOffset(targetRow);

        String lineText = textArea.getText(
                lineStart,
                lineEnd - lineStart
        );

        /*
         * Αφαιρούμε τον χαρακτήρα αλλαγής γραμμής
         * από τον υπολογισμό του μήκους.
         */
        lineText = lineText
                .replace("\n", "")
                .replace("\r", "");

        int currentColumn = lineText.length();

        if (targetColumn > currentColumn) {
            int spacesNeeded =
                    targetColumn - currentColumn;

            String spaces = " ".repeat(spacesNeeded);

            int insertionPosition =
                    lineStart + currentColumn;

            Document document =
                    textArea.getDocument();

            int previousLength =
                    document.getLength();

            document.insertString(
                    insertionPosition,
                    spaces,
                    null
            );

            /*
             * Αν το filter μπλόκαρε μέρος ή ολόκληρη
             * την εισαγωγή, υπολογίζουμε την πραγματική
             * νέα θέση.
             */
            int insertedCharacters =
                    document.getLength() - previousLength;

            int caretPosition =
                    insertionPosition
                            + Math.max(0, insertedCharacters);

            textArea.setCaretPosition(
                    Math.min(
                            caretPosition,
                            document.getLength()
                    )
            );

            return;
        }

        int caretPosition =
                lineStart + targetColumn;

        textArea.setCaretPosition(
                Math.min(
                        caretPosition,
                        lineStart + currentColumn
                )
        );
    }
}