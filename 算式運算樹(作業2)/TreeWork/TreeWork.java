import java.io.BufferedReader;
import java.io.*;
import java.util.*;

public class TreeWork {

    static Scanner scanner = new Scanner(System.in);
    static TreeNode root = null;  // 根節點
    static String expression = ""; // 用來儲存當前的中序算式

    public static void main(String[] args) {
        // 顯示第一頁選單，讓使用者選擇輸入算式的方式
        while (true) {
            System.out.println("請選擇輸入方式:");
            System.out.println("1. 自行輸入算式");
            System.out.println("2. 輸入檔案");
            System.out.print("請輸入選項 (1 或 2): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("請輸入中序算式: ");
                    setExpression(scanner.nextLine());
                    break;
                case "2":
                    System.out.print("請輸入檔案路徑: ");
                    String filePath = scanner.nextLine();
                    readFileAndSetExpression(filePath);
                    break;
                default:
                    System.out.println("無效選項，請重新輸入 (1 或 2)。");
                    continue;
            }

            // 當運算式被讀入後，進入第二頁選單，顯示功能選項
            while (true) {
                System.out.println("\n請選擇操作功能:");
                System.out.println("1. prefix - 顯示前序運算式");
                System.out.println("2. postfix - 顯示後序運算式");
                System.out.println("3. eval - 計算運算結果");
                System.out.println("4. tree - 顯示運算樹");
                System.out.println("5. new - 重新輸入算式");
                System.out.println("6. end - 結束程式");
                System.out.print("請輸入選項 (1-6): ");
                String secondChoice = scanner.nextLine();

                switch (secondChoice) {
                    case "1":
                        System.out.print("前序運算式: ");
                        printPrefix(root);
                        System.out.println();
                        break;
                    case "2":
                        System.out.print("後序運算式: ");
                        printPostfix(root);
                        System.out.println();
                        break;
                    case "3":
                        // 修改後的 eval 功能
                        Map<String, Double> variableValues = new HashMap<>(); // 用於存儲變數的值
                        double result = evaluate(root, variableValues);       // 傳入支持變數的 evaluate 方法
                        System.out.println("運算結果: " + result);             // 顯示結果
                        break;
                    case "4":
                        System.out.println("運算樹的結構: ");
                        printTree(root);
                        break;
                    case "5":
                        System.out.print("請輸入新的中序算式: ");
                        setExpression(scanner.nextLine());
                        break;
                    case "6":
                        System.out.println("程式結束。");
                        return;
                    default:
                        System.out.println("無效選項，請重新輸入 (1-6)。");
                }
            }
        }
    }

    public static void readFileAndSetExpression(String inputFile) {   // 讀取檔案並設置運算式
        //輸入檔案路徑,例如:E:\TreeTest\exp-1.txt
        try {

            // 創建檔案對象                       //用File類接收檔案,後可調用exists()、canRead()方法判別檔案是否存在、可讀
            File file = new File(inputFile);

            //檔案不存在則告知
            if (!file.exists()) {
                System.out.println("檔案不存在");
            }

            //檔案無法讀取則告知
            if(!file.canRead()){
                System.out.println("檔案不可讀");
            }

            // 讀取檔案
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            if (line != null) {
                System.out.println("從檔案讀取到運算式: " + line);
                setExpression(line);
            } else {
                System.out.println("檔案中沒有運算式。");
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("檔案讀取錯誤: " + e.getMessage());
        }
    }

    //此處將設置運算樹拆分成兩個方法,setExpression()和parseExpressionToTree()
    //增加代碼的靈活性,和之後修改、擴展時更加方便
    public static void setExpression(String expr) {   // 設定運算樹的方法，根據表達式來構建樹
        expression = expr;                              //當使用者希望選擇任何關於建構算式時,調用此方法來完成
        root = parseExpressionToTree(expr);             //之後接著調用parseExpressionToTree來處理
    }

    public static TreeNode parseExpressionToTree(String expr) {  // 解析中序運算式並建立運算樹
        expr = expr.replaceAll("\\s+", ""); //使用\\s+正則表達示匹配空格,例如3 + 2,這樣做可以把空格刪除,變成3+2

        Stack operandStack = new Stack(); // 操作數字的Stack,用來儲存數字或變數
        Stack operatorStack = new Stack(); // 運算符的Stack,用來存儲運算符

        for (int i = 0; i < expr.length(); i++) {     // 遍歷整個表達式字符串
            char currentChar = expr.charAt(i);

            if (Character.isLetter(currentChar)) {      // 如果是變數字母(如 a、b、x等),則視為操作數
                operandStack.push(new TreeNode(String.valueOf(currentChar)));

            } else if (Character.isDigit(currentChar)) { //如果是數字,則進行以下操作

                StringBuilder num = new StringBuilder();            //num搭配下方while循環用來處理連續的數字,例如123是三位數,而非三個獨立的個位數
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {     //這部分邏輯用來處理連續的數字
                    num.append(expr.charAt(i));                                 //將多位的數字拼接,確保其是一個數字,而不是多個獨立數字
                    i++;                                            //指針移動到下一個字符
                }
                i--;                                             //i-- 是一個補償操作，目的是將 i 恢復到正確的有效字符位置
                operandStack.push(new TreeNode(num.toString()));            // 將拼接完成的數字壓入operandStack中
                                                //Tree Node建構子,此時同時將值放入節點的value當中

            } else if (currentChar == '(') {  // 左括號則直接壓入operatorStack
                operatorStack.push(currentChar);

            } else if (currentChar == ')') { // 右括號
                                                //operator非空,且operator.peek(),用peek()方法去看Stack頂部不是左括號,便持續進行while循環
                                                //從operandStack彈出兩數字,並且operandStack彈出運算符,使該二數字運算後,壓回operandStack中
                while (!operatorStack.isEmpty() && (Character) operatorStack.peek() != '(') {
                    operandStack.push(createNode((TreeNode) operandStack.pop(), (TreeNode) operandStack.pop(), (Character) operatorStack.pop()));
                                        //此外,此處push之時調用createNode方法,同時來建構樹
                }

                                                    // 如果堆疊為空或左括號丟失，拋出異常
                if (operatorStack.isEmpty()) {
                    throw new IllegalArgumentException("運算式中的括號不匹配！");
                }

                operatorStack.pop(); // 最後,將左括號'('彈出

            } else if (isOperator(currentChar)) {               //當currentChar是運算符,,則需要考慮優先級
                                                                // 進行優先權的比較,決定是否壓入、彈出原先之運算符

                                                                //使用precedence比較符號之優先級大小
                                                              // while循環確保operatorStack中的運算符比當前的運算符優先級低
                                                               //否則進入循環體,例如此時*號於Stack中,當前符號是+號,則進入循環體,彈出兩operandStack中數字
                                                               //使用*進行二者計算,再將計算結果壓回operandStack
                while (!operatorStack.isEmpty() && precedence((Character) operatorStack.peek()) >= precedence(currentChar)) {

                    if (operandStack.size() < 2) { // 檢查操作數,若數量<2則無法做運算
                        throw new IllegalArgumentException("運算式中的運算符和操作數數量不匹配");
                    }

                    operandStack.push(createNode((TreeNode) operandStack.pop(), (TreeNode) operandStack.pop(), (Character) operatorStack.pop()));
                }
                operatorStack.push(currentChar);                // 操作符        //承上例子,剩下之+號則壓入operatorStack中

            } else {
                throw new IllegalArgumentException("無效字符: " + currentChar);     //無效字符拋出異常,告知當前字符是無效字符
            }
        }

        while (!operatorStack.isEmpty()) {                       //最後,使用迴圈將堆疊中剩餘之operator及operand進行運算完全處理完畢

            if (operandStack.size() < 2) {                                      //如果operand<2,則無法做運算,拋出異常
                throw new IllegalArgumentException("運算式中的運算符和操作數數量不匹配！");
            }
                                                                                                            //運算結果壓回operandStack中
            operandStack.push(createNode((TreeNode) operandStack.pop(), (TreeNode) operandStack.pop(), (Character) operatorStack.pop()));
        }


                                                                    //最後檢查是否存在多餘的左括號,若有則意味運算式有誤,拋出異常
        if (operatorStack.contains('(')) {
            throw new IllegalArgumentException("運算式中的括號不匹配！");
        }

        return (TreeNode) operandStack.pop(); // 返回運算樹的根節點
    }


    // 創建二叉樹節點
    //此方法在operandStack操作數棧之時,同時進行樹節點的創建
    //該方法使用兩個operand和一個operator,來建構新節點
    //並且最後會返回它
    public static TreeNode createNode(TreeNode right, TreeNode left, char operator) {
        TreeNode node = new TreeNode(String.valueOf(operator));
        node.left = left;
        node.right = right;
        return node;
    }

    // 檢查字符是否為運算符
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    //判別運算符的優先級
    public static int precedence(char operator) {
        if (operator == '+' || operator == '-') {                                       //+、-號給予權重1
            return 1;
        } else if (operator == '*' || operator == '/') {                                //*、/給予權重2
            return 2;
        }
        return -1;
    }


    public static double evaluate(TreeNode node, Map variableValues) { // 計算運算式的值，支持變數
        if (node == null) return 0;

        if (node.left == null && node.right == null) {  //若左右子樹為空,則意味著是葉子節點,此時直接 return 獲取數字或變數值
            try {
                return Double.parseDouble(node.value);
            } catch (NumberFormatException e) {
                // 如果無法解析為數字，則視為變數
                String variable = node.value;
                if (!variableValues.containsKey(variable)) {
                    // 如果變數未定義，詢問用戶
                    System.out.print("請輸入變數 " + variable + " 的值: ");
                    double value = scanner.nextDouble(); // 從用戶處獲取變數值
                    variableValues.put(variable, value); // 存儲變數值
                }
                return (double) variableValues.get(variable); // 返回變數的值
            }
        }

        // 遞迴處理左右子樹，然後根據操作符進行運算
        double leftValue = evaluate(node.left, variableValues);
        double rightValue = evaluate(node.right, variableValues);

        // 根據運算符進行運算
        switch (node.value) {
            case "+": return leftValue + rightValue;
            case "-": return leftValue - rightValue;
            case "*": return leftValue * rightValue;
            case "/":
                if (rightValue == 0) {                          //避免除數為0
                    throw new ArithmeticException("除數不能為零");
                }
                return leftValue / rightValue;
            default:
                throw new UnsupportedOperationException("不支援的運算符: " + node.value);
        }
    }


    public static void printPrefix(TreeNode node) {     // 顯示前序運算式
        if (node == null){                          //若節點為null,則直接return,表示處理到末尾(節點沒有子樹)
            return;
        }
        System.out.print(node.value + " ");         //前序運算式,意味著運算符在式子前方,故優先進行輸出
        printPrefix(node.left);                         //接著分別左子樹及右子樹遞迴該printPrefix方法
        printPrefix(node.right);
    }


    public static void printPostfix(TreeNode node) {    // 顯示後序運算式
        if (node == null){                              //若節點為null,則直接return,表示處理到末尾(節點沒有子樹)
            return;
        }
        printPostfix(node.left);                    //後序運算式,意味著運算符在式子後方,故最後進行輸出
        printPostfix(node.right);                   //故先遞迴的方式處理左右子樹,之後再去輸出根節點的值
        System.out.print(node.value + " ");
    }


    public static void printTree(TreeNode root) {   // 顯示運算樹的結構
                                                    //此處邏輯,參照上課ppt p.29
        if (root == null) return;

                                        // 使用Queue來進行樹的遍歷
        Queue queue = new LinkedList();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // 當前層的節點數量
            List currentLevel = new ArrayList();

                                                    // 處理當前層的所有節點

            for (int i = 0; i < levelSize; i++) {
                TreeNode currentNode = (TreeNode) queue.poll();
                currentLevel.add(currentNode.value);  // 添加節點值

                if (currentNode.left != null) queue.add(currentNode.left);
                if (currentNode.right != null) queue.add(currentNode.right);
            }


                                                 // 顯示當前層的所有節點
            for (Object value : currentLevel) {  //使用 增強for循環 逐一訪問Node,類型為 Object 類型

                System.out.print(value + " ");  //輸出節點的值,之後空格分開

            }
            System.out.println();  // 換行,表示樹的往下一層
        }
    }
}
