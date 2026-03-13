package FILES;

import GUI.FileUI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Saving
{
    public void saveAs(JTextArea[] textAreas)
    {
        FileDialog fd = new FileDialog(new JFrame(), "Save File", FileDialog.SAVE);
        fd.setFile("*.txt");
        fd.setVisible(true);

        String directory = fd.getDirectory();
        String filename = fd.getFile();

        FileUI F = new FileUI();
        if(directory != null && filename != null)
        {
            if (!filename.toLowerCase().endsWith(".txt"))
            {
                filename += ".txt";
            }

            StringBuilder content = new StringBuilder();

            for (JTextArea area : textAreas) {
                content.append("===PAGE BREAK===\n");
                content.append(area.getText());
                content.append("\n");
            }

            File file = new File(directory,filename);
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
            {
                writer.write(textAreas.length + "\n");
                writer.write(content.toString());
                F.saveMessage("SUCCESS");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                F.saveMessage("FAILED");
            }
        }
    }

}
