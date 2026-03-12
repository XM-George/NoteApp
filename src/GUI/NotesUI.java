package GUI;

import javax.swing.*;
import java.awt.*;

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
            textAreas[i] = textArea;
            frame.add(textArea);
        }
    }

}
