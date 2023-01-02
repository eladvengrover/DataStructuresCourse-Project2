
/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{

    private HeapNode min;
    private HeapNode first;
    private int size;

    public FibonacciHeap() {

    }

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() {
    	return first == null;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key) {
        this.size++;
    	HeapNode new_node = new HeapNode(key);
        this.getLast().updateNextNode(new_node);
        new_node.updateNextNode(this.getFirst());
        this.setFirst(new_node);
        if (this.getMin() == null || new_node.getKey() < this.getMin().getKey())
            this.setMin(new_node);
        return new_node;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin() {
        this.size--;
     	return; // should be replaced by student code
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() {
    	return this.getMin();
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2) {
        this.size += heap2.size;
        if (heap2.isEmpty())  // Case 1: heap2 is empty
            return;
        if (this.isEmpty()) {  // Case 2: this is empty
            this.setMin(heap2.getMin());
            this.setFirst(heap2.getFirst());
            return;
        }
        HeapNode currHeapLast = this.getLast();  // Case 3: both this and heap2 are not empty
        heap2.getLast().updateNextNode(this.getFirst());
        currHeapLast.updateNextNode(heap2.getFirst());
        if (heap2.getMin().getKey() < this.getMin().getKey())
          this.setMin(heap2.getMin());
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size() {
    	return this.size;
    }

    /**
     * public int getNumberOfTrees()
     *
     * Returns the number of trees in the heap.
     *
     */
    public int getNumberOfTrees() {
        HeapNode curr = this.getFirst().getNext();
        int counter = 1;
        while (curr != this.getFirst()) {
            curr = curr.getNext();
            counter++;
        }
        return counter;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    */
    public int[] countersRep() {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) {
        int delta = x.getKey() - this.getMin().getKey();
    	this.decreaseKey(x, delta + 1);
        this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() 
    {    
        return -232; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() {
        int marked = this.size() - this.nonMarked();
        return this.getNumberOfTrees() + 2 * marked;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }

    public HeapNode getMin() {
        return min;
    }

    public void setMin(HeapNode min) {
        this.min = min;
    }

    public HeapNode getFirst() {
        return first;
    }

    public void setFirst(HeapNode first) {
        this.first = first;
    }

    public HeapNode getLast() {
        return this.getFirst().getPrev();
    }

    /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;

       public HeapNode(int key) {
           this.key = key;
           this.prev = this;
           this.next = this;
       }

       public void updateNextNode(HeapNode next) {
           this.setNext(next);
           next.setPrev(this);
       }

    	public int getKey() {
    		return this.key;
    	}

       public int getRank() {
           return rank;
       }

       public boolean isMark() {
           return mark;
       }

       public HeapNode getChild() {
           return child;
       }

       public HeapNode getNext() {
           return next;
       }

       public HeapNode getPrev() {
           return prev;
       }

       public HeapNode getParent() {
           return parent;
       }

       public void setKey(int key) {
           this.key = key;
       }

       public void setRank(int rank) {
           this.rank = rank;
       }

       public void setMark(boolean mark) {
           this.mark = mark;
       }

       public void setChild(HeapNode child) {
           this.child = child;
       }

       public void setNext(HeapNode next) {
           this.next = next;
       }

       public void setPrev(HeapNode prev) {
           this.prev = prev;
       }

       public void setParent(HeapNode parent) {
           this.parent = parent;
       }
   }
}
