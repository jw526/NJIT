/*
 * NOTE: # Pages faults are counted AFTER the frame allocation is initially filled
 * One of the outputs:
FIFO Page Faults: 16
LRU Page Faults: 15
OPT Page Faults: 14 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
public class pgFaults {

	public static void main(String[] args) {
		int pages[] = new int[25];
		for(int i=0; i<25; i++)
			pages[i] = (int) (Math.random()*11+1);

		int capacity = 4;
		System.out.println("FIFO Page Faults: " + FIFO(pages, pages.length, capacity));
		System.out.println("LRU Page Faults: " + LRU(pages, pages.length, capacity));
		System.out.println("OPT Page Faults: " + OPT(pages, pages.length, capacity));
	}

	static int FIFO(int pages[], int n, int capacity)
    {
        // To represent Resident Frame Set. No duplicates allowed. Unordered
        HashSet<Integer> rfs = new HashSet<>(capacity);
      
        // To store the pages in FIFO manner
        Queue<Integer> indexes = new LinkedList<>() ;
      
        // store # of page fault occurring after the frame allocation is initially filled
        int page_faults = 0;
        for (int i=0; i<n; i++)
        {
            // Check if the set can hold more pages
            if (rfs.size() < capacity)
            {
                // Insert it into set if not present
             
                if (!rfs.contains(pages[i]))
                {
                    rfs.add(pages[i]);
      
                    // Push the current page into the queue
                    indexes.add(pages[i]);
                }
            }
      
            // If the RFS is full then need to perform FIFO
            // replacement policy using the queue ADT
            else
            {
                // Check if current page is not already
                // present in the set
                if (!rfs.contains(pages[i]))
                {
                    //get its value and pop the first page from the queue
                    int val = indexes.peek();
      
                    indexes.poll();
      
                    // Remove the indexes page
                    rfs.remove(val);
      
                    // insert the current page
                    rfs.add(pages[i]);
      
                    // push the current page into
                    // the queue
                    indexes.add(pages[i]);
      
                    // Increment page faults
                    page_faults++;
                }
            }
        }
      
        return page_faults;
    }
	
    static int LRU(int pages[], int n, int capacity)
    {
        // To represent RFS, unordered set
        HashSet<Integer> rfs = new HashSet<>(capacity);
      
        // To store least recently used indexes
        // of pages.
        HashMap<Integer, Integer> indexes = new HashMap<>();
      
        //store # of page fault occurring after the frame allocation is initially filled
        int page_faults = 0;
        for (int i=0; i<n; i++)
        {
            // Check if the set can hold more pages
            if (rfs.size() < capacity)
            {
                // Insert it into set if not present
                // already which represents page fault
                if (!rfs.contains(pages[i]))
                {
                    rfs.add(pages[i]);
                }
      
                // Store the recently used index of
                // each page
                indexes.put(pages[i], i);
            }
      
            // If the set is full then need to perform lru

            else
            {
                // Check if current page is not already
                // present in the set
                if (!rfs.contains(pages[i]))
                {
                    // Find the least recently used pages
                    // that is present in the set
                    int lru = Integer.MAX_VALUE, val=Integer.MIN_VALUE;
                     
                    Iterator<Integer> itr = rfs.iterator();
                     
                    while (itr.hasNext()) {
                        int temp = itr.next();
                        if (indexes.get(temp) < lru)
                        {
                            lru = indexes.get(temp);
                            val = temp;
                        }
                    }
                 
                    // Remove the indexes page
                    rfs.remove(val);
      
                    // insert the current page
                    rfs.add(pages[i]);
      
                    // Increment page faults
                    page_faults++;
                }
      
                // Update the current page index
                indexes.put(pages[i], i);
            }
        }
      
        return page_faults;
    }
    static int OPT(int pages[], int n, int capacity) {
    	// To represent Resident Frame Set. No duplicates allowed. Unordered
        ArrayList<Integer> rfs = new ArrayList<>(capacity);
      
        // To store the pages in an ArrayList
        ArrayList<Integer> indexes = new ArrayList<>() ;
      
        // store # of page fault occurring after the frame allocation is initially filled
        int page_faults = 0;
        for (int i=0; i<n; i++)
        {
            // Check if the set can hold more pages
            if (rfs.size() < capacity)
            {
                // Insert it into set if not present
                if (!rfs.contains(pages[i]))
                {
                    rfs.add(pages[i]);
                }
                
            }
      
            // If the set is full then need to perform optimal replacement

            else
            {
            	//add the rest pages to indexes
            	for(int j=i+1; j<n; j++)
  
            	indexes.add(pages[j]);
            	
                // Check if current page is not already
                // present in the set
                if (!rfs.contains(pages[i]))
                {
                    // go through the rest and compare them one by one
                    // to the elements in rfs list. 
                	for(int k=0; k<rfs.size();k++)
                    for(int j=i+1; j<j+capacity && j<n; j++) {
                    	if (rfs.get(k)==pages[j]) continue;
                    	else {
                    		rfs.remove(k);
                    		rfs.add(indexes.get(j));
                    		
                    		break;
                    	}	
                    }
                	page_faults++;
                }
            }
        }
      
        return page_faults;
    }
}