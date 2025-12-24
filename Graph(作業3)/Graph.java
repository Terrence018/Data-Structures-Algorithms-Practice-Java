import java.io.*;
import java.util.*;

public class Graph {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Boolean isFlag = true;

        System.out.println("請輸入 檔案名稱 或者 檔案位置: (例:E:/Graph Test/Data test1.txt) ");
        String fileName = sc.nextLine();s

        //創建Graph實例,並從檔案中初始化鄰接矩陣
        Graph graph = new Graph(fileName);

        // 提供操作選單,讓使用者輸入1~6選擇對應功能
        while (isFlag) {
            System.out.println("\n選擇一個操作:");
            System.out.println("1. MST (最小生成樹)");
            System.out.println("2. DFS (深度優先搜尋)");
            System.out.println("3. All Pair Shortest Path (所有節點最短路徑)");
            System.out.println("4. BFS (廣度優先搜尋)");
            System.out.println("5. Single Source Shortest Path (單一來源最短路徑)");
            System.out.println("6. End");

            int choice = sc.nextInt();
            switch (choice) {   //依據對應輸入的數字,調用對應的方法
                case 1:
                    int mstStart = 0;
                    graph.mst(mstStart);
                    break;
                case 2:
                    int dfsStart = 0;
                    graph.dfs(dfsStart);
                    break;
                case 3:
                    graph.allPairShortestPath();
                    break;
                case 4:
                    System.out.print("請輸入BFS的起始點: ");
                    int bfsStart = sc.nextInt();
                    graph.bfs(bfsStart);
                    break;
                case 5:
                    System.out.print("請輸入單一來源最短路徑的起始點: ");
                    int ssspStart = sc.nextInt();
                    graph.singleSourceShortestPath(ssspStart);
                    break;
                case 6:
                    System.out.println("結束程式。");
                    sc.close();
                    isFlag = false;
                    break;
                default:                     //輸入1~6以外的數字時,提示無效並要求重新輸入
                    System.out.println("無效的選擇,重新輸入!");
            }
        }
    }

    private int[][] adjacencyMatrix; //用於儲存鄰接矩陣
    private int vertices;  //Graph的節點數

    //Constructor,讀取文件並傳入readMatrixFromFile方法,初始化鄰接矩陣
    public Graph(String fileName) throws IOException {
        readMatrixFromFile(fileName);
    }

    //根據傳入的file,初始化鄰接矩陣
    private void readMatrixFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); //讀取檔案用變數br接收
        List<int[]> matrixList = new ArrayList<>(); //儲存檔案每一行的數據

        String line;
        while ((line = br.readLine()) != null) { //逐行讀取檔案內容,若為空則跳出while循環,達到逐行讀取目的
            String[] tokens = line.trim().split(" ");//去除每行的空格處,並之後分割成數字字串,放入token當中
            int[] row = Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();//將token內的字串中的數字取出,放到矩陣row當中
            matrixList.add(row); //之後將row逐行加入列表matrixList
        }
        br.close();

        vertices = matrixList.size(); //vertices變數儲存總節點數
        adjacencyMatrix = new int[vertices][vertices]; //初始化鄰接矩陣,根據總節點數設定矩陣邊界

        for (int i = 0; i < vertices; i++) { //將matrixList該List中數據逐步放入鄰接矩陣中
            adjacencyMatrix[i] = matrixList.get(i);
        }

        //輸出鄰接矩陣
        System.out.println("讀取的鄰接矩陣:");
        for (int i = 0; i < vertices; i++) {
            System.out.println(Arrays.toString(adjacencyMatrix[i]));
        }

        //輸出節點 與 總節點數
        System.out.println("節點:0 至 節點:" + (vertices - 1) + ",總節點數目: " + vertices );
    }


    // MST 最小生成樹 (Prim's Algorithm)
    public void mst(int start) {
        boolean[] visited = new boolean[vertices]; // 記錄頂點是否已被包含在生成樹中
        int[] key = new int[vertices]; //儲存每個節點的權重
        int[] parent = new int[vertices]; //儲存生成樹的結構

        Arrays.fill(key, Integer.MAX_VALUE); //初始化所有節點權重為無限大
        Arrays.fill(parent, -1); //初始化parent為-1
        key[start] = 0;           //起始點的key值設定為0
        parent[start] = -1;          //起始點無父節點,故設定為-1

        for (int count = 0; count < vertices - 1; count++) {
            int u = minKey(key, visited); //調用minkey方法,訪問目前權重最小且尚未訪問的節點
            visited[u] = true;  //訪問該節點後,將該節點標記為已訪問

            for (int v = 0; v < vertices; v++) { //之後更新所有與u相連,但尚未訪問的節點的權重和父節點
                if (adjacencyMatrix[u][v] != 0 && !visited[v] && adjacencyMatrix[u][v] < key[v]) {
                    parent[v] = u; //將父節點設定為u
                    key[v] = adjacencyMatrix[u][v]; //更新權重,根據鄰接矩陣u到v之值來設定
                }
            }
        }

        printMST(parent); //調用printMST方法,輸出最小生成樹MST
    }


    // 找到 所有未訪問節點中 權重最小的節點
    private int minKey(int[] key, boolean[] visited) {

        int min = Integer.MAX_VALUE; //設定變數min,初始化為Max_value,確保第一次if語句判斷時,所有數字都比它小
        int minIndex = -1;  //初始化 minIndex為-1

        for (int v = 0; v < vertices; v++) { //對所有節點進行遍歷
            if (!visited[v] && key[v] < min) { //用於尋找所有滿足條件,節點v不是已訪問的,並且它的key值小於min
                min = key[v];              //就將min修改為該節點v的key值
                minIndex = v;              //並且該節點v的索引儲存在minIndex當中
            }
        }
        return minIndex; //返回minIndex,最小值所在處的索引
    }

    private void printMST(int[] parent) {
        List<int[]> edges = new ArrayList<>(); //edges變數用於儲存 圖 的邊

        for (int i = 0; i < vertices; i++) { //遍歷所有節點i
            if (parent[i] != -1) { //若滿足節點i與其父節點之間存在一條邊,則將其加入edges中
                edges.add(new int[]{parent[i], i, adjacencyMatrix[parent[i]][i]}); //依序加入:起點、終點、權重
            }
        }

        //之後依據邊的權重大小去做sort
        edges.sort(Comparator.comparingInt(edge -> edge[2]));

        //使用增強for循環,打印MST
        System.out.println("邊 \t 權重");
        for (int[] edge : edges) {
            System.out.println(edge[0] + " - " + edge[1] + "\t" + edge[2] + "\t" + "adjacencyMatrix[" + edge[0] + "][" + edge[1] + "] = " + edge[2]);
        }
    }

    // DFS(使用遞迴)
    public void dfs(int start) {
        boolean[] visited = new boolean[vertices]; // 記錄節點是否被訪問
        int[] discovery = new int[vertices];      // 記錄每個節點的發現時間d[u]
        int[] finish = new int[vertices];         // 記錄每個節點的完成時間f[u]
        int[] predecessor = new int[vertices];    // 記錄每個節點的前行點
        Arrays.fill(predecessor, -1);             //預設所有節點的前行點為-1
        int[] time = {0};                         //使用陣列記錄時間，以便在遞迴中共享

        System.out.println("DFS 從節點 " + start + " 開始:");
        dfsUtil(start, visited, discovery, finish, predecessor, time);

        System.out.println();//換行
        // 輸出結果

        System.out.println("節點\td[u]\tf[u]\t前行點");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%d\t%d\t\t%d\t\t%s%n", i, discovery[i], finish[i], (predecessor[i] == -1 ? "無" : predecessor[i]));
        }
    }

    private void dfsUtil(int u, boolean[] visited, int[] discovery, int[] finish, int[] predecessor, int[] time) {
        visited[u] = true;               //標記當前節點為已訪問
        discovery[u] = ++time[0];        //設置發現時間
        System.out.println("訪問節點 " + u + ", 發現時間 d[" + u + "] = " + discovery[u]);

        for (int v = 0; v < vertices; v++) {
            if (adjacencyMatrix[u][v] != 0 && !visited[v]) { //若節點u與節點v有連接，且v尚未訪問
                predecessor[v] = u;                         //設定v的前行點為 u
                System.out.println("節點 " + v + " 的前行點設為 " + u);
                dfsUtil(v, visited, discovery, finish, predecessor, time); //遞迴訪問節點v
            }
        }

        finish[u] = ++time[0];           // 設置完成時間
        System.out.println("完成節點 " + u + ", 完成時間 f[" + u + "] = " + finish[u]);
    }


    // BFS
    public void bfs(int bfsStart) {
        boolean[] visited = new boolean[vertices]; //記錄節點是否已訪問
        int[] distance = new int[vertices];       //記錄每個節點的d[u]
        int[] predecessor = new int[vertices];    //記錄每個節點的前行點
        Arrays.fill(distance, Integer.MAX_VALUE); //初始化所有距離為無限大
        Arrays.fill(predecessor, -1);             //初始化所有節點的前行點為-1

        Queue<Integer> queue = new LinkedList<>(); //用於廣度優先搜尋的queue
        int start = bfsStart;                             //根據用戶選擇起始點開始

        visited[start] = true;   //設定起始節點為已訪問
        distance[start] = 0;     //起始節點距離設為 0
        queue.add(start);        //將起始節點加入隊列

        System.out.println("BFS 自動從節點 0 開始:");

        while (!queue.isEmpty()) {
            int u = queue.poll(); //取出queue節點,相當於dequeue
            System.out.println("訪問節點 " + u + ", 距離 d[" + u + "] = " + distance[u]);

            // 遍歷與當前節點 u 相連的所有節點
            for (int v = 0; v < vertices; v++) {
                if (adjacencyMatrix[u][v] != 0 && !visited[v]) { //若u和v相連，且v尚未被訪問
                    visited[v] = true;             //標記v為已訪問
                    distance[v] = distance[u] + 1; //更新節點v的距離
                    predecessor[v] = u;            //設定節點v的前行點為 u
                    System.out.println("節點 " + v + " 的前行點設為 " + u + ", 距離 d[" + v + "] = " + distance[v]);
                    queue.add(v);                  //將節點v加入隊列
                }
            }
        }

        System.out.println();//換行

        // 輸出結果
        System.out.println("節點\t距離(d)\t前行點");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%d\t%s\t\t%s%n", i, (distance[i] == Integer.MAX_VALUE ? "∞" : distance[i]), (predecessor[i] == -1 ? "無" : predecessor[i]));
        }
    }


    // All Pair Shortest Path
    public void allPairShortestPath() {
        int[][] dist = new int[vertices][vertices];     //創建dist數組

        for (int i = 0; i < vertices; i++) { //初始化dist數組之值
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = (adjacencyMatrix[i][j] == 0 && i != j) ? Integer.MAX_VALUE : adjacencyMatrix[i][j];
            }
        }

        //使用三層for迴圈遍歷所有i j節點及可能之中繼節點k
        //若節點i到節點j經由節點k到達之距離,會小於目前節點i至節點j的距離
        //則更新當前節點i至節點j的距離 為當前經過中繼k之距離
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        //打印dist矩陣做完成All Pair Shortest Path
        System.out.println("所有節點最短路徑矩陣:");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) { //矩陣Integer.MAX_VALUE用無限符號代替
                System.out.print((dist[i][j] == Integer.MAX_VALUE ? "∞" : dist[i][j]) + " ");
            }
            System.out.println();
        }
    }

    // Single Source Shortest Path (Dijkstra's Algorithm)
    public void singleSourceShortestPath(int start) {
        int[] dist = new int[vertices]; // 儲存從起始頂點到其他頂點的最短距離
        boolean[] visited = new boolean[vertices]; // 記錄訪問狀態

        Arrays.fill(dist, Integer.MAX_VALUE); // 初始化距離為無限大
        dist[start] = 0; // 起始點距離設為0

        for (int count = 0; count < vertices - 1; count++) {
            int u = minDistance(dist, visited); //調用minDistance方法,找到未訪問節點中距離最小的節點
            visited[u] = true;  //並將該節點設定為已訪問true

            for (int v = 0; v < vertices; v++) { //遍歷所有節點v

                //if條件,檢查節點v是否尚未訪問，且從u到v的邊存在，並且u可達。如果通過u的路徑更短，則更新dist[v]
                if (!visited[v] && adjacencyMatrix[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + adjacencyMatrix[u][v] < dist[v]) {
                    dist[v] = dist[u] + adjacencyMatrix[u][v]; //若更短,則修改起點到v的距離為通過中繼節點u再到達v的距離
                }
            }
        }

        //最終打印Single Source Shortest Path完成
        System.out.println("從節點 " + start + " 到其他節點的最短距離:");
        for (int i = 0; i < vertices; i++) { //其中dist[i]之值若是Integer.MAX_VALUE則設定為無限符號
            System.out.println(start + " -> " + i + ": " + (dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]));
        }

    }

    //該方法尋找未訪問節點中距離最小的節點
    private int minDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE; //初始化變數min為一個極大值,確保第一次if判斷處比較時必定小於min
        int minIndex = -1;        //初始化minIndex為-1

        for (int v = 0; v < vertices; v++) { //for循環,找尋未訪問節點中距離最小的節點
            if (!visited[v] && dist[v] < min) { //!visited[v]確保節點尚未被處理,dist[v] < min用於找到更短的距離
                min = dist[v]; //若找到則更新min為dist[v],做為目前的最短距離
                minIndex = v;  //並將v做為索引儲存在minIndex
            }
        }
        return minIndex; //返回minIndex,該節點的索引位置
    }

}
