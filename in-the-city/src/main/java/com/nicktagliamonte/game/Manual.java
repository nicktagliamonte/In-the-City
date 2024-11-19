package com.nicktagliamonte.game;

public class Manual {
    public void printCommands() {
        System.out.println(" ===================");
        System.out.println("||     COMMAND     ||");
        System.out.println("||      GUIDE      ||");
        System.out.println(" ===================");
        System.out.println("No command is case sensitive on any command word or argument. Caps are only used below to indicate which words are commands and arguments.");
        System.out.println("LOOK, look, and LoOk are all valid.");
        System.out.println("\"Take light leather armor\" and \"take LiGhT LeAtHeR aRmOr\" are both valid.");
        System.out.println("This game is long and is meant to be somewhat adversarial towards you, the player.");
        System.out.println("But there is a walkthrough available on GitHub if you're frustrated, or more interested in the story than the gameplay.");
        System.out.println("========================================");
        System.out.println(" ___________________");
        System.out.println("|    ORIENTATION    |");
        System.out.println("|___________________|");
        System.out.println("   -LOOK: Prints a description of your surroundings, as well as the items, characters, and pathways accessible to you.");
        System.out.println("   -LOCATE: Describes your current location and gives directions to the nearest safe zone and economic zone.");
        System.out.println("   -MENU: You used this command to get to this menu.");
        System.out.println(" ___________________");
        System.out.println("| MOVEMENT COMMANDS |");
        System.out.println("|___________________|");
        System.out.println("   -MOVE [DIRECTION]: Moves 1 step in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.");
        System.out.println("   -MOVE [DIRECTION] [NUMBER OF STEPS]: Moves [NUMBER OF STEPS] steps in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.\n\tValid NUMBERS OF STEPS are integers greater than or equal to 1.");
        System.out.println("   -MOVE TO [WAYPOINT]: Moves to the WAYPOINT specified by [WAYPOINT].\n\tValid WAYPOINTS are the names of characters, items, or pathways you currently have access to.\n\tUse LOOK for a list of such waypoints.");
        System.out.println("   -ENTER [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println("   -ASCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going up.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println("   -DESCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going down.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println(" _____________________");
        System.out.println("| OBJECT INTERACTIONS |");
        System.out.println("|_____________________|");
        System.out.println("   EXAMINE [ITEM NAME]: Gives a more detailed description of the item specified by [ITEM NAME]\n\tUse LOOK and/or INVENTORY to get a list of available items.");
        System.out.println("   TAKE [ITEM NAME]: Adds [ITEM NAME] to the player/party inventory, if there is room.");
        System.out.println("   USE [ITEM NAME]: Uses [ITEM NAME], if it has a use.");
        System.out.println("   DROP [ITEM NAME]: Removes [ITEM NAME] from inventory, if there is an item of such name in inventory");
        System.out.println("   EQUIP [ITEM NAME]: Equips [ITEM NAME] if it is a weapon, armor, or enhancement item, if there is an item of such name in inventory");
        System.out.println("   DEQUIP [ITEM NAME]: Removes [ITEM NAME] from armor slot and adds it back to inventory, if there is an item of such name currently equipped. I know dequip is technically not an English word but the game is already written with that being the word for \"stop having this item equipped\", so");
        System.out.println(" ________________________");
        System.out.println("| CHARACTER INTERACTIONS |");
        System.out.println("|________________________|");
        System.out.println("   TALK TO [CHARACTER NAME]: Enters Dialogue with [CHARACTER NAME].");
        System.out.println("   JOIN [CHARACTER NAME]: Adds [CHARACTER NAME] to the party, if there is not currently a party member of the same class as [CHARACTER NAME].\n\tThis can only be undone if [CHARACTER NAME] dies.");
        System.out.println("   FIGHT [CHARACTER NAME]: Enters Combat with [CHARACTER NAME].");
        System.out.println("   HINT [CHARACTER NAME]: Gets a hint from [CHARACTER NAME], if they have one to offer and you have one to receive.");
        System.out.println("   HIDE: Attempts to hide or conceal the presence of the party, reducing detectability and the chance of unwanted interactions.");
        System.out.println("========================================");
        System.out.println("\nEnter 1 to return to the main menu.");
    }

    public void printItems() {
        //TODO: Create list of items
    }
}
