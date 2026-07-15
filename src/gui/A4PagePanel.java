package gui;

import javax.swing.*;
import java.awt.*;

public class A4PagePanel extends JPanel {

    public static final int PAGE_WIDTH = 842;
    public static final int PAGE_HEIGHT = 595;

    private final A5SectionPanel leftSection;
    private final A5SectionPanel rightSection;

    public A4PagePanel() {
        setLayout(new GridLayout(1, 2, 1, 0));
        setBackground(Color.GRAY);

        setPreferredSize(
                new Dimension(PAGE_WIDTH, PAGE_HEIGHT)
        );

        setMinimumSize(
                new Dimension(PAGE_WIDTH, PAGE_HEIGHT)
        );

        setMaximumSize(
                new Dimension(PAGE_WIDTH, PAGE_HEIGHT)
        );

        leftSection = new A5SectionPanel();
        rightSection = new A5SectionPanel();

        add(leftSection);
        add(rightSection);
    }

    public A5SectionPanel getLeftSection() {
        return leftSection;
    }

    public A5SectionPanel getRightSection() {
        return rightSection;
    }
}