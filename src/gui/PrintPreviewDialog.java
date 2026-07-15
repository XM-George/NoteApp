package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PrintPreviewDialog extends JDialog {

    private final BufferedImage pageImage;
    private final Runnable printAction;

    public PrintPreviewDialog(
            JFrame owner,
            BufferedImage pageImage,
            Runnable printAction
    ) {
        super(owner, "Print Preview", true);

        this.pageImage = pageImage;
        this.printAction = printAction;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setMinimumSize(new Dimension(700, 550));
        setLocationRelativeTo(owner);

        initialiseUI();
    }

    private void initialiseUI() {
        setLayout(new BorderLayout());

        PreviewPanel previewPanel =
                new PreviewPanel(pageImage);

        JScrollPane scrollPane =
                new JScrollPane(previewPanel);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(
                new Color(65, 65, 65)
        );

        JButton printButton =
                new JButton("Print");

        JButton closeButton =
                new JButton("Close");

        printButton.addActionListener(event -> {
            dispose();
            printAction.run();
        });

        closeButton.addActionListener(
                event -> dispose()
        );

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        8,
                        8,
                        8
                )
        );

        buttonPanel.add(closeButton);
        buttonPanel.add(printButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private static class PreviewPanel extends JPanel {

        private static final int PREVIEW_PADDING = 40;

        private final BufferedImage image;

        public PreviewPanel(BufferedImage image) {
            this.image = image;

            setBackground(new Color(65, 65, 65));

            setPreferredSize(
                    new Dimension(
                            image.getWidth() + PREVIEW_PADDING * 2,
                            image.getHeight() + PREVIEW_PADDING * 2
                    )
            );
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D graphics2D =
                    (Graphics2D) graphics.create();

            try {
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC
                );

                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int availableWidth =
                        getWidth() - PREVIEW_PADDING * 2;

                int availableHeight =
                        getHeight() - PREVIEW_PADDING * 2;

                double scaleX =
                        (double) availableWidth / image.getWidth();

                double scaleY =
                        (double) availableHeight / image.getHeight();

                /*
                 * Δεν μεγαλώνουμε την εικόνα πάνω από
                 * το πραγματικό της μέγεθος.
                 */
                double scale = 1.0;

                int drawWidth =
                        (int) Math.round(image.getWidth() * scale);

                int drawHeight =
                        (int) Math.round(image.getHeight() * scale);

                int x = (getWidth() - drawWidth) / 2;
                int y = (getHeight() - drawHeight) / 2;

                /*
                 * Μικρή σκιά πίσω από το χαρτί.
                 */
                graphics2D.setColor(
                        new Color(0, 0, 0, 90)
                );

                graphics2D.fillRect(
                        x + 8,
                        y + 8,
                        drawWidth,
                        drawHeight
                );

                graphics2D.drawImage(
                        image,
                        x,
                        y,
                        drawWidth,
                        drawHeight,
                        null
                );

            } finally {
                graphics2D.dispose();
            }
        }
    }
}