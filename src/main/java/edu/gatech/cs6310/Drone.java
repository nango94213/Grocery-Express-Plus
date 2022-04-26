package edu.gatech.cs6310;

import java.util.Map;
import java.util.TreeMap;

class Drone {
    String id;
    int maxCapacity;
    int remainingTrip;
    int remainingCapacity;
    DronePilot pilot = null;
    private final TreeMap<String, Order> orderTreeMap = new TreeMap<>();

    Drone(String id, int capacity, int remainingTrip) {
        this.id = id;
        this.maxCapacity = capacity;
        this.remainingTrip = remainingTrip;
        this.remainingCapacity = capacity;
    }

    void addOrder(Order order) {
        this.orderTreeMap.put(order.orderId, order);
    }

    int getTotalNumberOfOrders() {
        return orderTreeMap.size();
    }

    int getCurrentTotalWeight() {
        int totalWeight = 0;
        for (Map.Entry<String, Order> entry : this.orderTreeMap.entrySet()) {
            Order order = entry.getValue();
            totalWeight += order.getTotalWeight();
        }
        return totalWeight;
    }

    boolean canAddNewItem(Line line) {
        int newLineWeight = line.getTotalWeight();
        return newLineWeight <= this.remainingCapacity;
    }

    void addLine(Line line) {
        this.remainingCapacity -= line.getTotalWeight();
    }

    boolean completeOrder(String orderId, DroneOrderCompletionClosure onDroneOrderCompleteSuccess){
        if (this.remainingTrip < 1) {
            return false;
        }

        Order order = this.orderTreeMap.get(orderId);
        this.remainingCapacity += order.getTotalWeight();

        this.remainingTrip -= 1;
        this.orderTreeMap.remove(orderId);
        this.pilot.experience += 1;
        onDroneOrderCompleteSuccess.updatePilotExperience(this.pilot.accountId, this.pilot.experience);
        return true;
    }

    void cancelOrder(String orderId) {
        Order order = this.orderTreeMap.get(orderId);
        this.remainingCapacity += order.getTotalWeight();
        this.orderTreeMap.remove(orderId);
    }

    boolean needMaintenance() {
        return this.remainingTrip <= 0;
    }
}