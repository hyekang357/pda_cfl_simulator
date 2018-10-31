package sample;

public class Arrow {

    private boolean arc_arrow;

    private int x1, x2, y1, y2;
    private String text;
    private String input_text;
    private String pop_text;
    private String push_text;
    private int current_index;

    private double[] x_points, y_points;
    private final int num_points = 3;

    private State from_state, to_state;
    char direction;

    public Arrow(boolean arc_arrow, int x1, int y1, int x2, int y2, State to_state){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.arc_arrow = arc_arrow;
        this.setArrowDirection(this.x1, this.y1, this.x2, this.y2);

        if (arc_arrow) {
            this.set_x_points_arc(this.x2);
            this.set_y_points_arc(this.y2);
        } else {
            this.set_x_points_line(this.x2);
            this.set_y_points_line(this.y2);
        }
        this.text = "";
        this.from_state = null;
        this.to_state = to_state;
    }

    public Arrow(boolean arc_arrow, int x1, int y1, int x2, int y2, State from_state, State to_state, String text){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.arc_arrow = arc_arrow;
        this.setArrowDirection(this.x1, this.y1, this.x2, this.y2);

        if (arc_arrow) {
            this.set_x_points_arc(this.x2);
            this.set_y_points_arc(this.y2);
        } else {
            this.set_x_points_line(this.x2);
            this.set_y_points_line(this.y2);
        }
        this.text = text;
        this.input_text = "";
        this.pop_text = "";
        this.push_text = "";
        this.from_state = from_state;
        this.to_state = to_state;
        setInputPopPushText();
    }

    public Arrow(boolean arc_arrow, int x1, int y1, State from_state, String text){
        this.x1 = x1+5;
        this.y1 = y1-78;
        this.arc_arrow = arc_arrow;

        if (arc_arrow) {
            this.set_x_points_arc(this.x1);
            this.set_y_points_arc(this.y1);
        } else {
            this.set_x_points_line(this.x1);
            this.set_y_points_line(this.y1);
        }
        this.text = text;
        this.input_text = "";
        this.pop_text = "";
        this.push_text = "";
        this.from_state = from_state;
        this.to_state = from_state;
        setInputPopPushText();
    }

    private void setInputPopPushText() {
        String text_split[]= this.text.split("\n");
        for (String line: text_split){
            String line_split[] = line.split(  "/");
            for (int i = 0; i < 3; i++){
                if (i == 0) {
                    this.input_text += line_split[i];
                } else if (i == 1) {
                    this.pop_text += line_split[i];
                } else if (i == 2) {
                    this.push_text += line_split[i];
                }
            }
        }
    }

    private void setArrowDirection (int x1, int y1, int x2, int y2){
        if (x1 == x2 && y1 < y2)
            this.direction = 'D';
        else if (x1 == x2 && y1 > y2)
            this.direction = 'U';
        else if (y1 == y2 && x1 < x2)
            this.direction = 'R';
        else
            this.direction = 'L';
    }

    private void set_x_points_arc(int x) {
        this.x_points = new double[]{x+104, x+86, x+86};
    }

    private void set_y_points_arc(int y) {
        this.y_points = new double[]{y+78, y+85, y+64};
    }

    private void set_x_points_line(int x) {
        if(this.direction == 'R')
            this.x_points = new double[]{x+4, x-12, x-12};
        else if (this.direction == 'U')
            this.x_points = new double[] {x, x-10, x+10};
        else if (this.direction == 'D')
            this.x_points = new double[] {x, x-10, x+10};
        else
            this.x_points = new double[] {x-4, x+12, x+12};
    }

    private void set_y_points_line(int y) {
        if(this.direction == 'R')
            this.y_points = new double[]{y, y-10, y+10};
        else if (this.direction == 'U')
            this.y_points = new double[] {y-4, y+12, y+12};
        else if (this.direction == 'D')
            this.y_points = new double [] {y+4, y-12, y-12};
        else
            this.y_points = new double [] {y, y-10, y+10};
    }

    public double[] get_x_points() {
        return this.x_points;
    }

    public double[] get_y_points() {
        return this.y_points;
    }

    public int get_num_points() {
        return this.num_points;
    }

    public char get_direction() {
        return this.direction;
    }

    public int get_x1() {
        return this.x1;
    }

    public int get_y1() {
        return this.y1;
    }

    public int get_x2() {
        return this.x2;
    }

    public int get_y2() {
        return this.y2;
    }

    public String get_text() {
        return this.text;
    }

    public String get_input_text() {
        return this.input_text;
    }

    public String get_pop_text() {
        return this.pop_text;
    }

    public String get_push_text() {
        return this.push_text;
    }

    public State get_from_state() {
        return this.from_state;
    }

    public State get_to_state() {
        return this.to_state;
    }

    public boolean get_arc_arrow() {
        return this.arc_arrow;
    }

    public void set_current_index(int c) {
        this.current_index = c;
    }

    public int get_current_index() {
        return this.current_index;
    }

    public String get_text_at_current() {
        String text_split[]= this.text.split("\n");
        return text_split[this.current_index];
    }

    public String get_text_to_pop() {
        if (!this.get_text_at_current().isEmpty()) {
            String current_split[] = this.get_text_at_current().split("/");
            return current_split[1];
        } else {
            return "E";
        }
    }

    public String get_text_to_push() {
        if (!this.get_text_at_current().isEmpty()) {
            String current_split[] = this.get_text_at_current().split("/");
            return current_split[2];
        } else {
            return "E";
        }
    }
}
