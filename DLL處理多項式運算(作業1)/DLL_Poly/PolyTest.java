import java.io.IOException;
import java.util.Scanner;

public class PolyTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Poly poly1 = new Poly();
        Poly poly2 = new Poly();

        System.out.println("請選擇輸入方式：");
        System.out.println("1. 手動輸入");
        System.out.println("2. 從文件讀取");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {                  // 手動輸入多項式
            System.out.println("請輸入多項式1: ");
            String input1 = scanner.nextLine();
            Poly.ParseAndAddToDLL(poly1, input1);

            System.out.println("請輸入多項式2: ");
            String input2 = scanner.nextLine();
            Poly.ParseAndAddToDLL(poly2, input2);
        }else {
                                             // 從文件讀取多項式
            System.out.println("請輸入檔案名稱（例如: E:\\\\DLLtest\\\\poly-1.txt）: ");
            String filename = scanner.nextLine();
            try {
                                          // 讀取檔案並解析多項式
                poly1 = Poly.ReadPolyFromFile(filename, 1);
                poly2 = Poly.ReadPolyFromFile(filename, 2);
            } catch (IOException e) {
                System.out.println("讀取檔案失敗: " + e.getMessage());
                return;
            }
        }

        while (true) {
            System.out.println("--------------------");
            System.out.println("請輸入數字選擇運算功能 ");
            System.out.println("1. + ");
            System.out.println("2. - ");
            System.out.println("3. * ");
            System.out.println("4. end ");
            System.out.println("--------------------");
            int func = scanner.nextInt();
            if (func == 4) {
                break;
            }

            Poly result = null;
            switch (func) {
                case 1:
                    result = poly1.add(poly2);
                    System.out.println("(" + poly1 + ") + (" + poly2 + ") = " + result);
                    break;
                case 2:
                    result = poly1.subtract(poly2);
                    System.out.println("(" + poly1 + ") - (" + poly2 + ") = " + result);
                    break;
                case 3:
                    result = poly1.multiply(poly2);
                    System.out.println("(" + poly1 + ") * (" + poly2 + ") = " + result);
                    break;
                default:
                    System.out.println("無效的運算");
                    continue;
            }
        }


    }
}
