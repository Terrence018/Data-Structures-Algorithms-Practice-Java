# Data Structures & Algorithms Practice (Java)

本專案收錄我使用 **Java** 完成的資料結構與演算法課程實作，重點在於「自己動手把核心結構與演算法寫出來」，並提供測資檔案方便重跑。

## 本專案涵蓋的資料結構與演算法概念

### 資料結構 (Data Structures)
- **Doubly Linked List（雙向鏈結串列）**
  - 用節點 (Node) 連接前後指標，儲存多項式的每一項（係數/次方）
- **Tree（樹） / Expression Tree（運算式樹）**
  - 以樹節點表示運算子與運算元，支援遍歷與計算
- **Graph（圖）**
  - 從測資建立圖結構，支援走訪、最短路、最小生成樹等操作

### 演算法 (Algorithms)
- **多項式運算**
  - 以鏈結串列表示多項式並實作 `+ / - / *`
- **Tree Traversal（樹遍歷）**
  - 前序 (Preorder) / 後序 (Postorder)
- **Graph Traversal（圖走訪）**
  - DFS（Depth-First Search）
  - BFS（Breadth-First Search）
- **Shortest Path（最短路徑）**
  - Dijkstra（Single Source Shortest Path）
  - Floyd–Warshall（All Pairs Shortest Path）
- **Minimum Spanning Tree（最小生成樹）**
  - Prim
  - Kruskal

---

## 📁 Assignments

### 1) Polynomial with DLL（作業1）
使用 **雙向鏈結串列** 表示多項式，支援兩種輸入方式：
- 讀取測資檔 `poly-1.txt`
- 手動輸入多項式

可選擇運算 `+ / - / *`，並輸出運算式與結果。
- `DLL_poly/Node.java`：節點結構
- `DLL_poly/Poly.java`：多項式核心運算
- `DLL_poly/PolyTest.java`：主程式（使用者介面/流程）

---

### 2) Expression Tree（作業2）
將算術運算式建立為 **運算式樹**（測資：`exp-1.txt`，例如 `52-(19-2*3)*2+13`），並提供：
- 前序表示式（Prefix）
- 後序表示式（Postfix）
- 計算結果（Evaluation）

- `TreeWork/TreeNode.java`：樹節點
- `TreeWork/TreeWork.java`：建樹、遍歷、計算

---

### 3) Graph Algorithms（作業3）
讀取測資 `test1.txt ~ test3.txt` 建立圖，並提供下列功能選單：
- DFS / BFS
- Minimum Spanning Tree（Prim / Kruskal）
- Shortest Path
  - All Pairs（Floyd–Warshall）
  - Single Source（Dijkstra）

---

## 🧪 Test Data
- 作業1：`poly-1.txt`
- 作業2：`exp-1.txt`
- 作業3：`test1.txt ~ test3.txt`
