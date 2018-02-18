/*
 test case
 20 5 3 4
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Scanner;

public class WaterJugs {

	public static void main(String args[]) {

		Queue<State> q = new LinkedList<State>();
		Stack<State> st = new Stack<State>();
		ArrayList<State> visited = new ArrayList<State>();
		
		Scanner scan = new Scanner(System.in);
		int[] cap = new int[3];
		
		for (int i = 0; i < 3; ++i) {		
			cap[i] = scan.nextInt();	
		}
		int d = scan.nextInt(); 
		
		State start = new State(cap); //initialize State
		State.setCapacities(cap);
		visited.add(start);
		
		q.add(start); // enqueue
		visited.add(start); // add to list
		
		while(q.peek() != null) {
			
			State next = q.poll(); // dequeue
			
			if (next.reachedGoal(d)) {
				for (State x = next; x != null; x = x.pred) 
					st.push(x);
				for (State y = next; y != null; y = y.pred) {
					st.pop().display();
				}
				break;
			}

			for(int i = 0; i <= 2; i++) 
				for(int j = 0; j <= 2; j++) {
					
					if(j == i) continue;
					
					State p = next.move(i, j);
						
					if(p == null || visited.contains(p)) continue;
						
					visited.add(p);
					q.add(p);
						
						
				}
			
		} 	
	}
}