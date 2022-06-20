package fr.aprevot.grandoralcrypto;

import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.aprevot.grandoralcrypto.utils.Status;
import fr.aprevot.grandoralcrypto.utils.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main{

    public static void main(String[] args) {
        try {



            // Creating pdf report
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("gdoral-report-" + formatter.format(LocalDateTime.now()) + ".pdf"));
            Table table = new Table();

            SpreadSheet data = new SpreadSheet(new File("java_ods.ods"));

            DecimalFormat df = new DecimalFormat("###.#####");

            Sheet sheet = data.getSheet(0);

            double opening = 0;
            double closing = 0;
            int priceDown = 0;
            int priceDownTime = 0;

            double evolution = 0;

            boolean isDown = false;
            boolean previousDown = false;

            List<Status> statuses = new LinkedList<>();
            List<Integer> numbers = new LinkedList<>();
            List<Integer> consecutiveTimes = new LinkedList<>();
            List<Double> priceEvolution = new LinkedList<>();


            for (int i = 1; i < sheet.getMaxRows(); i++) {
                opening = Double.parseDouble(sheet.getRange(i, 1).getValue().toString());
                closing = Double.parseDouble(sheet.getRange(i, 4).getValue().toString());

                evolution = (closing - opening) / opening * 100;

                System.out.printf("%-32s %s\n", "Date :", sheet.getRange(i, 0).getValue().toString());
                System.out.printf("%-32s %s %s\n", "Price evolution :" , evolution > 0 ? "+" + df.format(evolution) : df.format(evolution), "%");

                if(opening > closing) {
                    isDown = true;
                } else {
                    isDown = false;
                }

                double absEvolution = Math.abs(evolution);
                System.out.printf("%-32s %s\n", "AbsEvolution : ", absEvolution);

                if(absEvolution < 5) {
                    System.out.printf("%-32s %s\n", "Skipped : ", "Evolution was below 5%");
                    System.out.println("\n");
                    continue;
                }

                if (isDown) {

                    if (previousDown) {
                        statuses.add(Status.STILL_DOWN);
                    } else {
                        statuses.add(Status.DOWN);
                    }

                    priceDown = priceDown + 1;
                    priceDownTime = priceDownTime + 1;
                    System.out.printf("%-32s %s\n", "Price went down :", priceDownTime + " consecutive times.");
                    previousDown = true;
                } else {

                    if (priceDownTime > 0) {
                        consecutiveTimes.add(priceDownTime);
                    }
                    if (previousDown) {
                        statuses.add(Status.UP);
                    } else {
                        statuses.add(Status.STILL_UP);
                    }
                    priceDownTime = 0;
                    previousDown = false;

                }
                System.out.println("\n");
                priceEvolution.add(evolution);

            }

            numbers.add(priceDown);
            numbers.add(Collections.frequency(statuses, Status.DOWN));
            numbers.add(Collections.frequency(statuses, Status.STILL_DOWN));
            numbers.add(Collections.frequency(statuses, Status.UP));
            numbers.add(Collections.frequency(statuses, Status.STILL_UP));


            document.open();

            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph paragraph = new Paragraph("Report " + formatter.format(LocalDateTime.now()), catFont);

            document.add(paragraph);
            document.add(new Paragraph(" "));

            PdfPTable pdfPTable = new PdfPTable(5);
            table.createTable(pdfPTable, numbers);
            document.add(pdfPTable);

            document.add(new Paragraph("Price going down"));
            document.add(new Paragraph("---------------------------------"));
            document.add(new Paragraph("Max : " + Collections.max(consecutiveTimes)));
            document.add(new Paragraph("Min : " + Collections.min(consecutiveTimes)));
            document.add(new Paragraph("Average : " + consecutiveTimes.stream().mapToInt(val -> val).average().orElse(0)));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Price evolution"));

            List<Double> negEvolutions = new LinkedList<>();
            List<Double> posEvolutions = new LinkedList<>();

            priceEvolution.stream().forEach(i -> (i < 0 ? negEvolutions : posEvolutions).add(i));

            document.add(new Paragraph("---------------------------------"));
            document.add(new Paragraph("Global average : " + priceEvolution.stream().mapToDouble(val -> val).average().orElse(0.0)));
            document.add(new Paragraph("------"));
            document.add(new Paragraph("Lower down : " + Collections.min(negEvolutions)));
            document.add(new Paragraph("Higher down : " + Collections.max(negEvolutions)));
            document.add(new Paragraph("Average down : " + negEvolutions.stream().mapToDouble(value -> value).average().orElse(0.0)));
            document.add(new Paragraph("------"));
            document.add(new Paragraph("Lower up : " + Collections.min(posEvolutions)));
            document.add(new Paragraph("Higher up : " + Collections.max(posEvolutions)));
            document.add(new Paragraph("Average up : " + posEvolutions.stream().mapToDouble(value -> value).average().orElse(0.0)));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total entries : 624"));


            document.close();

            System.out.println("\nPrice went down " + priceDown + " times.");



        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }



}
