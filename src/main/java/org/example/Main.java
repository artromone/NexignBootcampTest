package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            parseDataRecord(fin);
        } catch (IOException e) {
            System.out.println("Parse error");
            System.exit(1);
        }
        //printReport();
    }

    private static void printReport() {
        System.out.println("Hello world!");
    }

    private static void parseDataRecord(FileInputStream fin) throws IOException {
        Scanner sc = new Scanner(fin);

        String number;
        String callType;
        String startTime;
        String endTime;
        String fareType;

        while (sc.hasNext()) {
            String[] array = sc.nextLine().split(", ");
            callType = array[0];
            number = array[1];
            startTime = array[2];
            endTime = array[3];
            fareType = array[4];

            System.out.println(number + " " + endTime);
        }
    }
}