package com.jp.calculate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {

    /** 
     * @param args 実行構成引数から受け取り
     * */
    public static void main(String[] args) {
	
	if (args.length < 1) {
            System.out.println("使用方法: java Main <ファイル名>");
            return;
        }

        // プロジェクトのルートからの相対パスでファイル名を取得
        String filename = args[0];
        SalesManager manager = new SalesManager();
        Scanner scanner = new Scanner(System.in);

        // 初期データのロード
        try {
            // プロジェクトのルートディレクトリのパスを取得
            Path projectRootPath = Paths.get("").toAbsolutePath().normalize();
            // sales_data.txt の相対パスを絶対パスに変換
            Path filePath = projectRootPath.resolve("src").resolve(filename);
            manager.loadSalesRecordsFromFile(filePath.toString());
        } catch (IOException e) {
            System.out.println("データのロードに失敗しました: " + e.getMessage());
        }

        while (true) {
            System.out.println("1: 売上データの追加");
            System.out.println("2: 売上データの表示");
            System.out.println("3: 日別売上集計");
            System.out.println("4: 商品別売上集計");
            System.out.println("5: 全期間売上合計");
            System.out.println("6: 終了");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("商品名:");
                String productName = scanner.nextLine();
                System.out.println("数量:");
                int quantity = scanner.nextInt();
                System.out.println("単価:");
                double price = scanner.nextDouble();
                System.out.println("日付 (YYYY-MM-DD):");
                LocalDate date = LocalDate.parse(scanner.next());

                SalesRecord record = new SalesRecord(productName, quantity, price, date);
                manager.addSalesRecord(record);

            } else if (choice == 2) {
                manager.displaySalesRecords();

            } else if (choice == 3) {
                System.out.println(manager.calculateDailySales());

            } else if (choice == 4) {
                System.out.println(manager.calculateProductSales());

            } else if (choice == 5) {
                System.out.println(manager.calculateTotalSales());

            } else if (choice == 6) {
                // 終了前にデータを保存
                try {
                    Path projectRootPath = Paths.get("").toAbsolutePath().normalize();
                    Path filePath = projectRootPath.resolve("src").resolve(filename);
                    manager.saveSalesRecordsToFile(filePath.toString());
                } catch (IOException e) {
                    System.out.println("データの保存に失敗しました。");
                }
                break;
            }
        }
        scanner.close();
    }
}
