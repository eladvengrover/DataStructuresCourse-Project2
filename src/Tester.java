import java.util.Arrays;

public class Tester {



    public static void main(String[] args) {

        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(Integer.MIN_VALUE + 1);
        FibonacciHeap.HeapNode x =heap.insert(0);
        heap.delete(x);
        System.out.println();

    }



}
