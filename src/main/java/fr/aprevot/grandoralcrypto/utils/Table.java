package fr.aprevot.grandoralcrypto.utils;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;

public class Table {

    public void createTable(PdfPTable pdfPTable, List<Integer> times) {
        PdfPCell cell1 = new PdfPCell(new Phrase("Prix en baisse"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("DOWN"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("STILL_DOWN"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("UP"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("STILL_UP"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPTable.addCell(cell1);

        pdfPTable.setHeaderRows(1);

        for(int i : times) {
            pdfPTable.addCell(String.valueOf(i));
        }

    }

}
