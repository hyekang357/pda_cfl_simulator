package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.shape.ArcType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.Arrays;
import java.util.ArrayList;

public class Controller {

    public TextArea TextAreaInput;
    public TextArea TextAreaOutput;
    public TextArea TextAreaStack;
    public Canvas myCanvas;
    GraphicsContext GC;
    final double DWidth = 5.0;

    public PDA Test;
    Arrow PrevArrow;

    private void initializeCanvas() {
        GC = myCanvas.getGraphicsContext2D();
        GC.clearRect(0, 0, 918.0, 483.0);
        GC.setLineWidth(DWidth);
        GC.setFont(new Font(16));
        TextAreaOutput.setFont(new Font("Arial", 24));
        TextAreaOutput.setText("");
        TextAreaStack.setFont(new Font("Arial", 36));
        TextAreaStack.setText("");
        Test = null;
        PrevArrow = null;
    }

    private void drawState(State q) {
        if (q.get_isAccept()) {
            drawAcceptState(q.get_x(), q.get_y(), q.get_size());
        } else {
            drawRejectState(q.get_x(), q.get_y(), q.get_size());
        }
        if (!q.get_text().isEmpty()) {
            drawTextInCircle(q.get_x(), q.get_y(), q.get_size(), q.get_text());
        }
    }

    private void highlightState(State q) {
        if (q != null) {
            GC.setStroke(Color.RED);
            GC.setFill(Color.RED);
            if (q.get_isAccept()) {
                drawAcceptState(q.get_x(), q.get_y(), q.get_size());
            } else {
                drawRejectState(q.get_x(), q.get_y(), q.get_size());
            }
            GC.setStroke(Color.BLACK);
            GC.setFill(Color.BLACK);
        }
    }

    private void drawAcceptState(int x, int y, int size) {
        GC.strokeOval(x, y, size, size);
        GC.strokeOval(x-8, y-8, size+16, size+16);
    }

    private void drawRejectState(int x, int y, int size) {
        GC.strokeOval(x-8, y-8, size+16, size+16);
    }

    private void drawTextInCircle(int x, int y, int size, String s) {
        GC.setLineWidth(1.0);
        GC.setFont(new Font("Arial", 24));
        GC.fillText(s, x+(size*0.4), y+(size*0.55));
        GC.setLineWidth(DWidth);
    }

    private void drawArrowToItself(Arrow a) {
        int size = 100;
        GC.strokeArc(a.get_x1(), a.get_y1(), size, size, 320, 258, ArcType.OPEN);
        GC.fillPolygon(a.get_x_points(), a.get_y_points(), a.get_num_points());
        // write text
        GC.setLineWidth(1.0);
        GC.setFont(new Font("Arial", 24));
        GC.fillText(a.get_text(), a.get_x1()+(size*1.05), a.get_y1()+(size*0.2));
        GC.setLineWidth(DWidth);
    }

    private void drawArrowLine(Arrow a) {
        GC.strokeLine(a.get_x1(), a.get_y1(), a.get_x2(), a.get_y2());
        GC.fillPolygon(a.get_x_points(), a.get_y_points(), a.get_num_points());
        if (!a.get_text().isEmpty()) {
            GC.setLineWidth(1.0);
            GC.setFont(new Font("Arial", 24));
            if(a.get_direction() == 'R')
                GC.fillText(a.get_text(), (a.get_x1()+a.get_x2()-60)/2, ((a.get_y1()+a.get_y2())/2)+30);
            else if(a.get_direction() == 'U')
                GC.fillText(a.get_text(), (((a.get_x1()+a.get_x2())/2)-30), (a.get_y1()+a.get_y2()+10)/2);
            else if(a.get_direction() == 'D')
                GC.fillText(a.get_text(), (a.get_x1()+a.get_x2()+30)/2, ((a.get_y1()+a.get_y2())/2)+8);
            else
                GC.fillText(a.get_text(), (a.get_x1()+a.get_x2()-14)/2, ((a.get_y1()+a.get_y2())/2)+20);
            GC.setLineWidth(DWidth);
        }
    }

    private void highlightArrow(Arrow a) {
        //set stroke color to red
        GC.setStroke(Color.RED);
        GC.setFill(Color.RED);
        // draw arrow
        if (a.get_arc_arrow()) {
            int size = 100;
            GC.strokeArc(a.get_x1(), a.get_y1(), size, size, 320, 258, ArcType.OPEN);
            GC.fillPolygon(a.get_x_points(), a.get_y_points(), a.get_num_points());
        } else {
            GC.strokeLine(a.get_x1(), a.get_y1(), a.get_x2(), a.get_y2());
            GC.fillPolygon(a.get_x_points(), a.get_y_points(), a.get_num_points());
        }
        // reset
        GC.setStroke(Color.BLACK);
        GC.setFill(Color.BLACK);
    }

    private String popOrPushStack(Arrow a) {
        String error = "";
        // Get the content of Stack
        String stack = TextAreaStack.getText();
        // Check if there is a need to pop
        if (!a.get_text_to_pop().equals("E")) {
            // check if the top of the stack equals the text_to_pop
            String stack_split[] = stack.split("\n");
            String top = stack_split[0];
            // if so remove
            if (top.equals(a.get_text_to_pop())) {
                // remove top and set Stack
                String temp = "";
                for (int i = 1; i < stack_split.length; i++){
                    temp += stack_split[i] + "\n";
                }
                TextAreaStack.setText(temp);
                // update TextAreaOutput
                TextAreaOutput.setText("\t\t\t      ---------------- Popping " + a.get_text_to_pop() + " from the stack\n" + TextAreaOutput.getText());
            } else {
                error = "Error: text to pop does not equal to of the stack";
                System.out.println(error);
                this.Test.set_implicit_reject_state();
            }
            // if not, return error
        }
        // Check if there is a need to push
        if (!a.get_text_to_push().equals("E")) {
            // add to the stack
            stack = a.get_text_to_push() + "\n" + stack;
            TextAreaStack.setText(stack);
            TextAreaOutput.setText("\t\t\t      ---------------- Pushing " + a.get_text_to_push() + " to the stack\n" + TextAreaOutput.getText());

        }
        return error;
    }

    private void removeHighlightArrow() {
        if (PrevArrow != null) {
            if (PrevArrow.get_arc_arrow()) {
                int size = 100;
                GC.strokeArc(PrevArrow.get_x1(), PrevArrow.get_y1(), size, size, 320, 258, ArcType.OPEN);
                GC.fillPolygon(PrevArrow.get_x_points(), PrevArrow.get_y_points(), PrevArrow.get_num_points());
            } else {
                GC.strokeLine(PrevArrow.get_x1(), PrevArrow.get_y1(), PrevArrow.get_x2(), PrevArrow.get_y2());
                GC.fillPolygon(PrevArrow.get_x_points(), PrevArrow.get_y_points(), PrevArrow.get_num_points());
            }
        }
    }

    private String uniqueCharacters(String input) {
        String temp = "";
        for (int i = 0; i < input.length(); i++){
            char current = input.charAt(i);
            if (temp.indexOf(current) < 0){
                temp += current;
            }
        }
        return temp;
    }

    private String getArcArrowPushLabel(String unique) {
        String temp = "";
        for (int i = 0; i < unique.length(); i++){
            char current = unique.charAt(i);
            temp += current + "/E/" + current + "\n";
        }
        return temp;
    }

    private String getArcArrowPopLabel(String unique) {
        String temp = "";
        for (int i = 0; i < unique.length(); i++){
            char current = unique.charAt(i);
            temp += current + "/" + current + "/E\n";
        }
        return temp;
    }

    private String getNextStateArrowLabel(String unique) {
        String temp = "E/E/E\n";
        for (int i = 0; i < unique.length(); i++){
            char current = unique.charAt(i);
            temp += current + "/E/E\n";
        }
        return temp;
    }

    public void drawTest1() {
        System.out.println("Drawing Test 1...");
        // Initialize canvas
        initializeCanvas();

        // Get input from input box
        String input = TextAreaInput.getText();
        System.out.println("Testing Test1 with given input: " + input);
        // Get the unique character set
        String uniqueChar = uniqueCharacters(input);
        System.out.println("unique char: " + uniqueChar);

        // Generate transition to itself arrow's label
        String arcArrowPushLabel = getArcArrowPushLabel(uniqueChar);
        String arcArrowPopLabel = getArcArrowPopLabel(uniqueChar);

        // Generate transition to next state arrow's label
        String nextStateArrowLabel = getNextStateArrowLabel(uniqueChar);

        int x = ((int) (GC.getCanvas().getWidth() / 2)) - 300;
        int y = 0;

        // Create states
        State q0 = new State(false, 208 + x, 208 + y, 100, "q0");
        State q1 = new State(true, 408 + x, 208 + y, 100, "q1");

        // Create arrows
        Arrow a0 = new Arrow(false, 100 + x, 258 + y, 196 + x, 258 + y, q0);
        Arrow a1 = new Arrow(true, 208 + x, 208 + y, q0, arcArrowPushLabel);
        Arrow a2 = new Arrow(false, 318 + x, 258 + y, 396 + x, 258 + y, q0, q1, nextStateArrowLabel);
        Arrow a3 = new Arrow(true, 408 + x, 208 + y, q1, arcArrowPopLabel);

        // Draw the objects
        drawArrowLine(a0);
        //q0
        drawState(q0);
        drawArrowToItself(a1);
        // draw q0 -> q1 arrow
        drawArrowLine(a2);
        //q1
        drawState(q1);
        drawArrowToItself(a3);

        // Create Test object
        ArrayList<Arrow> arrows = new ArrayList<>();
        arrows.addAll(Arrays.asList(a0, a1, a2, a3));
        ArrayList<State> states = new ArrayList<>();
        states.addAll(Arrays.asList(q0, q1));

        // Create test object
        this.Test = new PDA(arrows, states, input);

    }

    public void drawTest2() {
        System.out.println("Drawing Test 2...");
        // Initialize canvas
        initializeCanvas();

        // Get input from input box
        String input = TextAreaInput.getText();
        System.out.println("Testing Test2 with given input: " + input);
        // Get the unique character set
        String uniqueChar = uniqueCharacters(input);

        System.out.println("unique char: " + uniqueChar);

        // Generate transition to itself arrow's label
        String arcArrowPushLabel = getArcArrowPushLabel(uniqueChar);
        String arcArrowPopLabel = getArcArrowPopLabel(uniqueChar);

        // Generate transition to next state arrow's label
        String nextStateArrowLabel = getNextStateArrowLabel(uniqueChar);

        int x = ((int) (GC.getCanvas().getWidth() / 2)) - 300;
        int y = 0;

        // Create states
        State q0 = new State(false, 208 + x, 208 + y, 100, "q0");
        State q1 = new State(true, 408 + x, 208 + y, 100, "q1");

        // Create arrows
        Arrow a0 = new Arrow(false, 100 + x, 258 + y, 196 + x, 258 + y, q0);   //Starting Arrow
        Arrow a1 = new Arrow(true, 208 + x, 208 + y, q0, "0/E/0\n"); // q0 self-arrow
        Arrow a2 = new Arrow(false, 318 + x, 258 + y, 396 + x, 258 + y, q0, q1, "E/E/E\n1/0/E"); // q0 to q1 arrow
        Arrow a3 = new Arrow(true, 408 + x, 208 + y, q1, "1/0/E\n"); //q1 self-arrow

        // Draw the objects
        drawArrowLine(a0);
        //q0
        drawState(q0);
        drawArrowToItself(a1);
        // draw q0 -> q1 arrow
        drawArrowLine(a2);
        //q1
        drawState(q1);
        drawArrowToItself(a3);

        // Create Test object
        ArrayList<Arrow> arrows = new ArrayList<>();
        arrows.addAll(Arrays.asList(a0, a1, a2, a3));
        ArrayList<State> states = new ArrayList<>();
        states.addAll(Arrays.asList(q0, q1));

        // Create test object
        this.Test = new PDA(arrows, states, input);

    }

    public void drawTest3() {
        System.out.println("Drawing Test 3...");
        // Initialize canvas
        initializeCanvas();
    }

    public void clearCanvas() {
        initializeCanvas();
    }

    public void clickNext() {
        // if complete, highlight last state
        System.out.println("Next is clicked");
        if (this.Test != null) {
            if (this.Test.get_complete()) {
                System.out.println("COMPLETE!!");
                removeHighlightArrow();
                State ending_state = this.Test.get_ending_state();
                if (ending_state == null) {
                    TextAreaOutput.setText("The input is rejected\n" + TextAreaOutput.getText());
                } else {
                    TextAreaOutput.setText("Ending state: " + ending_state.get_text() + "\n" + TextAreaOutput.getText());
                    if (TextAreaStack.getText().equals("")) {
                        TextAreaOutput.setText("The input is accepted\n" + TextAreaOutput.getText());
                    } else {
                        TextAreaOutput.setText("T`he stack is not empty -------- input is rejected\n" + TextAreaOutput.getText());
                    }
                    highlightState(ending_state);
                }
                TextAreaOutput.setText("Complete\n" + TextAreaOutput.getText());
            }
            // if not complete, highlight next arrow
            else {
                System.out.println("evaluating next");
                Arrow next = this.Test.evaluate_next();
                if (next != null) {
                    removeHighlightArrow();
                    highlightArrow(next);
                    TextAreaOutput.setText(this.Test.get_output_text());
                    popOrPushStack(next);
                    this.Test.set_output_text(TextAreaOutput.getText());
                    TextAreaOutput.setText(this.Test.get_output_text());
                    this.PrevArrow = next;
                } else {
                    // at implicit reject state
                    removeHighlightArrow();
                    TextAreaOutput.setText(this.Test.get_output_text());
                    this.PrevArrow = null;
                }
            }
        }
    }
}
