import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Poly {
    Node head, tail;

    public void addToDLL(int coef,int exp){
        if(head == null){
            head = tail = new Node(coef,exp);
        }else{
            tail.next = new Node(coef,exp);
            tail.next.prev = tail;
            tail = tail.next;
        }
    }
    public static void ParseAndAddToDLL(Poly poly, String input) {  //該方法用於拆解多項式

                                                 //負號改為+-號,例如7x^2-6x,則變為7x^2+-6x,如此能夠正確分割每個項次
                                                //後方的replace則是消除多餘的" ",如此在輸入多項式時更為方便
        input = input.replace("-", "+-").replace(" ", "");

        String[] terms = input.split("\\+");  // 以 +號分割為單獨的項式

        for (String term : terms) {
            if (term.isEmpty()) continue;

            int coef1 = 0;
            int exp1 = 0;

            if (term.contains("x^")) { // 如果項次中包含 x^，例如:6x^2
                String[] parts = term.split("x\\^");
                coef1 = Integer.parseInt(parts[0].trim());
                exp1 = Integer.parseInt(parts[1].trim());

            } else if (term.contains("x")) { // 如果項次中只包含 x，則假設為一次方，例如:7x
                String[] parts = term.split("x");
                coef1 = Integer.parseInt(parts[0].trim());
                exp1 = 1;

            } else { // 如果沒有 x，則是常數項，次方為 0，如 -5
                coef1 = Integer.parseInt(term.trim());
                exp1 = 0;
            }

            poly.addToDLL(coef1, exp1);  //拆解後,將其加入多項式中
        }
    }

    public String toString() {             //重寫Poly類中的toString(),使得運算的輸出符合我想要的結果
        StringBuilder sb = new StringBuilder();
        Node current = head;
        boolean isFirstTerm = true;

        while (current != null) {

            if (current.coef != 0) {            //只加入係數不為0的項次

                if (!isFirstTerm && current.coef > 0) {
                    sb.append("+");             // 顯示+號在正數項前
                }

                if (current.exp == 0) {
                    sb.append(current.coef);    // 常數項只顯示係數

                } else if (current.exp== 1) {   // 如果次方為 1,僅顯示x,
                    sb.append(current.coef + "x");

                } else {
                    sb.append(current.coef + "x^" + current.exp);//非常數,則正常表示
                }
                isFirstTerm = false;
            }
            current = current.next;
        }

        if (sb.length() == 0) {
            return "0";  // 如果沒有任何有效項，返回 "0"
        }

        return sb.toString();
    }

    public Poly add(Poly other){        //多項式加法
        Poly AddResult = new Poly();
        Node p1 = this.head, p2 = other.head;  //調用該add方法的多項式,會賦給Node p1
                                               //於形參傳入的多項式賦給Node p2

                                            //while()來達到遍歷p1和p2節點的效果
        while (p1 != null || p2 != null) { //循環內部,AddResult調用addToDll方法,持續將p1、p2加到DLL當中
                                           //同時,若p1或p2被加入後,便調用next方法,使其進入下一節點
                                            //之後回到while()循環,持續加入p1或p2,直到二者遍歷完畢皆為null,則停止
            if (p1 == null) {
                AddResult.addToDLL(p2.coef, p2.exp);
                p2 = p2.next;
            } else if (p2 == null) {
                AddResult.addToDLL(p1.coef, p1.exp);
                p1 = p1.next;
            } else if (p1.exp == p2.exp) {              //次方項相同,二者係數相加
                AddResult.addToDLL(p1.coef + p2.coef, p1.exp);
                p1 = p1.next;
                p2 = p2.next;
            } else if (p1.exp > p2.exp) {
                AddResult.addToDLL(p1.coef, p1.exp);
                p1 = p1.next;
            } else {
                AddResult.addToDLL(p2.coef, p2.exp);
                p2 = p2.next;
            }
        }

        return AddResult;
    }

    public Poly subtract(Poly other) {  //多項式減法 //做法類似於多項式加法
        Poly SubResult = new Poly();
        Node p1 = this.head, p2 = other.head;

        while (p1 != null || p2 != null) {
            if (p1 == null) {
                SubResult.addToDLL(-p2.coef, p2.exp);
                p2 = p2.next;
            } else if (p2 == null) {
                SubResult.addToDLL(p1.coef, p1.exp);
                p1 = p1.next;
            } else if (p1.exp == p2.exp) {            //次方項相同,二者係數相減
                SubResult.addToDLL(p1.coef - p2.coef ,p1.exp);
                p1 = p1.next;
                p2 = p2.next;
            } else if (p1.exp > p2.exp ){
                SubResult.addToDLL(p1.coef, p1.exp);
                p1 = p1.next;
            } else {
                SubResult.addToDLL(-p2.coef, p2.exp);
                p2 = p2.next;
            }
        }
        return SubResult;
    }

    public Poly multiply(Poly other) {          // 多項式乘法
        Poly MultiResult = new Poly();
                                             //調用該方法的多項式賦給Node p1,傳入形參的多項式賦給Node p2
                                            //此處使用雙層迴圈,首先p1由首節點開始,持續與p2的節點,
                                            //進行 "係數相乘" 以及 "次方相加" , temp則用於暫存每次相乘的結果
                                           //兩層迴圈的迭代部分功能皆是使p1、p2在當前節點計算完畢後,逐一移動到下一個節點進行計算
                                            //例如p1 = p1.next;則p1移動到下一節點,之後在來到內層p2的迴圈與p2進行相乘
                                            //之後tmep會隨著外層p1的迴圈逐一與p2計算時,加到MultilResult當中,以此最終達到多項式乘法的效果
        for (Node p1 = this.head; p1 != null; p1 = p1.next) {
            Poly temp = new Poly();
            for (Node p2 = other.head; p2 != null; p2 = p2.next) {
                int newCoefficient = p1.coef * p2.coef;             //係數相乘
                int newExponent = p1.exp + p2.exp;                  //次方相加
                temp.addToDLL(newCoefficient, newExponent);
            }
            MultiResult = MultiResult.add(temp);  //調用add,用於累積每一輪的乘積結果
        }

        return MultiResult;
    }

    public static Poly ReadPolyFromFile(String filename, int polyNumber) throws IOException {
        Poly poly = new Poly();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int lineCount = 0;              //計算當前讀取的行數

        while ((line = reader.readLine()) != null) {    //while()循環不斷讀文件,若讀完則readLine()會返回null
            lineCount++;
            if (lineCount == polyNumber) {     //當行數等於polynumber時即進入方法體
                                                //例如poly number用2傳入,意即我們所需之多項式在文件內的第二行

                ParseAndAddToDLL(poly, line);  //找到我們要的方程式後,用ParseAndAddToDLL去拆解它
                break;
            }
        }

        reader.close();
        return poly;                //返回讀取到的多項式
    }


}
