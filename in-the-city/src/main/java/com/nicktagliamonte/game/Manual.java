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
        System.out.println("          Room names will be displayed in quotes.");
        System.out.println("   -LOCATE: Describes your current location and gives directions to the nearest safe zone and economic zone.");
        System.out.println("   -MENU: You used this command to get to this menu.");
        System.out.println(" ___________________");
        System.out.println("| MOVEMENT COMMANDS |");
        System.out.println("|___________________|");
        System.out.println("   -MOVE [DIRECTION]: Moves 1 step in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.");
        System.out.println("   -MOVE [DIRECTION] [NUMBER OF STEPS]: Moves [NUMBER OF STEPS] steps in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.\n\tValid NUMBERS OF STEPS are integers greater than or equal to 1.");
        System.out.println("   -MOVE TO [WAYPOINT]: Moves to the WAYPOINT specified by [WAYPOINT].\n\tValid WAYPOINTS are the names of characters, items, or pathways you currently have access to.\n\tUse LOOK for a list of such waypoints.");
        System.out.println("   -ENTER [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location.\n\tCurrently available ROOM NAMES can be found using the LOOK command.\n\tThis command will word for rooms separated by a staircase or ladder or otherwise above or below your current position, as well as rooms seperated by a doorway or otherwise on your same level.");
        System.out.println("   -ASCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going up.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println("   -DESCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going down.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println(" _____________________");
        System.out.println("| OBJECT INTERACTIONS |");
        System.out.println("|_____________________|");
        System.out.println("   EXAMINE [ITEM NAME]: Gives a more detailed description of the item specified by [ITEM NAME]\n\tUse LOOK and/or INVENTORY to get a list of available items.");
        System.out.println("   TAKE [ITEM NAME]: Adds [ITEM NAME] to the player/party inventory, if there is room.\n\tIf you are in the safe zone, the game will first try to take the item from the safe zone before moving onto general items in the room.\n\tOtherwise, the game will just take an item of the name you specify off the floor, as long as it is available.");
        System.out.println("   USE [ITEM NAME]: Uses [ITEM NAME], if it has a use.");
        System.out.println("   DROP [ITEM NAME]: Removes [ITEM NAME] from inventory, if there is an item of such name in inventory");
        System.out.println("   EQUIP [ITEM NAME]: Equips [ITEM NAME] if it is a weapon, armor, or enhancement item, if there is an item of such name in inventory");
        System.out.println("   DEQUIP [ITEM NAME]: Removes [ITEM NAME] from armor slot and adds it back to inventory, if there is an item of such name currently equipped.\n\tI know dequip is technically not an English word but the game is already written with that being the word for \"stop having this item equipped\", so");
        System.out.println("   TRANSFER [ITEM NAME]: Transfers [ITEM NAME] from your inventory to the safe zone inventory, if you are currently in the safe zone.");
        System.out.println("   TRANSFER ALL: Transfers every item in your inventory from your inventory to the safe zone inventory, if you are currently in the safe zone. This will not impact equipped items.");
        System.out.println(" ________________________");
        System.out.println("| CHARACTER INTERACTIONS |");
        System.out.println("|________________________|");
        System.out.println("   TALK TO [CHARACTER NAME]: Enters Dialogue with [CHARACTER NAME].");
        System.out.println("   JOIN [CHARACTER NAME]: You have the option to play this game with a set of NPC characters, your \"party.\"");
        System.out.println("          This command adds [CHARACTER NAME] to the party. RESTRICTIONS: Max party size is 3, including you the player. One party member per game class (Survivalist, Technologist, Negotiator).");
        System.out.println("          This can only be undone if [CHARACTER NAME] dies. There are very few ingame characters who you can join up with, and gameplay will make it clear who is a valid party member vs who is just another NPC.");
        System.out.println("   FIGHT [CHARACTER NAME]: Enters Combat with [CHARACTER NAME].");
        System.out.println("   HIDE: Attempts to hide or conceal the presence of the party, reducing detectability and the chance of unwanted interactions.");
        System.out.println("========================================");
    }

    public void printItems() {
        //TODO: Create list of items
        System.out.println("A very lazy dev left this section marked \"To Do\"");
    }

    public void combatManual() {
        System.out.println(" ==============================");
        System.out.println("||        COMBAT GUIDE        ||");
        System.out.println(" ==============================");
        System.out.println("");
        System.out.println("This is NOT a combat-focused game.");
        System.out.println("Combat is turn-based. You and your party, along with any enemies you fight, will be given an initiative order based on a random dice roll.");
        System.out.println("The combat system will iterate over the list of combatants in that order.");
        System.out.println("");
        System.out.println("On an adversary's turn, they will attempt to hit the player or party member with the lowest health.");
        System.out.println("If they succeed, damage will be dealt based on a flat value depending on the adversary.");
        System.out.println("");
        System.out.println("On a party member's turn, they will attempt to hit the adversary with the lowest health.");
        System.out.println("If they succeed, damage will be dealt based on a flat value depending on the party member's level.");
        System.out.println("");
        System.out.println("On your turn, you will be given the option to:");
        System.out.println("1. Attack");
        System.out.println("2. Use an Item");
        System.out.println("3. Use a Spell");
        System.out.println("4. Flee");
        System.out.println("");
        System.out.println("Attacking will roll to hit the adversary with the lowest health and then roll for damage.");
        System.out.println("Damage depends on your stats as well as any weapon you are wielding.");
        System.out.println("This game is not forgiving to players who choose not to wield a weapon.");
        System.out.println("");
        System.out.println("Using an item will bring up your inventory. If you have no items, this is a wasted turn.");
        System.out.println("If you do have items, you can use one to benefit your health or other stats.");
        System.out.println("");
        System.out.println("Selecting a spell will bring up a list of your spells. If you have no spells, this is a wasted turn.");
        System.out.println("All spells in this game are meant to deal damage.");
        System.out.println("Selecting a spell will use it to attack the adversary with the lowest health, just like a weapon attack.");
        System.out.println("There are no AoE or healing spells in this game (yet)");
        System.out.println("");
        System.out.println("Selecting 'Run' will attempt to flee from the enemy. Success is based on a dice roll, weighted by a comparison of your stats and the adversary's.");
        System.out.println("");
        System.out.println("It is currently not possible to equip party members with armor or weapons.");
        System.out.println("Their AC (Armor Class) will scale appropriately with level, but that is the extent of their defensive abilities.");
        System.out.println("");
        System.out.println("If you or a party member's health drops below 0, you must make death saving throws.");
        System.out.println("During this time, you are shielded from enemy attacks.");
        System.out.println("3 rolls below 10 result in death.");
        System.out.println("3 rolls above 10 result in stabilization to 0 health, but you will become vulnerable to attacks.");
        System.out.println("");
        System.out.println("If you or a party member's health falls to the equivalent of your max health below 0, you die.");
        System.out.println("For party members, this death is permanent: they are removed from the game forever, and you can never get them back.");
        System.out.println("For you, death is harsh as well: you will be sent back to the start of the level and the region will be reset.");
        System.out.println("If you have a save game, it would be wise to reload it in either case.");
        System.out.println("");
        System.out.println("Some NPCs are neutral to your presence, but if you fight them, they will become adversaries.");
        System.out.println("There is no way to make them friendly again.");
        System.out.println("If someone tries to kill you in real life, that would truly represent a permanent end to any potential friendship.");
        System.out.println("The same logic applies to characters in-game.");
        System.out.println("");
        System.out.println("=============================================");
        System.out.println("||    Combat will be challenging, so stay   ||");
        System.out.println("||      prepared and plan your actions!     ||");
        System.out.println("=============================================");
    }    

    public void enconomyManual() {
        System.out.println("┌──────────┐");
System.out.println("│ ECONOMY  │");
System.out.println("└──────────┘");
System.out.println();
System.out.println("In this game, there is no fiat currency. Instead, all transactions are based");
System.out.println("on bartering. Every item in the game has an inherent value, which is used to");
System.out.println("determine trade fairness.");
System.out.println();
System.out.println("To buy something, you must trade items of equal or greater value.");
System.out.println("Be strategic, as overpaying can leave you at a disadvantage!");
System.out.println();

System.out.println("┌───────────┐");
System.out.println("│ BARTERING │");
System.out.println("└───────────┘");
System.out.println();
System.out.println("1. To enter a trade, initiate a conversation with a merchant or");
System.out.println("   a friendly NPC, then select the 'Barter' option.");
System.out.println();
System.out.println("2. The NPC will show you their inventory along with the value of");
System.out.println("   each item they hold.");
System.out.println();
System.out.println("3. You can offer items from your inventory to trade. The total");
System.out.println("   value of the items you offer will create a 'Purchase Power'.");
System.out.println();
System.out.println("4. You can purchase anything from the NPC with a value less than");
System.out.println("   or equal to your Purchase Power. If you overpay, remember to");
System.out.println("   take back any leftover value you can as 'change'!");
System.out.println();
System.out.println("Example:");
System.out.println("   You want an item worth 10.");
System.out.println("   You offer 3 items worth 4 each (total value = 12).");
System.out.println("   You overpay by 2! The extra item is lost to the vendor,");
System.out.println("   as you don't have the purcase power to get it back.");
System.out.println();
System.out.println("Example 2:");
System.out.println("   You want an item worth 8.");
System.out.println("   You offer 3 items worth 4 each (total value = 12).");
System.out.println("   You overpay by 4, which is your remaining purchase power.");
System.out.println("  'Purchase' your item back from the vendor so you don't lose it!");
System.out.println();

System.out.println("┌──────────────────┐");
System.out.println("│ NEGOTIATOR BONUS │");
System.out.println("└──────────────────┘");
System.out.println();
System.out.println("If you or a member of your party belongs to the 'Negotiator' class:");
System.out.println("   - You receive a flat 10% discount on all items.");
System.out.println("   - Discounts are rounded down to the nearest whole number.");
System.out.println("   - Items with a discounted value less than 1 will have their");
System.out.println("     price set to 1.");
System.out.println();
System.out.println("Example:");
System.out.println("   An item worth 15 gets a 10% discount.");
System.out.println("   Discounted value: 15 - (15 * 0.10) = 13.5 -> rounds down to 13.");
System.out.println();
System.out.println("Barter smartly and use your Negotiator skills to maximize your resources!");

    }
}
