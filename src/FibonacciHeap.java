
/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public static final double PHI = (1 + Math.sqrt(5)) / 2;
    public static int numOfLinks;
    public static int numOfCuts;
    private HeapNode min;
    private HeapNode first;
    private int size;
    private int nonMarked;

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

    private HeapNode insert(int key, HeapNode matchingNode) {
        this.size++;
        this.nonMarked++;
        HeapNode newNode = new HeapNode(key);
        if (matchingNode != null)
            newNode.setMatchingNode(matchingNode);
        if (this.size == 1) {  // Insertion to an empty heap
            this.setFirst(newNode);
            this.setMin(newNode);
            return newNode;
        }
        this.getLast().updateNextNode(newNode); // Set new node pointers
        newNode.updateNextNode(this.getFirst());
        this.setFirst(newNode);
        if (newNode.getKey() < this.getMin().getKey()) // Update min if necessary
            this.setMin(newNode);
        return newNode;
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
        return this.insert(key, null);
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin() {
        this.size--;
        this.nonMarked--;
        if (size == 0) { // Deletion from heap with 1 element
            this.setMin(null);
            this.setFirst(null);
            return;
        }
        if (this.getNumberOfTrees() == 1) { // Deletion from heap with 1 tree and size > 1
            this.cutNodesChildrenFromParent(this.getFirst());
            this.setFirst(this.getFirst().getChild()); // Update new first
        }
        else { // Deletion from heap with more than 1 trees
            if (this.getFirst() == this.getMin()) // Edge case: first is min
                this.setFirst(
                        (this.getFirst().getChild() == null) ?
                                this.getFirst().getNext() : this.getFirst().getChild()
                );
            this.bypassMinNode();
        }
        if (this.size > 1)
            this.consolidating();
        else // If the heap contains only 1 node - make it min
            this.setMin(this.getFirst());
    }

    private void cutNodesChildrenFromParent(HeapNode node) {
        HeapNode nodeCurrChild = node.getChild();
        do {
            nodeCurrChild.setParent(null);
            if (nodeCurrChild.isMark())
                changeNodeMark(nodeCurrChild);
            nodeCurrChild = nodeCurrChild.getNext();
        } while (nodeCurrChild != node.getChild());
    }

    private void bypassMinNode() {
        HeapNode node = this.getMin();
        if (node.getChild() == null) { // Simple bypass node without children
            node.getPrev().updateNextNode(node.getNext());
            return;
        }
        this.cutNodesChildrenFromParent(node);
        HeapNode nodeLastChild = node.getChild().getPrev();
        node.getPrev().updateNextNode(node.getChild()); // Bypass node with at least 1 child
        nodeLastChild.updateNextNode(node.getNext());
    }

    private void consolidating() {
        HeapNode node = this.getFirst();
        int arrSize = (int)Math.ceil(Math.log(this.size) / Math.log(PHI));
        HeapNode[] bucketsList = new HeapNode[arrSize];
        do {
            HeapNode nodeNext = node.getNext();
            int nodeRank = node.getRank();
            if (bucketsList[nodeRank] == null) { // Case 1: no link needed
                bucketsList[nodeRank] = node;
                node = nodeNext;
                continue;
            }
            while (bucketsList[nodeRank] != null) { // Case 2: link(s) needed
                HeapNode new_node = FibonacciHeap.linkTwoTrees(node, bucketsList[nodeRank]);
                bucketsList[nodeRank] = null;
                nodeRank++;
                node = new_node;
            }
            bucketsList[nodeRank] = node; // Inserting linked node to an empty cell
            node = nodeNext;
        } while (node != this.getFirst());
        this.makeHeapFromTreesArray(bucketsList);
    }

    private void makeHeapFromTreesArray(HeapNode[] treesArray) {
        int i = 0;
        for (; i < treesArray.length; i++) {  // Find first node
            HeapNode node = treesArray[i];
            if (node != null) {
                this.setFirst(node);
                this.setMin(node);
                if (node.isMark())
                    this.changeNodeMark(node);
                i++;
                break;
            }
        }
        HeapNode node = this.getFirst();
        for (; i < treesArray.length; i++) {  // Update next for all trees roots
            HeapNode currNode = treesArray[i];
            if (currNode != null) {
                if (currNode.isMark())
                    this.changeNodeMark(currNode);
                if (currNode.getKey() < this.getMin().getKey())
                    this.setMin(currNode);
                node.updateNextNode(currNode);
                node = currNode;
            }
        }
        node.updateNextNode(this.getFirst());  // Update first tree's root prev
    }

    /**
     * private void changeNodeMark(HeapNode node)
     *
     * Swap node's mark and update nonMarked field accordingly
     *
     */
    private void changeNodeMark(HeapNode node) {
        this.nonMarked = (node.isMark()) ? this.nonMarked + 1 : this.nonMarked - 1;
        node.setMark(!node.isMark());
    }

    private static HeapNode linkTwoTrees(HeapNode a, HeapNode b) {
        numOfLinks++;
        if (a.getKey() > b.getKey()) { // If a > b then a <-> b
            HeapNode temp = b;
            b = a;
            a = temp;
        }
        if (a.getChild() != null) {
            a.getChild().getPrev().updateNextNode(b);
            b.updateNextNode(a.getChild());
        } else
            b.updateNextNode(b);
        a.setChild(b);
        b.setParent(a);
        a.setRank(a.getRank() + 1);
        return a;
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
        this.nonMarked += heap2.nonMarked;
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
        if (this.size == 0)
            return 0;
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
        if (this.size == 0)
            return new int[0];
        if (this.size == 1)
            return new int[] {1};
        int arrSize = (int)Math.ceil(Math.log(this.size) / Math.log(PHI));
        int[] ranksArray = new int[arrSize];
        ranksArray[this.getFirst().getRank()]++;
        HeapNode curr = this.getFirst().getNext();
        while (curr != this.getFirst()) {
            ranksArray[curr.getRank()]++;
            curr = curr.getNext();
        }
        int i = arrSize - 1;
        for (; i > -1; i--) {
            if (ranksArray[i] != 0)
                break;
        }
        int[] output = new int[i + 1];
        System.arraycopy(ranksArray, 0, output, 0, i + 1);

        return output;
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
    public void decreaseKey(HeapNode x, int delta) {
        x.decreaseKey(delta);
        if (x.isRoot() && x.getKey() < this.getMin().getKey()) {
            this.setMin(x); // Case 1: x is a root and its key is less than min's key
            return;
        }
        // Case 2-4: x is min, x is a root and its key is bigger than min's key, or x doesn't violate heap rules
        if (this.getMin() == x || (x.isRoot() && x.getKey() > this.getMin().getKey())
                || x.getParent().getKey() < x.getKey())
            return;
        this.cascadingCut(x, x.getParent());
    }

    private void cascadingCut(HeapNode x, HeapNode xParent) {
        this.cut(x, xParent);
        // Updating x to be first
        this.getFirst().getPrev().updateNextNode(x);
        x.updateNextNode(this.getFirst());
        this.setFirst(x);

        if (this.getMin().getKey() > x.getKey())
            this.setMin(x);
        if (!xParent.isRoot()) {
            if (!xParent.isMark() && !xParent.isRoot())
                this.changeNodeMark(xParent);
            else
                this.cascadingCut(xParent, xParent.getParent());
        }
    }

    private void cut(HeapNode x, HeapNode xParent) {
        numOfCuts++;
        x.setParent(null);
        if (x.isMark())
            this.changeNodeMark(x);
        xParent.setRank(xParent.getRank() - 1);
        if (x.getNext() == x) // x is xParent's only child
            xParent.setChild(null);
        else if(xParent.getChild() == x) { // x is xParent's leftmost child
            xParent.setChild(x.getNext());
            x.getPrev().updateNextNode(x.getNext());
        } else // x is one of xParent's other children
            x.getPrev().updateNextNode(x.getNext());
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() {
        return this.nonMarked;
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
    public static int totalLinks() {
    	return numOfLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts() {
    	return numOfCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k) {
        if (H.isEmpty())
            return new int[0];
        FibonacciHeap kHeap = new FibonacciHeap();
        int[] minKSortedArray = new int[k];
        HeapNode min = H.getMin();
        kHeap.insert(min.getKey(), min);
        for (int i = 0; i < k; i++) {
            HeapNode curr = kHeap.findMin();
            minKSortedArray[i] = curr.getKey();
            kHeap.deleteMin();
            if (curr.getMatchingNode().getChild() != null)
                addNodesChildrenToKHeap(kHeap, curr.getMatchingNode());
        }
        return minKSortedArray;
    }

    private static void addNodesChildrenToKHeap(FibonacciHeap kHeap, HeapNode node) {
        HeapNode nodeCurrChild = node.getChild();
        do {
            kHeap.insert(nodeCurrChild.getKey(), nodeCurrChild);
            nodeCurrChild = nodeCurrChild.getNext();
        } while (nodeCurrChild != node.getChild());
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
        private HeapNode matchingNode;

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

       public boolean getMarked() {
           return this.isMark();
       }

       public HeapNode getMatchingNode() {
            return matchingNode;
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

       public void setMatchingNode(HeapNode matchingNode) {
            this.matchingNode = matchingNode;
       }

       public void decreaseKey(int delta) {
           this.setKey(this.key - delta);
       }

       public boolean isRoot() {
           return this.getParent() == null;
       }

   }
}
