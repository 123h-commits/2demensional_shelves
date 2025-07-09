package Model;

import java.util.HashMap;
import java.util.Map;

public class Item {
    public int id;
    public double profit, cost, salvage;
    public double spaceElasticity;
    public int width, depth;
    public int minDemand, maxFacings;

    public Map<Integer, Double> substitutionRatesOOA = new HashMap<>();
    public Map<Integer, Double> substitutionRatesOOS = new HashMap<>();

    private boolean selected = true;

    public Item(int id, double profit, double cost, double salvage,
                double spaceElasticity, int width, int depth,
                int minDemand, int maxFacings) {
        this.id = id;
        this.profit = profit;
        this.cost = cost;
        this.salvage = salvage;
        this.spaceElasticity = spaceElasticity;
        this.width = width;
        this.depth = depth;
        this.minDemand = minDemand;
        this.maxFacings = maxFacings;
    }

    public void addSubstitution(int substituteId, double rate, boolean isOOA) {
        if (isOOA) {
            substitutionRatesOOA.put(substituteId, rate);
        } else {
            substitutionRatesOOS.put(substituteId, rate);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Item copy() {
        Item copy = new Item(id, profit, cost, salvage, spaceElasticity, width, depth, minDemand, maxFacings);
        copy.selected = this.selected;
        copy.substitutionRatesOOA = new HashMap<>(this.substitutionRatesOOA);
        copy.substitutionRatesOOS = new HashMap<>(this.substitutionRatesOOS);
        return copy;
    }
}