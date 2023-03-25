package org.example;

import java.io.*;
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
        StringBuilder stringBuilder = new StringBuilder(
                "Tariff index: " + "00" + "\n" +
                        "----------------------------------------------------------------------------\n" +
                        "Report for phone number " + number + ":\n" +
                        "----------------------------------------------------------------------------\n" +
                        "| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n" +
                        "----------------------------------------------------------------------------\n\n"
        );

        for (String[] entry : entries) {

            String callType = entry[0];
            String startTime = parseTime(entry[2]);
            String endTime = parseTime(entry[3]);
            String tariffIndex = entry[4];

            long durationL = calcDuration(entry[2], entry[3]);
            String durationS = String.format("%d:%02d:%02d", durationL / 3600, (durationL % 3600) / 60, (durationL % 60));

            if (entry[1].equals(number)) {
                stringBuilder.append("|     " + callType + "    | " + startTime + " | " + endTime + " | " + durationS + " |  " + calcCost(tariffIndex, durationL) + " |\n");
            }
        }

        stringBuilder.append(
                "----------------------------------------------------------------------------\n" +
                        "|                                           Total Cost: |     _____ rubles |\n" +
                        "----------------------------------------------------------------------------\n\n"
        );

        try {
            new File("reports").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter("reports\\" + number + ".txt"));
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String calcCost(String tariffIndex, long durationL) throws ParseException {
        if (tariffIndex.equals("06")) {
            if (durationL < 300) {
                return "1.00";
            } else {
                return "1.00";
            }
        }
        if (tariffIndex.equals("11")) {
            if (durationL < 100) {
                return "0.50";
            } else {
                return "1.50";
            }
        }
        if (tariffIndex.equals("03")) {
            return "1.50";
        }
        throw new ParseException("Calc cost error", 0);
    }

    private static long calcDuration(String startTime, String endTime) {
        return Long.parseLong(endTime) - Long.parseLong(startTime);
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

                printReport(number, entries);

            } catch (ParseException e) {
                System.out.println("Print error");
                System.exit(1);
            }
        }
    }
}