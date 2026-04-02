package API;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class Print {

    public void PrintFrame(JPanel notesPanel) {

        PrinterJob job = PrinterJob.getPrinterJob();

        PageFormat pf = job.defaultPage();
        pf.setOrientation(PageFormat.PORTRAIT); // change if you want LANDSCAPE

        // 🔥 VERY IMPORTANT: ensure correct size before printing
        notesPanel.setSize(notesPanel.getPreferredSize());

        job.setPrintable(new Printable() {

            @Override
            public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

                Graphics2D g2d = (Graphics2D) g;

                double pageHeight = pageFormat.getImageableHeight();
                double panelHeight = notesPanel.getHeight();

                // calculate total pages
                int totalPages = (int) Math.ceil(panelHeight / pageHeight);

                if (pageIndex >= totalPages) {
                    return NO_SUCH_PAGE;
                }

                // move to printable area
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // move view for current page
                g2d.translate(0, -pageIndex * pageHeight);

                // print panel
                notesPanel.printAll(g2d);

                return PAGE_EXISTS;
            }
        }, pf);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
