package meteordevelopment.pulsar.utils;

public class Vec4 {
    public double x, y, z, w;

    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4(double v) {
        this(v, v, v, v);
    }

    public int top() {
        return (int) Math.ceil(x);
    }
    public void top(double v) {
        this.x = v;
    }

    public int right() {
        return (int) Math.ceil(y);
    }
    public void right(double v) {
        this.y = v;
    }

    public int bottom() {
        return (int) Math.ceil(z);
    }
    public void bottom(double v) {
        this.z = v;
    }

    public int left() {
        return (int) Math.ceil(w);
    }
    public void left(double v) {
        this.w = v;
    }

    public int horizontal() {
        return (int) Math.ceil(left() + right());
    }
    public void horizontal(double v) {
        left(v);
        right(v);
    }

    public int vertical() {
        return (int) Math.ceil(bottom() + top());
    }
    public void vertical(double v) {
        bottom(v);
        top(v);
    }

    public double topLeft() {
        return x;
    }
    public void topLeft(double v) {
        this.x = v;
    }

    public double topRight() {
        return y;
    }
    public void topRight(double v) {
        this.y = v;
    }

    public double bottomRight() {
        return z;
    }
    public void bottomRight(double v) {
        this.z = v;
    }

    public double bottomLeft() {
        return w;
    }
    public void bottomLeft(double v) {
        this.w = v;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }
}
