package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileUI
{
    public void saveMessage(String status)
    {
        String title = "";
        String message = "";

        switch (status)
        {
            case "SUCCESS":
            title = "File Saved";
            message = "File was saved successfully!";
            break;
            case "FAILED":
            title = "Saving Failed";
            message = "File could not be saved!";
        }

        JDialog dialog = new JDialog();
        dialog.setLayout(null);
        dialog.getContentPane().setPreferredSize(new Dimension(300,200));
        dialog.pack();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        JTextField messageField = new JTextField();
        messageField.setText(message);
        messageField.setBorder(null);
        messageField.setEditable(false);
        messageField.setFocusable(false);
        messageField.setBounds(15,40,270,40);
        messageField.setFont(new Font(null, Font.BOLD, 20));

        JButton okButton = new JButton("OK");
        okButton.setBounds(100,120,100,40);
        okButton.setFont(new Font(null, Font.BOLD, 20));
        okButton.setFocusable(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(messageField);
        dialog.add(okButton);
        dialog.setVisible(true);

    }
}
