package com.nicktagliamonte.game;

public class Manual {
    public void printCommands() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ===================");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("||     COMMAND     ||");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("||      GUIDE      ||");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ===================");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("No command is case sensitive on any command word or argument. Caps are only used below to indicate which words are commands and arguments.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("LOOK, look, and LoOk are all valid.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\"Take light leather armor\" and \"take LiGhT LeAtHeR aRmOr\" are both valid.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("This game is long and is meant to be somewhat adversarial towards you, the player.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("But there is a walkthrough available on GitHub if you're frustrated, or more interested in the story than the gameplay.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("========================================");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ___________________");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("|    ORIENTATION    |");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("|___________________|");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -LOOK: Prints a description of your surroundings, as well as the items, characters, and pathways accessible to you.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("          Room names will be displayed in quotes.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -LOCATE: Describes your current location and gives directions to the nearest safe zone and economic zone.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -MENU: You used this command to get to this menu.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ___________________");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("| MOVEMENT COMMANDS |");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("|___________________|");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -MOVE [DIRECTION]: Moves 1 step in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -MOVE [DIRECTION] [NUMBER OF STEPS]: Moves [NUMBER OF STEPS] steps in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.\n\tValid NUMBERS OF STEPS are integers greater than or equal to 1.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -MOVE TO [WAYPOINT]: Moves to the WAYPOINT specified by [WAYPOINT].\n\tValid WAYPOINTS are the names of characters, items, or pathways you currently have access to.\n\tUse LOOK for a list of such waypoints.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -ENTER [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location.\n\tCurrently available ROOM NAMES can be found using the LOOK command.\n\tThis command will word for rooms separated by a staircase or ladder or otherwise above or below your current position, as well as rooms seperated by a doorway or otherwise on your same level.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -ASCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going up.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   -DESCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going down.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" _____________________");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("| OBJECT INTERACTIONS |");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("|_____________________|");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   EXAMINE [ITEM NAME]: Gives a more detailed description of the item specified by [ITEM NAME]\n\tUse LOOK and/or INVENTORY to get a list of available items.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   TAKE [ITEM NAME]: Adds [ITEM NAME] to the player/party inventory, if there is room.\n\tIf you are in the safe zone, the game will first try to take the item from the safe zone before moving onto general items in the room.\n\tOtherwise, the game will just take an item of the name you specify off the floor, as long as it is available.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   USE [ITEM NAME]: Uses [ITEM NAME], if it has a use.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   DROP [ITEM NAME]: Removes [ITEM NAME] from inventory, if there is an item of such name in inventory");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   EQUIP [ITEM NAME]: Equips [ITEM NAME] if it is a weapon, armor, or enhancement item, if there is an item of such name in inventory");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   DEQUIP [ITEM NAME]: Removes [ITEM NAME] from armor slot and adds it back to inventory, if there is an item of such name currently equipped.\n\tI know dequip is technically not an English word but the game is already written with that being the word for \"stop having this item equipped\", so");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   TRANSFER [ITEM NAME]: Transfers [ITEM NAME] from your inventory to the safe zone inventory, if you are currently in the safe zone.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   TRANSFER ALL: Transfers every item in your inventory from your inventory to the safe zone inventory, if you are currently in the safe zone. This will not impact equipped items.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   UNLOCK: Please note that this command does NOT take any additional arguments. If you have a key to a room you are currently adjacent to, this command will unlock the door.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   LOCKPICK [ROOM NAME]: Unlike the UNLOCK command, this command does require you to specify a ROOM NAME.\n\tInitiates lockpicking sequence to enter ROOM NAME.\n\tIf you get the door unlocked, you still have to use ENTER to get into the room.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ________________________");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("| CHARACTER INTERACTIONS |");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("|________________________|");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   TALK TO [CHARACTER NAME]: Enters Dialogue with [CHARACTER NAME].");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   JOIN [CHARACTER NAME]: You have the option to play this game with a set of NPC characters, your \"party.\"");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("          This command adds [CHARACTER NAME] to the party. RESTRICTIONS: Max party size is 3, including you the player. One party member per game class (Survivalist, Technologist, Negotiator).");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("          This can only be undone if [CHARACTER NAME] dies. There are very few ingame characters who you can join up with, and gameplay will make it clear who is a valid party member vs who is just another NPC.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   FIGHT [CHARACTER NAME]: Enters Combat with [CHARACTER NAME].");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   HIDE: Attempts to hide or conceal the presence of the party, reducing detectability and boosting theivery success. When hidden, you cannot move more than one space at a time.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   UNHIDE: Undoes the above, returns to normal.\n\tIt's not a word, but it's the word I'm using.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("========================================");
    }

    public void printItems() {
        //TODO: Create list of items
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("A very lazy dev left this section marked \"To Do\"");
    }

    public void combatManual() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ==============================");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("||        COMBAT GUIDE        ||");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" ==============================");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("This is NOT a combat-focused game.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Combat is turn-based. You and your party, along with any enemies you fight, will be given an initiative order based on a random dice roll.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("The combat system will iterate over the list of combatants in that order.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("On an adversary's turn, they will attempt to hit the player or party member with the lowest health.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If they succeed, damage will be dealt based on a flat value depending on the adversary.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("On a party member's turn, they will attempt to hit the adversary with the lowest health.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If they succeed, damage will be dealt based on a flat value depending on the party member's level.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("On your turn, you will be given the option to:");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("1. Attack");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("2. Use an Item");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("3. Use a Spell");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("4. Flee");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Attacking will roll to hit the adversary with the lowest health and then roll for damage.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Damage depends on your stats as well as any weapon you are wielding.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("This game is not forgiving to players who choose not to wield a weapon.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Using an item will bring up your inventory. If you have no items, this is a wasted turn.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If you do have items, you can use one to benefit your health or other stats.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Selecting a spell will bring up a list of your spells. If you have no spells, this is a wasted turn.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("All spells in this game are meant to deal damage.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Selecting a spell will use it to attack the adversary with the lowest health, just like a weapon attack.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("There are no AoE or healing spells in this game (yet)");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Selecting 'Run' will attempt to flee from the enemy. Success is based on a dice roll, weighted by a comparison of your stats and the adversary's.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("It is currently not possible to equip party members with armor or weapons.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Their AC (Armor Class) will scale appropriately with level, but that is the extent of their defensive abilities.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If you or a party member's health drops below 0, you must make death saving throws.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("During this time, you are shielded from enemy attacks.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("3 rolls below 10 result in death.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("3 rolls above 10 result in stabilization to 0 health, but you will become vulnerable to attacks.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If you or a party member's health falls to the equivalent of your max health below 0, you die.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("For party members, this death is permanent: they are removed from the game forever, and you can never get them back.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("For you, death is harsh as well: you will be sent back to the start of the level and the region will be reset.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If you have a save game, it would be wise to reload it in either case.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Some NPCs are neutral to your presence, but if you fight them, they will become adversaries.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("There is no way to make them friendly again.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("If someone tries to kill you in real life, that would truly represent a permanent end to any potential friendship.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("The same logic applies to characters in-game.");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("=============================================");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("||    Combat will be challenging, so stay   ||");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("||      prepared and plan your actions!     ||");
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("=============================================");
    }    

    public void enconomyManual() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("┌──────────┐");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("│ ECONOMY  │");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("└──────────┘");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("In this game, there is no fiat currency. Instead, all transactions are based");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("on bartering. Every item in the game has an inherent value, which is used to");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("determine trade fairness.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("To buy something, you must trade items of equal or greater value.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("Be strategic, as overpaying can leave you at a disadvantage!");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();

try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("┌───────────┐");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("│ BARTERING │");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("└───────────┘");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("1. To enter a trade, initiate a conversation with a merchant or");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   a friendly NPC, then select the 'Barter' option.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("2. The NPC will show you their inventory along with the value of");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   each item they hold.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("3. You can offer items from your inventory to trade. The total");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   value of the items you offer will create a 'Purchase Power'.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("4. You can purchase anything from the NPC with a value less than");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   or equal to your Purchase Power. If you overpay, remember to");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   take back any leftover value you can as 'change'!");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("Example:");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   You want an item worth 10.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   You offer 3 items worth 4 each (total value = 12).");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   You overpay by 2! The extra item is lost to the vendor,");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   as you don't have the purcase power to get it back.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("Example 2:");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   You want an item worth 8.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   You offer 3 items worth 4 each (total value = 12).");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   You overpay by 4, which is your remaining purchase power.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("  'Purchase' your item back from the vendor so you don't lose it!");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();

try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("┌──────────────────┐");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("│ NEGOTIATOR BONUS │");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("└──────────────────┘");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("If you or a member of your party belongs to the 'Negotiator' class:");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   - You receive a flat 10% discount on all items.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   - Discounts are rounded down to the nearest whole number.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   - Items with a discounted value less than 1 will have their");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("     price set to 1.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("Example:");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   An item worth 15 gets a 10% discount.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("   Discounted value: 15 - (15 * 0.10) = 13.5 -> rounds down to 13.");
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println();
try {
    Thread.sleep(15);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("Barter smartly and use your Negotiator skills to maximize your resources!");

    }
}
