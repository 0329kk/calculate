package com.jp.calculate;

import java.time.LocalDate;
import java.util.Map;
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
    
    private final SalesService salesService;

    @Autowired
    public Main(SalesManager manager, SalesService salesService) {
        this.manager = manager;
        this.salesService = salesService;
    }
    
    public static void main(String[] args) {
        // Spring Bootアプリケーションの起動 (Webインターフェース用)
        Main app = SpringApplication.run(Main.class, args).getBean(Main.class);
        
        // 別スレッドでコンソールベースの処理を実行
        new Thread(app::runConsoleApp).start();
    }

    public void runConsoleApp() {
	try (Scanner scanner = new Scanner(System.in)) {

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

                    // salesServiceを使用してデータベースに保存
                    salesService.saveSalesRecord(record);

                } else if (choice == 2) {
                    // 売上データを表示
                    manager.displaySalesRecords();

                } else if (choice == 3) {
                    // 日別売上集計
                    Map<LocalDate, Double> dailySales = manager.calculateDailySales();
                    System.out.println(manager.formatDailySales(dailySales));

                } else if (choice == 4) {
                    // 商品別売上集計
                    System.out.println(manager.calculateProductSales());

                } else if (choice == 5) {
                    // 全期間売上合計
                    System.out.println(manager.calculateTotalSales());

                } else if (choice == 6) {
                    // 終了
                    System.out.println("コンソール機能を終了しました。");
                    break;
                }
            }
        }
    }
}
