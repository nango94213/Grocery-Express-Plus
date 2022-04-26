package edu.gatech.cs6310;

interface TransactionLogClosure {
    void logTransaction(String item, int price, int quantity);
}


interface DroneOrderCompletionClosure {
    void updatePilotExperience(String pilotAccountId, int experience);
}
