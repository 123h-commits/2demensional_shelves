package Code.Algorithm;

import java.util.*;

import Code.Model.Container;
import Code.Model.Individual;
import Code.Model.Item;
import Code.Model.Rectangle;

public class GeneticAlgorithm {
    private final List<Item> items;
    private final int populationSize;
    private final double mutationRate;
    private final int generations;
    private final int shelfWidth, shelfDepth;
    private final boolean useASS;

    public GeneticAlgorithm(List<Item> items, int populationSize,
                           double mutationRate, int generations,
                           int shelfWidth, int shelfDepth, boolean useASS) {
        this.items = items;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.generations = generations;
        this.shelfWidth = shelfWidth;
        this.shelfDepth = shelfDepth;
        this.useASS = useASS;
    }

    public Individual run(List<Item> allItems) {
        List<Individual> population = generateInitialPopulation();
        Individual best = null;

        for (int i = 0; i < generations; i++) {
            evaluate(population, allItems);
            // 使用 Lambda 表达式排序
            Collections.sort(population, (i1, i2) -> Double.compare(i2.getFitness(allItems), i1.getFitness(allItems)));
            best = population.get(0);
            System.out.println("Generation " + i + " Best Fitness: " + best.getFitness(allItems));

            List<Individual> nextGen = new ArrayList<>();
            nextGen.add(best.copy());

            while (nextGen.size() < populationSize) {
                Individual parent1 = select(population);
                Individual parent2 = select(population);
                Individual child = crossover(parent1, parent2);
                mutate(child);
                nextGen.add(child);
            }

            population = nextGen;
        }

        return best;
    }

    private List<Individual> generateInitialPopulation() {
        List<Individual> pop = new ArrayList<>();

        if (useASS) {
            Container container = new Container(shelfWidth, shelfDepth);
            Individual assIndividual = generateASS(items, container);
            pop.add(assIndividual);
        }

        Random rand = new Random();
        while (pop.size() < populationSize) {
            List<Rectangle> rects = new ArrayList<>();
            for (Item item : items) {
                int facings = rand.nextInt(item.maxFacings) + 1;
                rects.add(new Rectangle(item, facings));
            }
            Individual ind = new Individual(new Container(shelfWidth, shelfDepth), rects);
            BottomLeftFill.pack(ind.container, ind.rectangles);
            pop.add(ind);
        }

        return pop;
    }

    private void evaluate(List<Individual> population, List<Item> allItems) {
        for (Individual ind : population) {
            BottomLeftFill.pack(ind.container, ind.rectangles);
            ind.getFitness(allItems); // 更新每个个体的适应度
        }
    }

    private Individual select(List<Individual> population) {
        Random rand = new Random();
        Individual p1 = population.get(rand.nextInt(population.size()));
        Individual p2 = population.get(rand.nextInt(population.size()));
        return (p1.getFitness(items) > p2.getFitness(items)) ? p1 : p2;
    }

    private Individual crossover(Individual p1, Individual p2) {
        List<Rectangle> childRects = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < p1.rectangles.size(); i++) {
            if (rand.nextBoolean()) {
                childRects.add(p1.rectangles.get(i).copy());
            } else {
                childRects.add(p2.rectangles.get(i).copy());
            }
        }

        return new Individual(new Container(shelfWidth, shelfDepth), childRects);
    }

    private void mutate(Individual ind) {
        Random rand = new Random();
        for (Rectangle r : ind.rectangles) {
            if (rand.nextDouble() < mutationRate) {
                int delta = rand.nextInt(3) - 1;
                int newFacing = Math.min(Math.max(r.facingCount + delta, 1), r.item.maxFacings);
                r.facingCount = newFacing;
            }
        }
    }

    private Individual generateASS(List<Item> items, Container container) {
        List<Rectangle> rects = new ArrayList<>();
        for (Item item : items) {
            int optimalFacings = Math.max(1, item.maxFacings / 2);
            rects.add(new Rectangle(item, optimalFacings));
        }
        BottomLeftFill.pack(container, rects);
        return new Individual(container, rects);
    }
}