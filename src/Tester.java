import java.util.Arrays;

public class Tester {



    public static void main(String[] args) {

        FibonacciHeap heap = new FibonacciHeap();
        System.out.println(Arrays.toString(FibonacciHeap.kMin(heap, 0)));

        for (int i = 0; i < 65; i++) {
            heap.insert(i);
        }
        heap.deleteMin();
    }



}
