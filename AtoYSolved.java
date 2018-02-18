/*
 * test case
zzzzz
zutaz
zvzrz
zwxyz
zzzzz
 */

import java.util.*;


public class AtoYSolved {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		char[][] tbl = new char[5][5];
		int row = -1, col = -1;
		String inp;
		for (int i = 0; i < 5; ++i) {
			inp = sc.next();
			for (int j = 0; j < 5; ++j) {
				tbl[i][j] = inp.charAt(j);
				if (tbl[i][j] == 'a') {
					row = i;
					col = j;
				}
			}
		}

		if(solve(tbl, row, col, 'a'))
		  printTable(tbl);
	}


		//System.out.println("ascii for char a: " + (int)'a' + ascii_AtoY[0]);
        //System.out.println("ascii for char y: " + (int)'y' + ascii_AtoY[24]);


	public static boolean solve(char[][] t, int row, int col, char c) {
		//Base case
		if(c == 'y'){
			return true;
		}

		int[][] movement = {{1,0},{0,1},{-1,0},{0,-1}};

		for(int[] moves: movement){
			if((row + moves[0]>= 0 && row + moves[0]<=4) && (col + moves[1] >= 0 && col + moves[1] <= 4)){

				if(t[row + moves[0]][col + moves[1]] == (char)(c + 1)){ //if next character equal to next letter
					//printTable(t);
					//System.out.println("");

					boolean e = solve(t, row+moves[0], col+moves[1], (char)(c+1));

					if(e) return true;

					solve(t, row + moves[0], col + moves[1], (char)(c + 1));
				}
				else if(t[row+moves[0]][col+moves[1]] == 'z'){ //if next character is 'z'
					t[row + moves[0]][col + moves[1]] = (char)(c + 1);
					//printTable(t);
					//System.out.println("");

					boolean e = solve(t, row+moves[0], col+moves[1], (char)(c+1));

					if(e) return true;
					t[row+moves[0]][col+moves[1]] = 'z';
				}
			}
		}


		return false;

	}
	public static void printTable(char[][] t) {
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j)
				System.out.print(t[i][j]);
			System.out.println();
		}
	}
}
