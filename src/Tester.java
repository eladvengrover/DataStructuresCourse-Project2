import java.util.Arrays;

public class Tester {



    public static void main(String[] args) {

        FibonacciHeap heap = new FibonacciHeap();
        for (int i = 0; i < 4; i++) {
            heap.insert(i);
        }
        FibonacciHeap.HeapNode x = heap.insert(4);
        heap.deleteMin();
        heap.delete(x);
        FibonacciHeap.HeapNode node = heap.getFirst().getChild().getNext();

    }



}
