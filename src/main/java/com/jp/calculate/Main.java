package com.jp.calculate;

import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jp.calculate.model.SalesRecord;
import com.jp.calculate.service.SalesManager;
import com.jp.calculate.service.SalesService;

@SpringBootApplication
public class Main {
    
    private final SalesManager manager;
    
    private final SalesService salesService = null;

    @Autowired
    public Main(SalesManager manager) {
        this.manager = manager;
    }
    
    public static void main(String[] args) {
        // Spring Bootアプリケーションの起動 (Webインターフェース用)
        Main app = SpringApplication.run(Main.class, args).getBean(Main.class);
        
        // 別スレッドでコンソールベースの処理を実行
        new Thread(app::runConsoleApp).start();
    }

    public void runConsoleApp() {
        Scanner scanner = new Scanner(System.in);

//        System.out.println("使用方法: sales_data.txt ファイルを使用します");

        // 初期データのロード
//        try {
//            Path projectRootPath = Paths.get("").toAbsolutePath().normalize();
//            Path filePath = projectRootPath.resolve("src").resolve("sales_data.txt");
//            manager.loadSalesRecordsFromFile(filePath.toString());
//        } catch (IOException e) {
//            System.out.println("データのロードに失敗しました: " + e.getMessage());
//            e.printStackTrace();
//        }

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
                
                salesService.saveSalesRecord(record);
                
//                manager.addSalesRecord(record);
                
                // データベースに追加
//                String sqlFilePath = "src/main/resources/sql/add.sql";  // SQLファイルのパス
//                manager.executeSQLFromFile(sqlFilePath, record);
                
                //テキストファイルにデータを保存
//                try {
//                    Path projectRootPath = Paths.get("").toAbsolutePath().normalize();
//                    Path filePath = projectRootPath.resolve("src").resolve("sales_data.txt");
//                    System.out.println("テキストファイルへデータの保存をしました。");
//                    manager.saveSalesRecordsToFile(filePath.toString());
//                } catch (IOException e) {
//                    System.out.println("テキストファイルへデータの保存に失敗しました。");
//                }

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
//                try {
//                    Path projectRootPath = Paths.get("").toAbsolutePath().normalize();
//                    Path filePath = projectRootPath.resolve("src").resolve("sales_data.txt");
//                    manager.saveSalesRecordsToFile(filePath.toString());
//                } catch (IOException e) {
//                    System.out.println("データの保存に失敗しました。");
//                }
                break;
            }
        }
//        scanner.close();
    }
}
