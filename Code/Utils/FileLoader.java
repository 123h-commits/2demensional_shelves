package Code.Utils;

import java.io.*;
import java.util.*;

import Code.Model.Item;

public class FileLoader {
    public static List<Item> loadItems(String filePath) throws IOException {
        List<Item> items = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        reader.readLine(); // 跳过第一行字段名

        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length < 9) continue;

            int id = Integer.parseInt(parts[0]);
            double profit = Double.parseDouble(parts[1]);
            double cost = Double.parseDouble(parts[2]);
            double salvage = Double.parseDouble(parts[3]);
            double spaceElasticity = Double.parseDouble(parts[4]);
            int width = Integer.parseInt(parts[5]);
            int depth = Integer.parseInt(parts[6]);
            int minDemand = Integer.parseInt(parts[7]);
            int maxFacings = Integer.parseInt(parts[8]);

            Item item = new Item(id, profit, cost, salvage, spaceElasticity, width, depth, minDemand, maxFacings);

            // 加载 OOA 替代率（假设从第9列开始是OOA）
            for (int i = 9; i < parts.length / 2 + 1 && i < parts.length; i++) {
                double rate = Double.parseDouble(parts[i]);
                item.addSubstitution(i - 8, rate, true);
            }

            // 加载 OOS 替代率（后半部分）
            for (int i = parts.length / 2 + 1; i < parts.length; i++) {
                double rate = Double.parseDouble(parts[i]);
                item.addSubstitution(i - parts.length / 2, rate, false);
            }

            items.add(item);
        }

        reader.close();
        return items;
    }
}