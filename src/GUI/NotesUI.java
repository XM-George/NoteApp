package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NotesUI
{
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dimension = toolkit.getScreenSize();
    int width = (int) dimension.getWidth();
    int height = (int) dimension.getHeight();

    JFrame frame;
    public void start()
    {
        frame=new JFrame("Note App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width/2,height/2);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        initialiseTextArea(3);
        frame.setVisible(true);
    }

    public void initialiseTextArea(int total)
    {
        JTextArea[] textAreas = new JTextArea[total];
        frame.setLayout(new GridLayout(total,1,0,20));
        for (int i = 0; i < total; i++)
        {
            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Arial",Font.PLAIN, 20));
            textArea.setTabSize(4);
            textArea.setMargin(new Insets(10,10,10,10));


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





            textAreas[i] = textArea;
            frame.add(new JScrollPane(textArea));
        }
    }

}
