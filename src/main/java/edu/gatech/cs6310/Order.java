package edu.gatech.cs6310;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class Order {
    String orderId;
    Customer customer;
    // Key: Item id
    private final TreeMap<String, Line> lineTreeMap = new TreeMap<>();

    Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
    }

    void addLine(Line line) {
        lineTreeMap.put(line.item.name, line);
    }

    boolean canAddNewLine(Line line) {
        // Since we cannot add duplicated item, there will be no quantity check here because it can't be added
        if (this.lineTreeMap.containsKey(line.item.name)) {
            System.out.println("ERROR:item_already_ordered");
            return false;
        }

        boolean isAffordable = this.customer.canAffordNewLine(line);
        if (!isAffordable) {
            System.out.println("ERROR:customer_cant_afford_new_item");
            return false;
        }
        return true;
    }

    int getTotalWeight() {
        int totalWeight = 0;
        for (Map.Entry<String, Line> entry : this.lineTreeMap.entrySet()) {
            Line line = entry.getValue();
            totalWeight += line.getTotalWeight();
        }
        return totalWeight;
    }

    int getTotalCost() {
        int totalCost = 0;
        for (Map.Entry<String, Line> entry : this.lineTreeMap.entrySet()) {
            Line line = entry.getValue();
            totalCost += line.getTotalCost();
        }
        return totalCost;
    }

    void displayItems() {
        for (Map.Entry<String, Line> entry : this.lineTreeMap.entrySet()) {
            Line line = entry.getValue();
            System.out.println("item_name:" + line.item.name +
                    ",total_quantity:" + line.quantity +
                    ",total_cost:" + line.getTotalCost() +
                    ",total_weight:" + line.getTotalWeight());
        }
    }

    List<Line> getLines() {
        return this.lineTreeMap.values().stream().toList();
    }

    void displayTotalWeight() {
        int totalWeight = this.getTotalWeight();
        System.out.println("total_weight:" + totalWeight);
    }

    void complete() {
        customer.completeOrder(this.orderId);
    }

    void cancel() {
        customer.cancelOrder(this.orderId);
    }
}