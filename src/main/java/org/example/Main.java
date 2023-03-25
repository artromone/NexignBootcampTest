package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

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

    private static void printReport(String number, ArrayList<String[]> entries) throws ParseException {
        System.out.println(
                "Tariff index: " + "00" + "\n" +
                        "----------------------------------------------------------------------------\n" +
                        "Report for phone number " + number + ":\n" +
                        "----------------------------------------------------------------------------\n" +
                        "| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n" +
                        "----------------------------------------------------------------------------\n"
        );

        for (String[] entry : entries) {

            String callType = entry[0];
            String startTime = parseTime(entry[2]);
            String endTime = parseTime(entry[3]);
            String tariffIndex = entry[4];

            if (entry[1].equals(number)) {
                System.out.println("|     " + callType + "    | " + startTime + " | " + endTime + " | ________ |  ____ |");
            }
        }

        System.out.println(
                "----------------------------------------------------------------------------\n" +
                        "|                                           Total Cost: |     _____ rubles |\n" +
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

        ArrayList<String[]> entries = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();

        while (sc.hasNext()) {
            String[] entry = sc.nextLine().split(", ");

            String number = entry[1];
            if (!numbers.contains(number)) {
                numbers.add(entry[1]);
            }

            entries.add(entry);
        }

        for (String number : numbers) {
            try {

                if (number.equals("79270106185")) printReport(number, entries);

            } catch (ParseException e) {
                System.out.println("Print error");
                System.exit(1);
            }
        }
    }
}