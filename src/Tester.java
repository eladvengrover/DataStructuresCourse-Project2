import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tester {



    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        for (int i = 5; i <= 20; i += 5) {
            double start = System.currentTimeMillis();
            int m = (int) Math.pow(2, i);
            Map<Integer, FibonacciHeap.HeapNode> nodes = new HashMap<>();
            for (int k = m - 1; k > -2; k--) {
                nodes.put(k, heap.insert(k));
            }
            heap.deleteMin();
            int size = (int) Math.ceil(Math.log(m) / Math.log(2));
            for (int j = size; j > 0; j--) {
                heap.decreaseKey(nodes.get(m - (int) Math.pow(2, j) + 1), m + 1);
            }

            double end = System.currentTimeMillis();
            System.out.println("m = 2^" + i);
            System.out.println("time: " + (end - start));
            System.out.println("links: " + FibonacciHeap.totalLinks());
            System.out.println("cuts: " + FibonacciHeap.totalCuts());
            System.out.println("potential: " + heap.potential());
        }


    }



}
