package GUI;

import API.Print;
import FILES.Loading;
import FILES.Saving;
import API.TextRelated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NotesUI implements ActionListener
{
    TextRelated T = new TextRelated();
    Saving S = new Saving();
    Loading L = new Loading();
    Print P = new Print();

    String mainAppIconPath ="/ICONS/appIcon.png";
    String filename = "New";
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dimension = toolkit.getScreenSize();
    int width = (int) dimension.getWidth();
    int height = (int) dimension.getHeight();

    JFrame frame;
    public void start(int total)
    {
        frame=new JFrame(filename);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width/2,height/2);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        ImageIcon rawIcon =  new ImageIcon(Objects.requireNonNull(getClass().getResource(mainAppIconPath)));
        Image scaledImage = rawIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        frame.setIconImage(scaledImage);
        barMenu();
        initialiseTextArea(total,null);
        frame.setVisible(true);
    }

    JTextArea[] textAreas;
    public void initialiseTextArea(int total, String[] texts)
    {
        textAreas = new JTextArea[total];
        frame.setLayout(new GridLayout(total,1,0,0));
        for (int i = 0; i < total; i++)
        {
            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Arial",Font.PLAIN, 20));
            textArea.setTabSize(4);
            textArea.setMargin(new Insets(10,10,10,10));
            if(texts!=null && texts.length>i)
            {
                textArea.setText(texts[i]);
            }

            textArea = T.mouseClickOnTextAreas(textArea);

            textAreas[i] = textArea;
            frame.add(new JScrollPane(textArea));
        }
    }

    public void barMenu()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu printMenu = new JMenu("Print");
        JMenuItem printAll = new JMenuItem("Print");
        printAll.addActionListener(this);
        printMenu.add(printAll);

        JMenu editTextMenu = new JMenu("Edit Text");

        JMenu saveMenu = new JMenu("Save");
        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(this);
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        saveMenu.add(saveAs);
        saveMenu.add(save);

        JMenu openFileMenu = new JMenu("Open");
        JMenuItem openFile = new JMenuItem("Open File");
        openFile.addActionListener(this);
        openFileMenu.add(openFile);

        JMenu pageCountMenu = new JMenu("Pages");
        JMenuItem pageCount = new JMenuItem("Change Page Count");
        pageCount.addActionListener(this);
        pageCountMenu.add(pageCount);

        menuBar.add(printMenu);
        menuBar.add(editTextMenu);
        menuBar.add(saveMenu);
        menuBar.add(openFileMenu);
        menuBar.add(pageCountMenu);

        frame.setJMenuBar(menuBar);
        menuBar.setVisible(true);
    }


    public void getPageCount()
    {
        JDialog dialog=new JDialog();
        dialog.setTitle("Page Count");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.getContentPane().setPreferredSize(new Dimension(400,300));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setLayout(null);

        JComboBox<Integer> comboBox = new JComboBox<>();
        comboBox.setBounds(100,40,200,40);
        comboBox.addItem(1);
        comboBox.addItem(2);
        comboBox.addItem(3);
        comboBox.addItem(4);
        comboBox.addItem(5);

        JButton confirmButton = new JButton("Change Page Count");
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String[] texts = T.getTextFromTextAreas(textAreas);
                reinitTextAreas(comboBox.getSelectedIndex() + 1 , texts);
                dialog.dispose();
            }
        });
        confirmButton.setFont(new Font("Arial",Font.BOLD,15));
        confirmButton.setBounds(20,200,180,50);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dialog.dispose();
            }
        });
        cancelButton.setFont(new Font("Arial",Font.BOLD,15));
        cancelButton.setBounds(220,200,160,50);

        dialog.add(confirmButton);
        dialog.add(cancelButton);
        dialog.add(comboBox);
        dialog.setVisible(true);
    }

    public void reinitTextAreas(int pageCount, String[] texts)
    {
        frame.getContentPane().removeAll();
        initialiseTextArea(pageCount , texts);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "Print":
            P.PrintFrame((JPanel) frame.getContentPane());
            break;
            case "Save As":
            filename = S.saveAs(textAreas);
            frame.setTitle(filename);
            break;
            case "Save":
            if(filename.equals("New"))
            {
                filename = S.saveAs(textAreas);
                frame.setTitle(filename);
            }
            else
            {
                S.save(textAreas);
            }
            break;
            case "Open File":
            Loading.ReturnValues result = L.openFile();
            if(result.getFilename() != null && result.getNumberOfTextAreas()>0 && result.getNumberOfTextAreas()<6)
            {
                reinitTextAreas(result.getNumberOfTextAreas(), result.getTexts());
                frame.setTitle(result.getFilename());
            }
            break;
            case "Change Page Count":
            getPageCount();
            break;
        }

    }

}
