package API;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class Print {

    public void PrintFrame(JPanel notesPanel) {

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        pf.setOrientation(PageFormat.PORTRAIT);

        notesPanel.setSize(notesPanel.getPreferredSize());
        notesPanel.validate();

        job.setPrintable(new Printable() {

            @Override
            public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

                if (pageIndex > 0)
                {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                double scaleX = pageFormat.getImageableWidth() / notesPanel.getWidth();
                double scaleY = pageFormat.getImageableHeight() / notesPanel.getHeight();

                g2d.scale(scaleX, scaleY);

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
