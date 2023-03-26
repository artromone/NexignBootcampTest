package org.example;

import java.io.*;
import java.text.DecimalFormat;
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
                "----------------------------------------------------------------------------\n"
        );

        double totalCost = 0;
        long totalDuration = 0;
        boolean connectedUnlimited = false;

        for (String[] entry : entries) {

            String callType = entry[0];
            String startTime = parseTime(entry[2]);
            String endTime = parseTime(entry[3]);
            String tariffIndex = entry[4];

            if (entry[1].equals(number)) {
                connectedUnlimited = tariffIndex.equals("06");

                long durationL = calcDuration(entry[2], entry[3]);
                totalDuration += durationL;
                double cost = calcCost(tariffIndex, totalDuration);
                if (tariffIndex.equals("11") && callType.equals("02")) {
                    cost = 0;
                }
                totalCost += cost;

                String durationS = String.format("%02d:%02d:%02d", durationL / 3600, (durationL % 3600) / 60, (durationL % 60));

                stringBuilder.append("|     " + callType + "    | " + startTime + " | " + endTime + " | " + durationS + " |  " +
                        new DecimalFormat("#0.00").format(cost) + " |\n");
            }
        }

        stringBuilder.append(
                "----------------------------------------------------------------------------\n" +
                "|                                           Total Cost: |     " +
                new DecimalFormat("#0.00").format(calcTotalCost(totalCost, connectedUnlimited)) + " rubles |\n" +
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

    private static double calcTotalCost(double totalCost, boolean connectedUnlimited) {
        if (connectedUnlimited) {
            return totalCost + 100;
        }
        return totalCost;
    }

    private static double calcCost(String tariffIndex, long totalDuration) throws ParseException {
        if (tariffIndex.equals("06")) {
            if (totalDuration < 300) {
                return 0;
            } else {
                return 1;
            }
        }
        if (tariffIndex.equals("11")) {
            if (totalDuration < 100) {
                return 0.5;
            } else {
                return 1.5;
            }
        }
        if (tariffIndex.equals("03")) {
            return 1.5;
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