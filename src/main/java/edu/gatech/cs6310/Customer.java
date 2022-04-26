package edu.gatech.cs6310;

import java.util.Map;
import java.util.TreeMap;

public class Customer extends User {

    String accountId;
    int rating;
    int credit;
    private TreeMap<String, Order> orderTreeMap = new TreeMap<>();

    public Customer(String accountId, String firstName, String lastName, String phoneNumber, int rating, int credit) {
        super(firstName, lastName, phoneNumber);
        this.accountId = accountId;
        this.rating = rating;
        this.credit = credit;
    }

    void addNewOrder(Order order) {
        orderTreeMap.put(order.orderId, order);
    }

    int getTotalCost() {
        int totalCost = 0;
        for (Map.Entry<String, Order> entry : this.orderTreeMap.entrySet()) {
            Order order = entry.getValue();
            totalCost += order.getTotalCost();
        }
        return totalCost;
    }

    boolean canAffordNewLine(Line line) {
        int totalCost = line.getTotalCost();
        int currentTotalCost = this.getTotalCost();
        return totalCost + currentTotalCost <= this.credit;
    }

    void completeOrder(String orderId) {
        Order order = this.orderTreeMap.get(orderId);
        this.credit -= order.getTotalCost();
        this.orderTreeMap.remove(orderId);
    }

    void cancelOrder(String orderId) {
        this.orderTreeMap.remove(orderId);
    }
}