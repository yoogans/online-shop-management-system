package CRUD;

import jdk.jshell.execution.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operation {
    public static void showData() throws IOException {
        FileReader fileInput;
        BufferedReader bufferedInput;
        try {
            fileInput = new FileReader("database.txt");
            bufferedInput = new BufferedReader(fileInput);
        } catch (Exception e) {
            System.err.println("\nDatabase tidak di temukan!");
            System.err.println("Silahkan buat terlebih dahulu");
            tambahData();
            return;
        }
        String data = bufferedInput.readLine();

        System.out.println("\n| No |\ttahun |\tbrand                |\tStore          |\tcategory");
        System.out.println("-".repeat(80));

        if (data != null) {
            int noData = 0;
            while (data != null) {
                noData++;
                StringTokenizer stringToken = new StringTokenizer(data, ",");

                stringToken.nextToken();
                System.out.printf("| %2d ", noData);
                System.out.printf("| %4s  ", stringToken.nextToken());
                System.out.printf("|\t%-19s  ", stringToken.nextToken());
                System.out.printf("|\t%-13s   ", stringToken.nextToken());
                System.out.printf("|\t%-13s  ", stringToken.nextToken());
                System.out.println("");
                data = bufferedInput.readLine();

            }
        } else {
            System.out.println("data tidak ada");
        }

        System.out.println("-".repeat(80));

    }

    public static void updateData() throws IOException {
        // Membaca database
        File database = new File("database.txt");
        if (!database.exists() || database.length() == 0) {
            System.err.println("Database kosong, silahkan tambahkan data terlebih dahulu");
            tambahData();
            return;
        }

        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);


        // Membuat database sementara
        File temptDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(temptDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Menampilkan data
        System.out.println("List Product");
        showData();

        String data = bufferInput.readLine();
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukkan nomor yang ingin di update: ");
        int updateNum = terminalInput.nextInt();

        int entryNum = 0;
        boolean isDataFound = false;

        while (data != null) {
            entryNum++;
            StringTokenizer st = new StringTokenizer(data, ",");

            if (updateNum == entryNum) {
                isDataFound = true;
                System.out.println("Data yang ingin di update");
                System.out.println("-".repeat(50));
                System.out.println("Referensi           :     " + st.nextToken());
                System.out.println("Tahun               :     " + st.nextToken());
                System.out.println("Product             :     " + st.nextToken());
                System.out.println("Brand               :     " + st.nextToken());
                System.out.println("Category            :     " + st.nextToken());

                String[] dataArr = {"Tahun", "Product", "brand", "category"};
                String[] temptData = new String[4];
                st = new StringTokenizer(data, ",");
                st.nextToken();

                for (int i = 0; i < dataArr.length; i++) {
                    boolean isUpdate = Utility.getQuest("Apakah anda ingin mengubah: " + dataArr[i]);
                    String originalData = st.nextToken();

                    if (isUpdate) {
                        if (dataArr[i].equalsIgnoreCase("tahun")) {
                            System.out.print("Masukkan tahun: ");
                            temptData[i] = Utility.formatTahun();
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("Masukkan " + dataArr[i] + " baru: ");
                            temptData[i] = terminalInput.nextLine();
                        }
                    } else {
                        temptData[i] = originalData;
                    }
                }

                System.out.println("Data yang ingin di ubah");
                System.out.println("-".repeat(50));
                System.out.println("Tahun               :     " + st.nextToken() + " -----> " + temptData[0]);
                System.out.println("Product             :     " + st.nextToken() + " -----> " + temptData[1]);
                System.out.println("Brand               :     " + st.nextToken() + " -----> " + temptData[2]);
                System.out.println("Category            :     " + st.nextToken() + " -----> " + temptData[3]);

                boolean isUpdate = Utility.getQuest("Apakah anda ingin update data tersebut?");
                if (isUpdate) {
                    System.out.println("Success Update data!");
                    boolean isExist = Utility.cekProductDb(temptData, false);

                    if (isExist) {
                        System.err.println("Data product sudah ada, update gagal!");
                        bufferedOutput.write(data);
                    } else {
                        long noEntry = noEntryPertahun(temptData[1], temptData[0]) + 1;
                        String namaProductTanpaSpasi = temptData[1].replaceAll("\\s+", "");
                        String primary = namaProductTanpaSpasi + "_" + temptData[0] + "_" + noEntry;
                        bufferedOutput.write(primary + "," + temptData[0] + "," + temptData[1] + "," + temptData[2] + "," + temptData[3]);
                    }
                } else {
                    bufferedOutput.write(data);
                }
            } else {
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();
            data = bufferInput.readLine();
        }

        if (!isDataFound) {
            System.out.println("Nomor tidak ditemukan, silakan coba lagi.");
        }

        bufferedOutput.flush();
        fileInput.close();
        bufferInput.close();
        fileOutput.close();
        bufferedOutput.close();
        System.gc();

        // Hapus database lama
        String filename = "database.txt";
        try {
            Files.delete(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename
        Path source = Paths.get("tempDB.txt");
        Path target = Paths.get("database.txt");

        try {
            Files.move(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void deleteData() throws IOException {
        // Membaca database asli
        File database = new File("database.txt");
        if (database.exists() || database.length() == 0) {
            System.out.println("Database tidak di temukan, silahkan buat data terlebih dahulu!");
            tambahData();
            return;
        }
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // membuat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tapilkan data
        System.out.println("List Product");
        showData();

        // ambil user input untuk delete data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukkan nomor produk yang akan di hapus: ");
        int deleteNum = terminalInput.nextInt();


        // looping untuk membaca tiap data baris dan skip data yang akan didelete

        int noEntry = 0;
        String data = bufferedInput.readLine();

        while (data != null) {
            noEntry++;
            boolean isDelete = false;
            StringTokenizer st = new StringTokenizer(data, ",");

            if (deleteNum == noEntry) {
                System.out.println("data yang ingin di hapus");
                System.out.println("-".repeat(50));
                System.out.println("Referensi           :     " + st.nextToken());
                System.out.println("Tahun               :     " + st.nextToken());
                System.out.println("Product             :     " + st.nextToken());
                System.out.println("Brand               :     " + st.nextToken());
                System.out.println("Category            :     " + st.nextToken());
                isDelete = Utility.getQuest("Apakah anda ingin menghapus data");
            }

            if (isDelete) {
                System.out.println("Data berhasil di hapus");
            } else {
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        bufferedOutput.flush();
        fileInput.close();
        bufferedInput.close();
        fileOutput.close();
        bufferedOutput.close();
        System.gc();

//         Delete
        String filename = "database.txt";
        try {
            Files.delete(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename
        Path source = Paths.get("tempDB.txt");
        Path target = Paths.get("database.txt");

        try {
            Files.move(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getValidInput(Scanner scanner, String prompt) {
        String input = "";

        while (input.isEmpty()) {
            System.out.println(prompt);
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.err.println("Input tidak boleh kosong!");
                System.out.print("Masukkan data: ");
                input = scanner.nextLine().trim();

            }
        }

        return input;
    }

    public static void tambahData() throws IOException {
        FileWriter fileOutput = new FileWriter("database.txt", true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        Scanner terminalInput = new Scanner(System.in);
        String nama_product, store, kategori, tahun;

        System.out.print("\n\nNama Product: ");
        nama_product = terminalInput.nextLine();
        System.out.print("Store: ");
        store = terminalInput.nextLine();
        System.out.print("Kategori: ");
        kategori = terminalInput.nextLine();
        System.out.print("tahun: ");
        tahun = Utility.formatTahun();


        String[] keywords = {tahun + "," + nama_product + "," + store + "," + kategori};
        boolean isExists = Utility.cekProductDb(keywords, false);

        if (Utility.isString(nama_product) && (Utility.isString(store) && (Utility.isString(kategori)))) {
            System.out.println(Arrays.toString(keywords));
            if (!isExists) {
                //phone002_2023_1,2023,Samsung,TechStore,Electronics


                long noEntry = noEntryPertahun(nama_product, tahun) + 1;

                String namaProductTanpaSpasi = nama_product.replaceAll("\\s+", "");
                String primary = namaProductTanpaSpasi + "_" + tahun + "_" + noEntry;
                System.out.println("Data yang anda masukkan");
                System.out.println("-".repeat(30));
                System.out.println("Primary Key  : " + primary);
                System.out.println("Tahun        : " + tahun);
                System.out.println("Nama Product : " + nama_product);
                System.out.println("Store        : " + store);
                System.out.println("Kategori     : " + kategori);
                System.out.println("-".repeat(30));


                if (!tahun.equalsIgnoreCase("tahun")) {
                    boolean isTambah = Utility.getQuest("apakah anda ingin menambah data? ");
                    if (isTambah) {
                        bufferOutput.write(primary + "," + tahun + "," + nama_product + "," + store + "," + kategori);
                        bufferOutput.newLine();
                        bufferOutput.flush();
                    }
                } else {
                    System.err.println("Tidak bisa menambah data!");
                }

            } else {
                System.out.println("\nData yang anda masukkan sudah tersedia dengan data berikut");
                Utility.cekProductDb(keywords, true);
            }
        } else {
            System.err.println("Format harus string!");
        }
        bufferOutput.close();
    }

    public static long noEntryPertahun(String nama_product, String tahun) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while (data != null) {
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            nama_product = nama_product.replaceAll("\\s+", "");
            if (nama_product.equalsIgnoreCase(dataScanner.next()) && tahun.equalsIgnoreCase(dataScanner.next())) {
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        }
        return entry;

    }

    public static void searchData() throws IOException {
        // Check database exists or not
        try {
            File file = new File("database.txt");
        } catch (Exception e) {
            System.err.println("Database tidak di temukan");
            System.err.println("Silahkan buat terlebih dahulu");
            tambahData();
            return;
        }

//         Get keyword from user
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Cari data product:");
        String cariString = terminalInput.nextLine();
        String[] keywords = cariString.split("\\s+");

        // Check keyword from database
        Utility.cekProductDb(keywords, true);

    }
}
