package FILES;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Loading
{
    public static class ReturnValues
    {
        private String filename;
        private int numberOfTextAreas;
        private String [] texts;

        public ReturnValues(String filename, int numberOfTextAreas, String [] texts)
        {
            this.filename = filename;
            this.numberOfTextAreas = numberOfTextAreas;
            this.texts = texts;
        }

        public String getFilename()
        {
            return filename;
        }

        public int getNumberOfTextAreas()
        {
            return numberOfTextAreas;
        }

        public String[] getTexts()
        {
            return texts;
        }
    }

    public ReturnValues openFile()
    {
        FileDialog fd = new FileDialog((Frame) null, "Open File", FileDialog.LOAD);
        fd.setFile("*.txt");
        fd.setVisible(true);

        String dir = fd.getDirectory();
        String file = fd.getFile();

        int numberOfTextAreas = 0;
        String[] texts = {"", "", ""};

        if(file != null && dir != null)
        {
            if(file.endsWith(".txt"))
            {
                File f = new File(dir,file);
                try(BufferedReader br = new BufferedReader(new FileReader(f)))
                {
                    try
                    {
                        numberOfTextAreas = Integer.parseInt(br.readLine());
                    }
                    catch(NumberFormatException ignored){}

                    if(numberOfTextAreas <= 0 || numberOfTextAreas > 5) {
                        return new ReturnValues(file,0, texts);
                    }
                    texts = new String[numberOfTextAreas];

                    br.readLine();
                    for(int i=0;i<numberOfTextAreas;i++)
                    {
                        StringBuilder builder = new StringBuilder();

                        String nextLine;
                        while( (nextLine = br.readLine()) != null && !("===PAGE BREAK===").equals(nextLine) )
                        {
                            builder.append(nextLine).append("\n");
                        }
                        texts[i] = builder.toString();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return new ReturnValues(file,numberOfTextAreas, texts);
            }
        }
        return new ReturnValues(file,numberOfTextAreas, texts);
    }
}
