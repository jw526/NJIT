
/*
 * Define states to be used in solving the water jugs problem.
 */

class State {
	static int[] capacity;
	int[] contents;
	State pred; // predecessor state

	static void setCapacities(int[] c) {
		capacity = c;
	}

	State(int[] c) {
		contents = new int[3];
		contents[0] = c[0];
		contents[1] = 0;
		contents[2] = 0;
	}

	public State(State state) {
		contents = new int[3];
		contents[0] = state.contents[0];
		contents[1] = state.contents[1];
		contents[2] = state.contents[2];
	}
	

	State move(int src, int dest) {
		State newState = new State(this);

		if((contents[src] == 0) || (contents[dest] == capacity[dest])==true) 
			return null;  // Checks if source is empty or if destination is full
		else{
			
			int diff = capacity[dest] - newState.contents[dest];
			
			if(newState.contents[src] >= diff) { 	// if SOURCE is greater than DEST
				newState.contents[dest] += diff; 	// fills up destination jug
				newState.contents[src] -= diff;		// subtracts fill_up from source jug
			}
			else {// else SOURCE is less than DEST
				if(newState.contents[dest] + newState.contents[src] > capacity[dest]) return null;
				newState.contents[dest] += newState.contents[src];
				newState.contents[src] = 0;
			}
		}
		newState.pred = this;
		return newState;
	}

	boolean reachedGoal(int d) {
		return(contents[0] == d || contents[1] == d || contents[2] == d);
	}

	void display() {
		System.out.println(contents[0] + " " + contents[1] + " " + contents[2]);
	}
	
}