package Code.Algorithm;

import java.util.List;

import Code.Model.Container;
import Code.Model.Rectangle;

public class BottomLeftFill {

    public static void pack(Container container, List<Rectangle> rectangles) {
        container.clear();
        for (Rectangle r : rectangles) {
            placeRectangle(container, r);
        }
    }

    private static void placeRectangle(Container container, Rectangle r) {
        for (int y = 0; y <= container.depth - r.getHeight(); y++) {
            for (int x = 0; x <= container.width - r.getWidth(); x++) {
                if (!isOverlap(container, x, y, r.getWidth(), r.getHeight())) {
                    r.x = x;
                    r.y = y;
                    container.add(r);
                    return;
                }
            }
        }
    }

    private static boolean isOverlap(Container container, int x, int y, int width, int height) {
        for (Rectangle placed : container.getRectangles()) {
            boolean noOverlap = (x + width <= placed.x ||
                                 placed.x + placed.getWidth() <= x ||
                                 y + height <= placed.y ||
                                 placed.y + placed.getHeight() <= y);
            if (!noOverlap) return true;
        }
        return false;
    }
}