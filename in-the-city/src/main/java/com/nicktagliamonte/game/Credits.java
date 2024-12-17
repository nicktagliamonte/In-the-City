package com.nicktagliamonte.game;

public class Credits {
    public Credits() {
        showCredits();
    }
    public void showCredits() {

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Congratulations! You've beaten the game!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n\n--- Credits ---");
        
        System.out.println("Lead Developer: Nick Tagliamonte");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Gameplay Designer: Nick Tagliamonte");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Narrative Designer: Nick Tagliamonte");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Programmer: Nick Tagliamonte");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Artists: Nick Tagliamonte");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Quality Assurance: Stacey Chaffee");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nSpecial thanks to all the testers and supporters.");
        System.out.println("This game wouldn't have been possible without Stacey Chaffee, her support and testing were vital.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nThank you for playing! Until next time!");
    }
}
