package Utils;

//import Model.Container;
import Model.Individual;
import Model.Item;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResultWriter {

    public static void writeToFile(String filePath, Individual best, double profit,
                                  int generations, int populationSize, double mutationRate,
                                  boolean useASS, int shelfWidth, int shelfDepth) throws IOException {
        FileWriter writer = new FileWriter(filePath);

        writer.write("=== 2DSCASP Genetic Algorithm Result ===\n");
        writer.write("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n\n");

        writer.write("== Parameters ==\n");
        writer.write(String.format("Shelf Size: %d x %d\n", shelfWidth, shelfDepth));
        writer.write(String.format("Population Size: %d\n", populationSize));
        writer.write(String.format("Mutation Rate: %.2f\n", mutationRate));
        writer.write(String.format("Generations: %d\n", generations));
        writer.write(String.format("Use ASS Initialization: %b\n", useASS));
        writer.write("\n");

        writer.write("== Shelf Arrangement ==\n");
        for (Model.Rectangle r : best.rectangles) {
            writer.write(String.format("Item %d: %d facings at (%d, %d), Width=%d, Depth=%d\n",
                    r.item.id, r.facingCount, r.x, r.y, r.getWidth(), r.getHeight()));
        }

        writer.write("\n== Profit Details ==\n");
        for (Model.Rectangle r : best.rectangles) {
            Item item = r.item;
            double spaceElasticDemand = item.minDemand * Math.pow(r.facingCount, item.spaceElasticity);
            double ooaDemand = calculateOOADemand(item, best.container.getRectangles());
            double oosDemand = calculateOOSDemand(item, best.container.getRectangles());

            writer.write(String.format("Item %d: Space Elastic Demand=%.2f, OOA=%.2f, OOS=%.2f, Contribution=%.2f\n",
                    item.id, spaceElasticDemand, ooaDemand, oosDemand,
                    r.facingCount * (item.profit - item.cost) * (spaceElasticDemand + ooaDemand + oosDemand)));
        }

        writer.write("\n== Summary ==\n");
        writer.write(String.format("Total Profit: $%.2f\n", profit));

        writer.close();
        System.out.println("✅ Results written to " + filePath);
    }

    private static double calculateOOADemand(Item item, List<Model.Rectangle> allRects) {
        double ooaDemand = 0.0;
        for (Model.Rectangle otherRect : allRects) {
            Item other = otherRect.item;
            if (other.id != item.id && !other.isSelected()) {
                double rate = other.substitutionRatesOOA.getOrDefault(item.id, 0.0);
                ooaDemand += other.minDemand * rate;
            }
        }
        return ooaDemand;
    }

    private static double calculateOOSDemand(Item item, List<Model.Rectangle> allRects) {
        double oosDemand = 0.0;
        for (Model.Rectangle otherRect : allRects) {
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