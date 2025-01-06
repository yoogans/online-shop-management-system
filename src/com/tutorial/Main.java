package com.tutorial;
import java.io.IOException;
import java.util.Scanner;

import CRUD.*;


public class Main {
    public static void main(String[] args) throws IOException {

        Scanner termimalInput = new Scanner(System.in);
        String userOption;

        boolean isContinue = true;
        while (isContinue) {
            Utility.clearScreen();

            System.out.println("-".repeat(20) + "\nToko Online\n" + "-".repeat(20));
            System.out.println("1.\tLihat daftar Product");
            System.out.println("2.\tCari product");
            System.out.println("3.\tTambah product");
            System.out.println("4.\tUbah Product");
            System.out.println("5.\tHapus Product (Mantenance)");

            System.out.print("\nMasukkan pilihan: ");
            userOption = termimalInput.next();

            switch (userOption) {
                case "1":
                    System.out.println("=".repeat(15));
                    System.out.println("LIST PRODUCT");
                    System.out.println("=".repeat(15));
                    Operation.showData();
                    break;
                case "2":
                    System.out.println("=".repeat(15));
                    System.out.println("Cari product");
                    System.out.println("=".repeat(15));
                    Operation.searchData();
                    break;
                case "3":
                    System.out.println("=".repeat(15));
                    System.out.println("tambah Product");
                    System.out.println("=".repeat(15));
                    Operation.tambahData();
                    Operation.showData();
                    break;
                case "4":
                    System.out.println("=".repeat(15));
                    System.out.println("Ubah Product");
                    System.out.println("=".repeat(15));
                    Operation.updateData();
                    break;
                case "5":
                    System.out.println("=".repeat(15));
                    System.out.println("Hapus Product");
                    System.out.println("=".repeat(15));
                    Operation.deleteData();
                    break;
                default:
                    System.err.println("Opsi tidak di temukan\nMasukkan opsi (1-5)");
            }

            isContinue = CRUD.Utility.getQuest("\nLanjutkan");
        }

    }




}


