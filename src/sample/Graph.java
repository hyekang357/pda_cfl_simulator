package sample;

import java.util.ArrayList;

abstract class Graph {

    State curr_state;
    boolean complete;
    ArrayList<Arrow> arrows;
    ArrayList<State> states;
    String input;
    int input_index;
    String output_text;

    public abstract Arrow evaluate_next();

    public boolean get_complete() { return this.complete; }
    public String get_output_text() { return this.output_text; }

    public State get_ending_state() {
        if (this.complete) {
            if (this.curr_state == null) {
                return null; // at implicit reject statement
            } else { return this.curr_state; }
        } else { return null; }
    }

    public void set_implicit_reject_state() {
        this.curr_state = null;
        this.complete = true;
    }

    public Arrow get_starting_arrow() {
        for(Arrow a: this.arrows) {
            if (a.get_from_state() == null) { return a; }
        } return null; // starting error not found
    }

    public boolean check_complete(String input, int index) {
        // System.out.println("input: " + input);
        // System.out.println(index);
        if (index >= input.length() - 1) {
            // System.out.println("this test is complete");
            return true;
        } else {
            // System.out.println("this test is not complete");
            return false;
        }
    }

}
