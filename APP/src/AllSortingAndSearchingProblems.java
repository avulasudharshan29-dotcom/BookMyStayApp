import java.util.*;

// ================= MAIN CLASS =================
public class AllSortingAndSearchingProblems {

    // ================= PROBLEM 1 =================
    static class Transaction {
        String id;
        double fee;
        String ts;

        Transaction(String id, double fee, String ts) {
            this.id = id;
            this.fee = fee;
            this.ts = ts;
        }

        public String toString() {
            return id + ":" + fee + "@" + ts;
        }
    }

    static void bubbleSortTransactions(List<Transaction> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void insertionSortTransactions(List<Transaction> list) {
        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).fee > key.fee) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    static void findOutliers(List<Transaction> list) {
        System.out.println("Outliers (>50):");
        for (Transaction t : list) {
            if (t.fee > 50) System.out.println(t);
        }
    }

    // ================= PROBLEM 2 =================
    static class Client {
        String name;
        int risk;
        int balance;

        Client(String name, int risk, int balance) {
            this.name = name;
            this.risk = risk;
            this.balance = balance;
        }

        public String toString() {
            return name + ":" + risk;
        }
    }

    static void bubbleSortClients(List<Client> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).risk > list.get(j + 1).risk) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }

    static void insertionSortClients(List<Client> list) {
        for (int i = 1; i < list.size(); i++) {
            Client key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).risk < key.risk) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // ================= PROBLEM 3 =================
    static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    static void merge(int[] arr, int l, int m, int r) {
        int[] temp = new int[r - l + 1];
        int i = l, j = m + 1, k = 0;

        while (i <= m && j <= r) {
            if (arr[i] <= arr[j]) temp[k++] = arr[i++];
            else temp[k++] = arr[j++];
        }

        while (i <= m) temp[k++] = arr[i++];
        while (j <= r) temp[k++] = arr[j++];

        for (int x = 0; x < temp.length; x++)
            arr[l + x] = temp[x];
    }

    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] > pivot) { // DESC
                i++;
                int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
            }
        }
        int t = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = t;
        return i + 1;
    }

    // ================= PROBLEM 4 =================
    static void mergeSortDouble(double[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSortDouble(arr, l, m);
            mergeSortDouble(arr, m + 1, r);
            mergeDouble(arr, l, m, r);
        }
    }

    static void mergeDouble(double[] arr, int l, int m, int r) {
        double[] temp = new double[r - l + 1];
        int i = l, j = m + 1, k = 0;

        while (i <= m && j <= r) {
            if (arr[i] <= arr[j]) temp[k++] = arr[i++];
            else temp[k++] = arr[j++];
        }

        while (i <= m) temp[k++] = arr[i++];
        while (j <= r) temp[k++] = arr[j++];

        for (int x = 0; x < temp.length; x++)
            arr[l + x] = temp[x];
    }

    // ================= PROBLEM 5 =================
    static int linearSearch(String[] arr, String target) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].equals(target)) return i;
        return -1;
    }

    static int binarySearch(String[] arr, String target) {
        int l = 0, r = arr.length - 1;

        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m].equals(target)) return m;
            if (arr[m].compareTo(target) < 0) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }

    // ================= PROBLEM 6 =================
    static int floor(int[] arr, int target) {
        int res = -1;
        for (int x : arr)
            if (x <= target) res = x;
        return res;
    }

    static int ceil(int[] arr, int target) {
        for (int x : arr)
            if (x >= target) return x;
        return -1;
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        // Problem 1
        List<Transaction> tx = new ArrayList<>();
        tx.add(new Transaction("id1", 10.5, "10:00"));
        tx.add(new Transaction("id2", 25.0, "09:30"));
        tx.add(new Transaction("id3", 5.0, "10:15"));

        bubbleSortTransactions(tx);
        System.out.println("P1 Bubble: " + tx);
        insertionSortTransactions(tx);
        System.out.println("P1 Insertion: " + tx);
        findOutliers(tx);

        // Problem 2
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("C", 80, 1000));
        clients.add(new Client("A", 20, 2000));
        clients.add(new Client("B", 50, 1500));

        bubbleSortClients(clients);
        System.out.println("P2 Bubble: " + clients);
        insertionSortClients(clients);
        System.out.println("P2 Insertion: " + clients);

        // Problem 3
        int[] trades = {500, 100, 300};
        mergeSort(trades, 0, trades.length - 1);
        System.out.println("P3 Merge: " + Arrays.toString(trades));
        quickSort(trades, 0, trades.length - 1);
        System.out.println("P3 Quick: " + Arrays.toString(trades));

        // Problem 4
        double[] returns = {12, 8, 15};
        mergeSortDouble(returns, 0, returns.length - 1);
        System.out.println("P4 Merge: " + Arrays.toString(returns));

        // Problem 5
        String[] logs = {"accA", "accB", "accB", "accC"};
        System.out.println("P5 Linear: " + linearSearch(logs, "accB"));
        Arrays.sort(logs);
        System.out.println("P5 Binary: " + binarySearch(logs, "accB"));

        // Problem 6
        int[] risks = {10, 25, 50, 100};
        System.out.println("P6 Floor(30): " + floor(risks, 30));
        System.out.println("P6 Ceil(30): " + ceil(risks, 30));
    }
}