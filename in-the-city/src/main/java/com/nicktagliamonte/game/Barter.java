package com.nicktagliamonte.game;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.items.Item;

public class Barter {
    @Expose private Player player;
    @Expose private List<Item> playerInventory;
    @Expose private List<Item> npcInventory;
    @Expose private double purchasePower;
    @Expose private boolean isNegotiator;

    public Barter(Player player, List<Item> playerInventory, List<Item> npcInventory, double purchasePower, boolean isNegotiator) {
        this.player = player;
        this.playerInventory = playerInventory;
        this.npcInventory = npcInventory;
        this.purchasePower = purchasePower;
        this.isNegotiator = isNegotiator;
        applyNegotiatorDiscount();
    }

    private void applyNegotiatorDiscount() {
        if (isNegotiator) {
            npcInventory.forEach(item -> {
                double discountedPrice = Math.floor(item.getValue() * 0.9);
                item.setValue(discountedPrice > 0 ? discountedPrice : 1);
            });
        }
    }

    public void displayInventory() {
        System.out.println("NPC Inventory:");
        for (int i = 0; i < npcInventory.size(); i++) {
            Item item = npcInventory.get(i);
            System.out.printf("%-15s %-5.2f", item.getName(), item.getValue());
            if (i % 2 == 1 || i == npcInventory.size() - 1) {
                System.out.println();
            } else {
                System.out.print("  |  ");
            }
        }
    }

    public boolean playerOffersItems(List<Item> offeredItems) {
        double offeredValue = offeredItems.stream().mapToDouble(Item::getValue).sum();

        // Ensure all offered items are in the player's inventory
        for (Item item : offeredItems) {
            if (!playerInventory.contains(item)) {
                System.out.println("You don't have " + item.getName());
                return false;
            }
        }

        // Update inventories
        playerInventory.removeAll(offeredItems);
        npcInventory.addAll(offeredItems);
        purchasePower += offeredValue;
        return true;
    }

    public boolean playerSelectsItem(Item item) {
        if (npcInventory.contains(item)) {
            if (item.getValue() <= purchasePower) {
                if (player.getRemainingCarryWeight() >= item.getWeight()) {
                    npcInventory.remove(item);
                    playerInventory.add(item);
                    purchasePower -= item.getValue();
                    return true;
                } else {
                    System.out.println("You cannot carry " + item.getName() + ".");
                }
            } else {
                System.out.println("You cannot afford " + item.getName() + ".");
            }
        }
        return false;
    }

    public double getPurchasePower() {
        return purchasePower;
    }
}