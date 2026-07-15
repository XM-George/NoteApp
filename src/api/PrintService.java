package api;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class PrintService {

    public void printComponent(
            Component component,
            Component parentComponent
    ) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        PageFormat pageFormat = createA4LandscapePageFormat(
                printerJob
        );

        printerJob.setPrintable(
                (graphics, format, pageIndex) ->
                        printPage(
                                component,
                                graphics,
                                format,
                                pageIndex
                        ),
                pageFormat
        );

        boolean printAccepted =
                printerJob.printDialog();

        if (!printAccepted) {
            return;
        }

        try {
            printerJob.print();
        } catch (PrinterException exception) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Printing failed:\n"
                            + exception.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private PageFormat createA4LandscapePageFormat(
            PrinterJob printerJob
    ) {
        PageFormat pageFormat =
                printerJob.defaultPage();

        Paper paper = new Paper();

        /*
         * Οι μονάδες της Java Print API είναι points.
         * 1 inch = 72 points.
         *
         * A4:
         * 210 × 297 mm
         * περίπου 595 × 842 points.
         */
        double a4Width = 595.28;
        double a4Height = 841.89;

        paper.setSize(a4Width, a4Height);

        /*
         * Περιθώριο περίπου 10 mm.
         * 10 mm ≈ 28.35 points.
         */
        double margin = 28.35;

        paper.setImageableArea(
                margin,
                margin,
                a4Width - 2 * margin,
                a4Height - 2 * margin
        );

        pageFormat.setPaper(paper);
        pageFormat.setOrientation(
                PageFormat.LANDSCAPE
        );

        return printerJob.validatePage(pageFormat);
    }

    private int printPage(
            Component component,
            Graphics graphics,
            PageFormat pageFormat,
            int pageIndex
    ) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D graphics2D =
                (Graphics2D) graphics.create();

        try {
            double componentWidth =
                    component.getWidth();

            double componentHeight =
                    component.getHeight();

            if (componentWidth <= 0
                    || componentHeight <= 0) {
                return Printable.NO_SUCH_PAGE;
            }

            double imageableWidth =
                    pageFormat.getImageableWidth();

            double imageableHeight =
                    pageFormat.getImageableHeight();

            double scaleX =
                    imageableWidth / componentWidth;

            double scaleY =
                    imageableHeight / componentHeight;

            double scale =
                    Math.min(scaleX, scaleY);

            double printedWidth =
                    componentWidth * scale;

            double printedHeight =
                    componentHeight * scale;

            /*
             * Κεντράρουμε το A4 panel μέσα στο
             * εκτυπώσιμο μέρος του χαρτιού.
             */
            double offsetX =
                    pageFormat.getImageableX()
                            + (imageableWidth - printedWidth) / 2;

            double offsetY =
                    pageFormat.getImageableY()
                            + (imageableHeight - printedHeight) / 2;

            graphics2D.translate(offsetX, offsetY);
            graphics2D.scale(scale, scale);

            /*
             * Καλύτερη ποιότητα για κείμενο και γραμμές.
             */
            graphics2D.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            graphics2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            /*
             * printAll αντί για paint, ώστε να τυπωθούν
             * σωστά όλα τα Swing child components.
             */
            component.printAll(graphics2D);

            return Printable.PAGE_EXISTS;

        } finally {
            graphics2D.dispose();
        }
    }
}