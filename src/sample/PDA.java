package sample;

import java.util.ArrayList;

public class PDA {

    State curr_state;
    boolean complete;
    ArrayList<Arrow> arrows;
    ArrayList<State> states;
    String input;
    int input_index;
    String output_text;

    boolean isEven;
    String transition_text;
    int transition_index;

    public PDA(ArrayList<Arrow> arrows, ArrayList<State> states, String input) {
        this.arrows = arrows;
        this.states = states;
        this.input = input;
        this.curr_state = null;
        this.input_index = -1;
        this.complete = false;
        this.output_text = "";
        this.isEven = getIsEven(input);
        this.transition_text = getTransitionText(input, this.isEven);
        this.transition_index = getTransitionIndex(input);
    }
    public String get_output_text() { return this.output_text; }
    public void set_output_text(String s) { this.output_text = s; }
    public boolean get_complete() { return this.complete; }

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

    public boolean getIsEven(String i) {
        if (i.length() % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getTransitionText(String i, boolean isEven) {
        if (isEven) {
            return "E";
        } else {
            return Character.toString(i.charAt(getTransitionIndex(i)));
        }
    }

    public int getTransitionIndex(String i) {
        return (i.length() / 2);
    }

    public Arrow evaluate_next() {
        // check the current state; if this is the very beginning
        if (this.curr_state == null && !this.complete) {
            // get the starting arrow
            Arrow starting_arrow = get_starting_arrow();
            // set the curr_state
            this.curr_state = starting_arrow.get_to_state();
            System.out.println("Current state is set to: " + this.curr_state.get_text());
            // set complete
            this.complete = check_complete(this.input, this.input_index);
            this.input_index++;
            this.output_text = "Starting at state q0\n";
            return starting_arrow;
        } else if (this.curr_state != null && !this.complete) {
            // Get input at index
            String evaluating_input = Character.toString(this.input.charAt(this.input_index));
            System.out.println("Evaluating input: " + evaluating_input);
            // Check if at transition state
            Arrow next_arrow = null;
            if (this.input_index == this.transition_index) {
                // Get next arrow
                next_arrow = get_next_arrow(this.curr_state, evaluating_input, true);
                // if next_arrow is not null set curr_state, increment input_index, set_complete
                if (next_arrow != null) {
                    this.curr_state = next_arrow.get_to_state();
                    this.complete = check_complete(this.input, this.input_index);
                    if (!isEven) {
                        this.input_index++;
                    } else {
                        evaluating_input = "E";
                    }
                    this.output_text = "Evaluating input: " + evaluating_input + " --------- Move to state: " + this.curr_state.get_text() + "\n" + this.output_text;
                    return next_arrow;
                } else {
                    // if next_arrow not found then implicit reject state
                    // System.out.println("At implicit reject state");
                    set_implicit_reject_state();
                    this.output_text = "Evaluating input: " + evaluating_input + " --------- Rejected\n" + this.output_text;
                    return null;
                }
            } else {
                // Get next arrow
                next_arrow = get_next_arrow(this.curr_state, evaluating_input, false);
                // if next_arrow is not null set curr_state, increment input_index, set_complete
                if (next_arrow != null) {
                    this.curr_state = next_arrow.get_to_state();
                    this.complete = check_complete(this.input, this.input_index);
                    this.input_index++;
                    this.output_text = "Evaluating input: " + evaluating_input + " --------------- Move to state: " + this.curr_state.get_text() + "\n" + this.output_text;
                    return next_arrow;
                } else {
                    // if next_arrow not found then implicit reject state
                    // System.out.println("At implicit reject state");
                    set_implicit_reject_state();
                    this.output_text = "Evaluating input: " + evaluating_input + " --------------- Move to implicit reject state\n" + this.output_text;
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public Arrow get_starting_arrow() {
        for(Arrow a: this.arrows) {
            if (a.get_from_state() == null) { return a; }
        } return null; // starting error not found
    }

    public boolean check_complete(String input, int index) {
        if (index >= input.length() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private Arrow get_next_arrow(State current_state, String evaluating_input, boolean isAtTransition) {
        boolean isNotFound = true;
        if (isAtTransition) {
            // loop through all the arrows and find the arrow that connects to q1
            for (Arrow a: this.arrows) {
                a.set_current_index(-1);
                // if arrow is a transition arrow
                if (a.get_from_state() != null && a.get_to_state().get_text() != a.get_from_state().get_text()) {
                    if (this.isEven) { evaluating_input = "E"; }
                    // System.out.println("checking arrow from state: ->" + a.get_from_state().get_text() + "<-");
                    // System.out.println("    with arrow text: ->" + a.get_text() + "<-");
                    // if arrow is from current state
                    if (a.get_from_state().get_text().equals(current_state.get_text())) {
                        String input_text = a.get_input_text();
                        for (int i = 0; i < input_text.length(); i++) {
                            if (evaluating_input.equals(Character.toString(input_text.charAt(i)))) {
                                a.set_current_index(i);
                                System.out.println("found next arrow");
                                return a;
                            }
                        }
                    }
                }
            }
        }
        if (isNotFound) {
            // find the arrow that is from s, text of evaluating char
            // System.out.println("Looking for next arrow from curr_state ->" + current_state.get_text() + "<-");
            // System.out.println("    with arrow value of ->" + evaluating_input + "<-");
            for (Arrow a: this.arrows) {
                a.set_current_index(-1);
                if (a.get_from_state() != null) {
                    // System.out.println("checking arrow from state: ->" + a.get_from_state().get_text() + "<-");
                    // System.out.println("    with arrow text: ->" + a.get_text() + "<-");
                    // if arrow is from current state
                    if (a.get_from_state().get_text().equals(current_state.get_text())) {
                        String input_text = a.get_input_text();
                        for (int i = 0; i < input_text.length(); i++) {
                            if (evaluating_input.equals(Character.toString(input_text.charAt(i)))) {
                                a.set_current_index(i);
                                System.out.println("found next arrow");
                                return a;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("did not find next arrow");
        return null;
    }






}