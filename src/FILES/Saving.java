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
    FileUI F = new FileUI();
    String directory = null;
    String filename = null;

    public String saveAs(JTextArea[] textAreas)
    {
        FileDialog fd = new FileDialog(new JFrame(), "Save File", FileDialog.SAVE);
        fd.setFile("*.txt");
        fd.setVisible(true);

        String dir = fd.getDirectory();
        String file = fd.getFile();

        if(dir != null && file != null)
        {
            if (!file.toLowerCase().endsWith(".txt"))
            {
                file += ".txt";
            }
            filename = file;
            directory = dir;

            writeToFile(textAreas);
        }

        if(filename!=null)
        {
            return filename;
        }
        return "New";
    }

    public void save(JTextArea[] textAreas)
    {
        if(directory != null && filename != null)
        {
            writeToFile(textAreas);
        }
    }

    protected void writeToFile(JTextArea[] textAreas)
    {
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
