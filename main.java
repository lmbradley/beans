/*
Bean Problem by LMW.

PROBLEM: Given n black and m black beans, count the total number of possible
collections of sets of beans.  The order of beans doesn't matter, i.e. a single set of
beans is defined solely based on the # of white and black beans it contains.

For example, for n = 2, m = 2, we have
WWBB
WWB, B
WBB, W
WW, BB
WB, WB
WW, B, B
WB, W, B
BB, W, W
W, W, B, B
There are 9 possible ways to split up the four beans into a collection.  As a more
extreme case, the seemingly innocuous increase to n = 20, m = 30 gives out a result of
*drum-roll*... 9,857,579,142.  Clearly a naive method, such as trying to manually
enumerate all of the sets, isn't going to work that well, is it.

SOLUTION: Define a total order over all possible sets of beans, wherein each set is
represented by an integer 0, 1, ... .  A set S_1 < S_2 if |S_1| < |S_2| or |S_1| = |S_2|
and S_1 contains more white beads than S_2.  Use dynamic programming, and define
C[b][w][g] to be the total number of configurations when we have b black beans, w white
beans and restrict the maximum possible bead set to g.  Given a possible collection C of
beans, there are two cases: C contains the set represented by g, or C does not.  If g is
not in the collection, then the total number is

C[b][w][g] = C[b][w][g - 1]

i.e. all possible configurations are already counted when we only consider sets of beans
up to g - 1.  If g is in the collection, where g has i black and j white beans, we have

C[b][w][g] = C[b - i][w - j][g]

i.e., we take all the possible collections with the remaining beans and add g to them.
Note that if we don't have enough of either the black or white beans to add g to the
collection, this number is 0.

The total number is thus C[b][w][g] = C[b][w][g - 1] + C[b - i][w - j][g].  The time
complexity is O(n^2 * m^2).

USE: Compile, then run using java main.  The algorithm will prompt for input; enter
the integers n and then m on the same line, separated by a space.
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Math;

public class main {

	public static long beans(int n, int m) {
		long C[][][] = new long[n + 1][m + 1][(n + 1) * (m + 1)];
		
		for (int b = 0; b <= n; b++) {
			for (int w = 0; w <= m; w++) {
				// C(b, w, 0) = 0 forall b, w
				C[b][w][0] = 0;
				// iterate in ascending order of g; this is done with a 2-step iteration.
				// first in group size and then # black beans.
				int size = 0, i = 0, j = 0, g = 0;
				do {
					g++;
					// C(0, 0, g) = 1 forall g
					if (b == 0 && w == 0) {
						C[b][w][g] = 1;
						continue;
					}
					// if no white beans, or if black beans are full, next group is |g|+1 group with all white beans.
					if (j == 0 || i == n) {
						size++;
						j = Math.min(size, m);
						i = size - j;
					// else remove a white bean and add a black bean.
					} else {
						i++;
						j--;
					}
					// C(b, w, g) = C(b, w, g - 1) when i < b or j < w
					if (i > b || j > w)
						C[b][w][g] = C[b][w][g - 1];
					// else C(b, w, g) = C(b, w, g - 1) + C(b - i, w - j, g)
					else
						C[b][w][g] = C[b][w][g - 1] + C[b - i][w - j][g];
					// (b + 1)(w + 1) is the maximum possible value for g.
				}	while (g < (n + 1) * (m + 1) - 1);
				
			}
		}			
		
		return C[n][m][(n + 1) * (m + 1) - 1];
	}

	public static void main(String args[]) throws IOException {	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;
		int n, m;
		
		while ((line = in.readLine()) != null) {
			if (line.isEmpty()) break;
			
			n = Integer.parseInt(line.split(" ")[0]);
			m = Integer.parseInt(line.split(" ")[1]);
			System.out.printf("Result: %d\n",beans(n, m));
		}
	}

}