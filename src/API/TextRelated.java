package API;

import javax.swing.*;
import java.awt.*;

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
}
