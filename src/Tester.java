public class Tester {



    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        for (int i = 0; i < 9; i++) {
            heap.insert(i);
        }
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        System.out.println();
    }



}
