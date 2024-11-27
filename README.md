# In the City
A text-based adventure game set approximately in Philadelphia in a far-future world.  
Currently:  
- the game engine works  
  - the main loop works  
  - I can read and initialize a set of rooms for the player to explore from JSON.  
- There are objects in the room which are interactable (take/use/drop)  
- All the command words work  
- There is a menu with  
  - Manuals  
  - Maps  
  - Basic proof-of-concept style crafting with a limited item set  
  - Basic proof-of-concept style save/load (it will launch the system file explorer, but i don't yet have methods to read or write save files)  
- There is a character who can join the party, and combat works with adversarial and neutral NPCs.  
- A dialogue system exists with the 'friend' character as proof-of-concept
- A timing system exists to  
  - Determine status effects (health regen, health drain, hunger)  
  - Provide occasional "flavor text" output to console  
  - Generate random npc encounters (barter or combat interactions)  
- A status effect system exists  
  - Currently, hunger and thirst are the only status effects.  
  - Hunger reduces combat effectiveness, thirst causes slow health drain
- A player alignment system exists which
  - Increments alignment slightly in dialogue (being mean lowers the number, etc)
  - Increments alignment significantly in combat (fighting a bad guy increases the number, fighting a good guy decreases it)
  - Causes neutral characters to act as adversaries if player alignment is too low (or high) for their liking
  
Some character names or attributes are pulled from things I was watching at the time I made them, which will not be a feature of the final game:  
- The party member is named chunky [see Tim Robinson sketch: "What do you do???"]
- The neutral character is named Gilear [see D20's Fantasy High for the most neutral character I could think of]  
- Various other references are scattered into my test data but are not elements of the actual game story and will be removed/replaced by my story  

## Upcoming Work:  

### Phase 6: Working Demo  
Keep adding individual game elements in a "test data" format.  

- **GAME STATE MODIFICATIONS**  
  - Put a safe zone in the game (room 1).  
    - Make the random events that are combat-based not occur in the safe room.  (randomeventmanager.java)
    - Make the safe room have an inventory.  
      - Make trapped items appear in that inventory.  
      - Figure out how to access it.  
    - Build out the feature to locate the safe zone.  
      - This one is a job and a half.  
      - Could I just say (safe zone is at [coordinates of all adjacencies needed to get to safety?])  
  - Put an economic zone in the game (at this stage, probably make attic safe and room 1 economic).  
    - Make the random events that are combat-based not occur in the economic zone.  
      - Have barter events only occur in the economic zone and soem in safe zone (see todo in randomeventmanager).  
        - (Yes, really. When you're setting up the Metalwoods, is some guy gonna try to sell you a fish while you're fighting the fomorian?)  

- **MISC UPDATES**  
  - Trim the name entered by the user to be all lowercase but first letter upper case.
  - update the manuals to use the nicer formatting for section headers seen in the economy manual
  - Add sleep function(s), probably many of them, before printing to the console so that messages don't appear many at once but instead prit one-at-a-time.
    - format output for readability in general.
  - There should be a way to see remaining carry weight.  
  - Update the inventory function.  
    - Rather than listing a million fuel cells if you hold a million fuel cells, format as:  
      ```
      fuel cell   50  
      otherItem   1  
      etc  
      ```  
    - Also, make sure it shows the inventory for the whole party.  
  - Do I want a thievery mechanic?  
    - Probably for the negotiator, since simplifying the barter system completely nerfed them.  
  - Put a quest in the (room? game? NPC friend?).  
    - Decide what quests do to `gamestate`.  
      - Probably it will have to change the region (major quests at least) once complete.  
    - Decide whether quests come from Java or JSON.  
      - The JSON idea might be stupid.  
    - At this point, implement leveling for party members and NPCs.  
  - **Player Leveling Mechanics**  
    - Pay attention to the fact that both max and remaining carry weight will need to increase.  
    - Pay attention to the fact that party members should also level up here.  
    - Pay attention to the fact that this should come from EXP -- that will make side quests worthwhile without fully breaking the game.  
      - You can have EXP from (fights, stealth achievements, bartering achievements) be static per region and scale with the leveling equation.  
  - Add an "introduction" screen.  
    - This will be called from a method in the game engine (which, when I get to persistence, can be put in an if/else depending on what is passed into `main`).  
    - Just introduce the game before proceeding.  
  - Reassess demo at this time.  
    - In the planning stage, currently seems like this is a full demo.  
    - Check **ALL** TODO comments in every file.  
  - (Assuming that the demo is in fact done) host the demo.  
    - Find out what changes need to be made to host the demo online.  
      - Possibly just host the demo game online, and make the one with levels and save states run local. That way, you can have persistence without a backend.  
      - If that isn't reasonable, update the README to provide a detailed description of running the game.  
    - Tell friends to test it.  

### NEXT PHASES  

- **Phase 7: Codebase Creation**  
  - Create files for all items.  
    - If the deserializer gives you issues, put JSON methods in the switch statements rather than before it (see `CharacterDeserializer`).  
    - Update item manual
    - add item creation to the crafting menu (see Menu.java todos)
  - And all spells.  
  - And all NPCs.  
  - And everything and anything else.  
    - Pay close attention to the conveyance. It'll be basically an NPC, I guess. It will have an inventory with functionally no weight limit. 

- **Phase 8: Persistence**  
  - Create methods for saving and loading game states to test persistence.  

- **Phase 9: The Actual Game Itself**  
  - Start coding the levels one-by-one, modularly add them to the game.  

## Information:  

If you somehow stumbled onto this page and are interested in someday playing a text-based adventure game set in a far-future Philadelphia that draws primary influence from "A Dark Room," "Unsleeping City," and "Annihilation" and is overall kind of a bummer, **don't read anything in the resources folder** because it contains significant spoilers.  
I'll make the test game playable and include instructions for that much later.  Current ETA is probably around 12/25/24
