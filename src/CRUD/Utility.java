package CRUD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {
    public static boolean isString(String input) {
        if (input == null) {
            return false; // Jika input null, return false
        }
        return input.matches("^[a-zA-Z\\s]+$");
    }

    protected static String formatTahun() throws IOException {
        Scanner terminalInput = new Scanner(System.in);
        String tahunInput = terminalInput.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        boolean tahunValid = false;
        while (!tahunValid) {
            try {
                Year.parse(tahunInput, formatter);
                tahunValid = true;
            } catch (Exception e) {
                System.err.println("Format tahun tidak valid");
                System.out.print("\nSilahkan masukkan tahun dengan benar: ");
                tahunInput = terminalInput.nextLine();
                tahunValid = false;
            }
        }

        return tahunInput;
    }

    protected static boolean cekProductDb(String[] keywords, boolean isDisplay) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);
        String data = bufferInput.readLine();
        boolean isExists = false;
        int noData = 0;


        if (isDisplay) {
            System.out.println("\n| No |\ttahun |\tbrand     |\tStore           |\tKategori");
            System.out.println("-".repeat(60));
        }

        while (data != null) {
            isExists = true;
            for (String keyword : keywords) {
                isExists = isExists && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if (isExists) {
                if (isDisplay) {
                    noData++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d ", noData);
                    System.out.printf("| %4s  ", stringToken.nextToken());
                    System.out.printf("|\t%-7s  ", stringToken.nextToken());
                    System.out.printf("|\t%-13s   ", stringToken.nextToken());
                    System.out.printf("|\t%-13s  ", stringToken.nextToken());
                    System.out.println("");
                } else {
                    break;
                }
            }
            data = bufferInput.readLine();
        }
        if (isDisplay) {
            if (noData == 0) {
                System.out.println("Data tidak ditemukan.");
            }
            System.out.println("-".repeat(60));
        }
        return isExists;

    }

    public static boolean getQuest(String message) {
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n" + message + " (y/n)?");
        String userOption = terminalInput.next();

        while (!userOption.equalsIgnoreCase("y") && !userOption.equalsIgnoreCase("n")) {
            System.err.println("Pilihan anda bukan y atau n");

            System.out.print("\n" + message + " (y/n)? \n");
            userOption = terminalInput.next();
        }

        return userOption.equalsIgnoreCase("y");

    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.println("\033\143");
            }
        } catch (Exception e) {
            System.err.println("Tidak bisa cleear screen");
        }
    }
}
