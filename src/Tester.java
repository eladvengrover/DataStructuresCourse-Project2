import java.util.Arrays;

public class Tester {



    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        for (int i = 0; i < 65; i++) {
            heap.insert(i);
        }
        heap.deleteMin();
        System.out.println(Arrays.toString(FibonacciHeap.kMin(heap, 50)));
    }



}
