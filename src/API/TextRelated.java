package API;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextRelated
{
    public String[] getTextFromTextAreas(JTextArea[] textAreas)
    {
        String[] texts = new String[textAreas.length];
        for (int i = 0; i < textAreas.length; i++)
        {
            texts[i] = textAreas[i].getText();
        }
        return texts;
    }

    public JTextArea mouseClickOnTextAreas(JTextArea textArea)
    {
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    int pos = textArea.viewToModel2D(e.getPoint());
                    int line = textArea.getLineOfOffset(pos);

                    int lineStart = textArea.getLineStartOffset(line);
                    int lineEnd = textArea.getLineEndOffset(line);

                    String lineText = textArea.getText(lineStart, lineEnd - lineStart).replace("\n", "");

                    FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
                    int charWidth = fm.charWidth(' ');
                    int charHeight = fm.getHeight();

                    int clickedColumn = e.getX() / charWidth;
                    int clickedRow = e.getY() / charHeight;

                    if (clickedColumn > lineText.length()) {
                        int spacesNeeded = clickedColumn - lineText.length();
                        int insertPos = lineStart + lineText.length();
                        textArea.insert(" ".repeat(spacesNeeded), insertPos);
                        pos = lineStart + clickedColumn;
                    }

                    textArea.setCaretPosition(pos);

                    int totalLines = textArea.getLineCount();
                    if (clickedRow >= totalLines) {
                        int linesToAdd = clickedRow - totalLines + 1; // +1 because row index is 0-based
                        for (int i = 0; i < linesToAdd; i++) {
                            textArea.append("\n"); // add empty line
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return textArea;
    }

}
