package edu.gatech.cs6310;

class Line {
    Item item;
    int quantity;
    int unitPrice;

    Line(Item item, int quantity, int unitPrice) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    int getTotalCost() {
        return this.quantity * this.unitPrice;
    }

    int getTotalWeight() {
        return this.item.weight * this.quantity;
    }

    void displayTotalWeight() {
        int totalWeight = this.getTotalWeight();
        System.out.println("total_weight:" + totalWeight);
    }
}