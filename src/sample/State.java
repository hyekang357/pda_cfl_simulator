package sample;

public class State {

    private boolean isAccept;
    private int x, y, size;
    private String text;

    public State(boolean isAccept, int x, int y, int size, String text) {
        this.isAccept = isAccept;
        this.x = x;
        this.y = y;
        this.size = size;
        this.text = text;
    }

    public boolean get_isAccept() {
        return this.isAccept;
    }

    public int get_x() {
        return this.x;
    }

    public int get_y() {
        return this.y;
    }

    public int get_size() {
        return this.size;
    }

    public String get_text() {
        return this.text;
    }
}
