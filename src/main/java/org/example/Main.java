package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static javax.xml.bind.DatatypeConverter.parseDate;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("No file provided");
            System.exit(1);
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(args[0]);
        } catch (FileNotFoundException exc) {
            System.out.println("File not found");
            System.exit(1);
        }

        try {
            readRecords(fin);
        } catch (IOException e) {
            System.out.println("Parse error");
            System.exit(1);
        }
    }

    private static void printReport(String[] array) throws ParseException {
        String callType = array[0];
        String number = array[1];
        String startTime = parseTime(array[2]);
        String endTime = parseTime(array[3]);
        String tariffIndex = array[4];

        System.out.println(
                "Tariff index: " + tariffIndex + "\n" +
                        "----------------------------------------------------------------------------\n" +
                        "Report for phone number " + number + ":\n" +
                        "----------------------------------------------------------------------------\n" +
                        "| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n" +
                        "----------------------------------------------------------------------------\n" +
                        "|     01    | 2023-02-03 05:55:06 | 2023-02-03 06:02:49 | 00:07:43 |  5.00 |\n" +
                        "|     01    | 2023-05-23 21:37:39 | 2023-05-23 21:45:27 | 00:07:48 |  5.00 |\n" +
                        "|     01    | 2023-08-24 06:16:23 | 2023-08-24 06:27:35 | 00:11:12 |  5.00 |\n" +
                        "|     01    | 2023-09-08 09:38:34 | 2023-09-08 09:46:13 | 00:07:39 |  1.50 |\n" +
                        "|     01    | 2023-12-17 20:39:29 | 2023-12-17 20:47:18 | 00:07:49 |  5.00 |\n" +
                        "|     02    | 2023-01-17 11:08:27 | 2023-01-17 11:17:41 | 00:09:14 |  5.00 |\n" +
                        "|     02    | 2023-03-17 17:52:45 | 2023-03-17 17:56:11 | 00:03:26 |  1.50 |\n" +
                        "|     02    | 2023-10-11 14:00:17 | 2023-10-11 14:10:19 | 00:10:02 |  5.00 |\n" +
                        "----------------------------------------------------------------------------\n" +
                        "|                                           Total Cost: |     33.00 rubles |\n" +
                        "----------------------------------------------------------------------------\n\n"
        );
    }

    private static String parseTime(String input) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = parser.parse(input);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    private static void readRecords(FileInputStream fin) throws IOException {
        Scanner sc = new Scanner(fin);
        while (sc.hasNext()) {
            String[] array = sc.nextLine().split(", ");
            try {
                printReport(array);
            } catch (ParseException e) {
                System.out.println("Print error");
                System.exit(1);
            }
        }
    }
}