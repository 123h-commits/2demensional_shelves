package Code;
import java.util.List;

import Code.Algorithm.GeneticAlgorithm;
import Code.Model.Individual;
import Code.Utils.FileLoader;
import Code.Utils.ResultWriter;

public class Main {
    public static void main(String[] args) {
        try {
            List<Code.Model.Item> items = FileLoader.loadItems("Data/data.txt");

            int shelfWidth = 30;
            int shelfDepth = 20;
            int populationSize = 50;
            double mutationRate = 0.1;
            int generations = 100;
            boolean useASS = true;

            GeneticAlgorithm ga = new GeneticAlgorithm(
                    items, populationSize, mutationRate, generations,
                    shelfWidth, shelfDepth, useASS
            );

            Individual best = ga.run(items);

            System.out.println("\nBest Solution:");
            for (Code.Model.Rectangle r : best.container.getRectangles()) {
                System.out.printf("Item %d: %d facings at (%d, %d)\n", r.item.id, r.facingCount, r.x, r.y);
            }
            System.out.println("Total Profit: $" + best.getFitness(items));

            ResultWriter.writeToFile("Data/output.txt", best, best.getFitness(items),
                    generations, populationSize, mutationRate, useASS, shelfWidth, shelfDepth);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}