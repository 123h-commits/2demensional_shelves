package Code.Model;

import java.util.ArrayList;
import java.util.List;

public class Individual {
    public Container container;
    public List<Rectangle> rectangles;

    public Individual(Container container, List<Rectangle> rectangles) {
        this.container = container;
        this.rectangles = new ArrayList<>(rectangles);
    }

    /**
     * 获取个体适应度（利润）
     */
    public double getFitness(List<Item> allItems) {
        return container.calculateFitness(allItems);
    }

    /**
     * 创建当前个体的深拷贝
     */
    public Individual copy() {
        List<Rectangle> rectCopies = new ArrayList<>();
        for (Rectangle r : rectangles) {
            rectCopies.add(r.copy());
        }
        return new Individual(new Container(container.width, container.depth), rectCopies);
    }
}