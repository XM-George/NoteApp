package API;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Print {
    public void PrintFrame(JPanel notesPanel)
    {
        PrinterJob job = PrinterJob.getPrinterJob();

        PageFormat pf = job.defaultPage();
        pf.setOrientation(PageFormat.LANDSCAPE); // or PORTRAIT

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                if (pageIndex > 0)
                {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;

                g2d.translate(pf.getImageableX(), pf.getImageableY());

                // scale to fit the page
                double scaleX = pf.getImageableWidth() / notesPanel.getWidth();
                double scaleY = pf.getImageableHeight() / notesPanel.getHeight();
                System.out.println("ImageableWidth:" + pf.getImageableWidth());
                System.out.println("ImageableHeight:" + pf.getImageableHeight());
                System.out.println("notePanelWidth:" + notesPanel.getWidth());
                System.out.println("notePanel Height:" + notesPanel.getHeight());
                pf.setOrientation(PageFormat.LANDSCAPE);
                double scale = Math.min(scaleX, scaleY);
                //g2d.scale(scale, scale);

                // print the panel as it appears on screen
                notesPanel.printAll(g2d);

                return Printable.PAGE_EXISTS;
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
