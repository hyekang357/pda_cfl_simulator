package sample;

import java.util.ArrayList;

public class CFL {

	String input;
	String orig;
	ArrayList<String> combinations;
	int evalIndex = 0;

	public CFL(String input, String orig) {
		this.input = input;
		this.orig = orig;

		char[] tokens = split();
		Boolean even = false;

		if ((orig.length() % 2) == 0) {
			even = true;
			// System.out.println("even " + tokens.length);
		} else {
			// System.out.println("odd");
		}
		// Build combinations like RSR, ASA etc..
		combinations = getCombinations(tokens, even);

		// Build combination like R, A, C, E
		for (int i = tokens.length - 1; i >= 0; i--) {
			combinations.add(String.valueOf(tokens[i]));
		}

		combinations.add("E");
	}

	public ArrayList<String> getCombinations() {
		return combinations;
	}

	public String evalNext() {
		if (evalIndex == -1) {
			return "";
		}

		char[] origToken = orig.toCharArray();
		Boolean even = false;
		if ((orig.length() % 2) == 0) {
			even = true;
		}
		if (evalIndex <= combinations.size()) {
			if (evalIndex == 0) {
				String r = "Step 1" + " : " + combinations.get(evalIndex);
				if (origToken[evalIndex] != origToken[orig.length() - 1]) {
					r += "\n Reject";
					evalIndex = -1;
				} else {
					evalIndex++;
				}
				return r;
			} else {

				StringBuilder prev = new StringBuilder();
				for (int i = 0; i <= evalIndex; ++i) {
					prev.append(origToken[i]);
				}
				String r = "Step " + (evalIndex + 1) + " : " + prev + "S" + prev.reverse();
				if (origToken[evalIndex] != origToken[orig.length() - (evalIndex + 1)]) {
					r += "\n Reject";
					evalIndex = -1;
				} else {
					evalIndex++;

					if (even == false) {
						if (evalIndex > orig.length() / 2) {
							r = "Output : " + orig;
							evalIndex = -1;
						}
					}
					else {
						if (evalIndex == orig.length() / 2) {
							r = "Output : " + orig;
							evalIndex = -1;
						}
					}
				}

				return r;

				/*
				 * StringBuilder ret = new StringBuilder(); for (int i = 0; i <= evalIndex; ++i)
				 * { if (combinations.get(i).length() == 1) { eventerminate = true;
				 * System.out.println("terminate"); break; }
				 * 
				 * ret.append(combinations.get(i).substring(0, 1));
				 * 
				 * if (combinations.get(i).substring(1, 2).compareTo("*") == 0) { oddterminate =
				 * true; System.out.println("terminate"); break; } } evalIndex++; String result
				 * = ""; if (oddterminate) { result = "Output" + " : "; evalIndex = -1; result
				 * += ret + "" + ret.replace(ret.length() - 1, ret.length(), "").reverse(); }
				 * else if (eventerminate) { result = "Output" + " : "; evalIndex = -1; result
				 * += ret + "" + ret.reverse(); } else { result = "Step " + evalIndex + " : ";
				 * result += ret + "S" + ret.reverse(); } return result;
				 */
			}
		}
		return "";
	}

	public char[] split() {
		return input.toCharArray();
	}

	public void combinationUtil(ArrayList<String> out, char arr[], char data[], int start, int end, int index, int r) {
		// Current combination is ready to be printed, print it
		if (index == r) {
			StringBuilder sl = new StringBuilder();
			for (int j = 0; j < r; j++) {
				// System.out.print(data[j] + " ");
				sl.append(String.valueOf(data[j]));
			}
			out.add(sl.toString());
			// System.out.println("");
			return;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(out, arr, data, i + 1, end, index + 1, r);
		}
	}

	private ArrayList<String> getCombinations(char[] tokens, Boolean even) {
		ArrayList<String> ar = new ArrayList<String>();
		for (int i = 0; i < tokens.length; i++) {
			if (i == tokens.length - 1 && even == false) {
				// StringBuilder sb = new StringBuilder();
				// sb.append(tokens[i]);
				// sb.append('*');
				// String str = sb.toString();
				// ar.add(str);

			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(tokens[i]);
				sb.append('S');
				sb.append(tokens[i]);
				String str = sb.toString();
				ar.add(str);
			}
		}

		return ar;
	}
}
