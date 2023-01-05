import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tester {



    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        double start = System.currentTimeMillis();
        int m = (int) Math.pow(2, 20);
        Map<Integer, FibonacciHeap.HeapNode> nodes = new HashMap<>();
        for (int k = m - 1; k >  -2; k--) {
            nodes.put(k, heap.insert(k));
        }
        heap.deleteMin();
        int size = (int)Math.ceil(Math.log(m) / Math.log(2));
        for (int i = size; i > 0 ; i--) {
            heap.decreaseKey(nodes.get(m - (int)Math.pow(2, i) + 1), m + 1);
        }

        double end = System.currentTimeMillis();

        System.out.println("time: " + (end- start));
        System.out.println("links: " + FibonacciHeap.totalLinks());
        System.out.println("cuts: " + FibonacciHeap.totalCuts());
        System.out.println("potential: " + heap.potential());



    }



}
