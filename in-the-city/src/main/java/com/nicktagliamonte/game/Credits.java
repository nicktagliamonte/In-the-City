package com.nicktagliamonte.game;

public class Credits {
    public Credits() {
        showCredits();
    }

    public void showCredits() {
        // Displaying credits with pauses
        showMessageWithDelay("Congratulations! You've beaten the game!", 3500);

        showMessageWithDelay("\n\n--- Credits ---", 0);
        showMessageWithDelay("Lead Developer: Nick Tagliamonte", 1000);
        showMessageWithDelay("Gameplay Designer: Nick Tagliamonte", 1000);
        showMessageWithDelay("Narrative Designer: Nick Tagliamonte", 1000);
        showMessageWithDelay("Programmer: Nick Tagliamonte", 1000);
        showMessageWithDelay("Artists: Nick Tagliamonte", 1000);
        showMessageWithDelay("Quality Assurance: Stacey Chaffee", 1000);

        showMessageWithDelay("\nSpecial thanks to all the testers and supporters.", 0);
        showMessageWithDelay("This game wouldn't have been possible without Stacey Chaffee, her support and testing were vital.", 1000);

        showMessageWithDelay("\nThank you for playing! Until next time!", 0);
    }

    // Helper method for showing a message with a delay
    private void showMessageWithDelay(String message, long delayInMillis) {
        System.out.println(message);
        if (delayInMillis > 0) {
            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}