package sample;

import java.util.ArrayList;

public class CFL {

	String input;
	ArrayList<String> combinations;

	public CFL(String input) {
		this.input = input;

		char[] tokens = split();
		//Build combinations like RSR, ASA etc..
		combinations = getCombinations(tokens);
		
		//Build combination like R, A, C, E
		for (int i = 0; i < tokens.length; i++) {
			combinations.add(String.valueOf(tokens[i]));
		}
		
		combinations.add("E");
	}

	public ArrayList<String> getCombinations() {
		return combinations;
	}

	private char[] split() {
		return input.toCharArray();
	}

	private ArrayList<String> getCombinations(char[] tokens) {
		ArrayList<String> ar = new ArrayList<String>();
		for (int i = 0; i < tokens.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(tokens[i]);
			sb.append('S');
			sb.append(tokens[i]);
			String str = sb.toString();
			ar.add(str);
		}

		return ar;
	}
}
