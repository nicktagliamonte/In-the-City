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
- A safe zone which
  - Has an inventory of items the player can store, working around limited carry weight and giving them a place to stockpile resources
  - Blocks randomly generated combat events from occurring, and significantly decreases the frequency of randomly generated barter/begging events.
  - Can be located from anywhere in the game through the `Locate` command, which will print a string of adjacencies required to get from the player's current position to the safe zone  
    - "To get to the safe zone from your current position, go through: "room 2" -> "room 1" -> "room 0"" where room 0 is the safe zone
- An economic zone which increases the incidence of random barter events and decreases, but does not eliminate, the incidence of random combat events.
- A hide feature which attempts to conceal the players presence, and modifies attribute scores based on this concealed presence (lowered strength, increased wisdom, etc)
- A theivery mechanic which 
  - Attempts to take the single highest value item from an NPC and puts it either in player inventory or the safe zone inventory, depending on player carrying capacity
  - Failure is based on opposed skill checks plus a dice roll, where the skills are wis and char
  - Failure against a neutral or adversarial npc initiates combat with that npc
  - Attempts at theivery modify the player alignment.  Stealing from a bad guy increases alignment, stealing from a good guy decreases it.
- A lockpicking mechanic which enters a 'guess the number' minigame to attempt to pick a lock.  The number of guesses is based on a skill check from the players intelligence score, and the upper bound for a number to guess is based on a field in the adjacency object.
- Player leveling mechanics which increment the player and party attributes, calculate the xp needed to get to the next level, and scales the xp received based on player level.
  
Some character names or attributes are pulled from things I was watching at the time I made them, which will not be a feature of the final game:  
- The party member is named chunky [see Tim Robinson sketch: "What do you do???"]
- The neutral character is named Gilear [see D20's Fantasy High for the most neutral character I could think of]  
- Various other references are scattered into my test data but are not elements of the actual game story and will be removed/replaced by my story  

## Upcoming Work:  

### Phase 6: Working Demo  
Keep adding individual game elements in a "test data" format.      
- Put a quest in the (room? game? NPC friend?).  
  - Decide what quests do to `gamestate`.  
    - Probably it will have to change the region (major quests at least) once complete.  
  - Decide whether quests come from Java or JSON. 
  - Include a puzzle 
- Add sleep function(s), probably many of them, before printing to the console so that messages don't appear many at once but instead prit one-at-a-time.
  - format output for readability in general.  
- Update the inventory function.  
  - Rather than listing a million fuel cells if you hold a million fuel cells, format as:  
    ```
    fuel cell   1,000,000  
    otherItem   1  
    etc  
    ```  
  - Also, make sure it shows the inventory for the whole party.  
- There should be a way to see remaining carry weight.  
- update the manuals to use the nicer formatting for section headers seen in the economy manual
- Trim the name entered by the user to be all lowercase but first letter upper case.
- Build out some SLIGHT story elements.  fill out the dialogue options with npc's, spice up the descriptions, give the characters real names
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

**Phase 7: Codebase Creation**  
- Create files for all items.  
  - If the deserializer gives you issues, put JSON methods in the switch statements rather than before it (see `CharacterDeserializer`).  
  - Update item manual
  - add item creation to the crafting menu (see Menu.java todos)
- And all spells.  
- And all NPCs.  
- And everything and anything else.  
  - Pay close attention to the conveyance. It'll be basically an NPC, I guess. It will have an inventory with functionally no weight limit. 

**Phase 8: Persistence**  
- Create methods for saving and loading game states to test persistence.  

**Phase 9: The Actual Game Itself**  
- Start coding the levels one-by-one, modularly add them to the game.  

## Information:  

If you somehow stumbled onto this page and are interested in someday playing a text-based adventure game set in a far-future Philadelphia that draws primary influence from "A Dark Room," "Unsleeping City," and "Annihilation" and is overall kind of a bummer, **don't read anything in the resources folder** because it contains significant spoilers.  
I'll make the test game playable and include instructions for that much later.  Current ETA is probably around 12/25/24