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
  - Save/load options.  
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
- A quest system which inlcudes a variety of potential objectives.
- A series of puzzles of different styles, which take the form of "mini-games" including:
  - A combination lock sequence.
  - A "turn the dials in the correct order" puzzle.
  - A mastermind style "arrange the items correctly" puzzle.
- Each of the puzzles have a hint mechanic and player rewards for completion.
- An escape room style section where the door locks behind the player on entry, and the player has to solve a puzzle to unlock it.
- Methods exist to read and write from save files.
- Right now, these elements all exist in a very minor, loosely held together story which takes place through 3 quests seen in 2 regions. It is playable in that state, and could hypothetically be cloned from here and run locally if you wanted to.
### Testing is an ongoing effort and, pending testing results, a full story will come next.

## Current Work:  

**Phase 8: Hosting**  
- Host the demo.  
  - Find out what changes need to be made to host the demo online.  
    - Possibly just host the demo game online, and make the one with levels and save states run local. That way, you can have persistence without a backend.  
    - If that isn't reasonable, update the README to provide a detailed description of running the game.  
  - Tell friends to test it.
  
### NEXT PHASES  
**Phase 9: The Actual Game Itself**  
- Start coding the levels one-by-one, modularly add them to the game.  

## Information:  

If you somehow stumbled onto this page and are interested in someday playing a text-based adventure game set in a far-future Philadelphia that draws primary influence from "A Dark Room," "Unsleeping City," and "Annihilation" and is overall kind of a bummer, **don't read anything in the resources folder** because it contains significant spoilers.  
I'll make the test game playable and include instructions for that later.  Current ETA is probably around 1/15/25
