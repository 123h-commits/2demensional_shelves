package Code.Model;

public class Rectangle {
    public Item item;
    public int facingCount;
    public int x, y;

    public Rectangle(Item item, int facingCount) {
        this.item = item;
        this.facingCount = facingCount;
    }

    public int getWidth() {
        return item.width * facingCount;
    }

    public int getHeight() {
        return item.depth;
    }

    public Rectangle copy() {
        return new Rectangle(item.copy(), facingCount);
    }
}