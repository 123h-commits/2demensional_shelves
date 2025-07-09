package Model;

import java.util.ArrayList;
import java.util.List;

public class Container {
    public final int width, depth;
    private List<Rectangle> rectangles = new ArrayList<>();

    public Container(int width, int depth) {
        this.width = width;
        this.depth = depth;
    }

    public boolean place(Rectangle r, int x, int y) {
        if (x + r.getWidth() > width || y + r.getHeight() > depth) return false;
        for (Rectangle placed : rectangles) {
            if (overlap(r, placed, x, y)) return false;
        }
        r.x = x;
        r.y = y;
        rectangles.add(r);
        return true;
    }

    private boolean overlap(Rectangle a, Rectangle b, int ax, int ay) {
        return !(ax + a.getWidth() <= b.x ||
                 b.x + b.getWidth() <= ax ||
                 ay + a.getHeight() <= b.y ||
                 b.y + b.getHeight() <= ay);
    }

    public void clear() {
        rectangles.clear();
    }

    public void add(Rectangle r) {
        rectangles.add(r);
    }

    public List<Rectangle> getRectangles() {
        return new ArrayList<>(rectangles);
    }

    public double calculateFitness(List<Item> allItems) {
        double totalProfit = 0;
        for (Rectangle r : rectangles) {
            Item item = r.item;

            double spaceElasticDemand = item.minDemand * Math.pow(r.facingCount, item.spaceElasticity);
            double ooaDemand = calculateOOADemand(item, allItems);
            double oosDemand = calculateOOSDemand(item, rectangles);

            double expectedDemand = spaceElasticDemand + ooaDemand + oosDemand;
            totalProfit += r.facingCount * (item.profit - item.cost) * expectedDemand;
        }
        return totalProfit;
    }

    private double calculateOOADemand(Item item, List<Item> allItems) {
        double ooaDemand = 0.0;
        for (Item other : allItems) {
            if (other.id != item.id && !other.isSelected()) {
                double rate = other.substitutionRatesOOA.getOrDefault(item.id, 0.0);
                ooaDemand += other.minDemand * rate;
            }
        }
        return ooaDemand;
    }

    private double calculateOOSDemand(Item item, List<Rectangle> allRects) {
        double oosDemand = 0.0;
        for (Rectangle otherRect : allRects) {
            Item other = otherRect.item;
            if (other.id == item.id || !other.isSelected()) continue;

            double rate = other.substitutionRatesOOS.getOrDefault(item.id, 0.0);
            double expectedDemand = other.minDemand * Math.pow(otherRect.facingCount, other.spaceElasticity);
            double shortage = Math.max(0, expectedDemand - otherRect.facingCount);
            oosDemand += shortage * rate;
        }
        return oosDemand;
    }
}