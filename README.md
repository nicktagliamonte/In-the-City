# In the City
A text-based adventure game set approximately in Philadelphia in a far-future world.  
Currently, the game engine works, the main loop works, and I can read and initialize a set of rooms for the player to explore from JSON.  
There are objects in the room which are interactable, all the command words work, there is a menu, there is a character who can join the party, and combat works with adverdarial and neutral NPCs.  
Some character names are pulled from things I was watching at the time I made them, and that will change:  
- The party member is named chunky [see Tim Robinson sketch.  "What do you do???"]
- The neutral character is named Gilear [see D20 Fantasy High for the most neutral character I could think of]

## Upcoming Work:  

### Phase 6: Working Demo  
Keep adding individual game elements in a "test data" format.  

- **GAME STATE MODIFICATIONS**  
  - Neutral needs all stats, so does adversary (see `PartyMember.java`).  
  - Make adversary inventory drop to floor at adversary location (update remove dead adversary method and the other instance where `map.entry` appears in `Combat.java`).  
  - Reset Gilear health and etc. back to normal (currently elevated to test death saving throw functionality).  
  - Edit the combat manual to hit the "not combat-focused" thing a little less hard.  
  - Add an economy manual.  
  - Reassess shrewdness in friend and neutral. Maybe do away with it.  
  - Organize all my files (method calls and such, the ordering of methods).  
  - How does the technologist learn new spells?  
  - Put a hint in the (room? game state? player inventory?).  
    - Make sure the hint is usable. Regeneration is the next step.  
    - Hint command needs to be updated, as does `player.java` `useHint`.  
  - Put timed events in the game.  
    - Start with hint regeneration.  
    - How about health regeneration as well?  
      - Party member health also needs to regen.  
    - Have some kind of clock tick that calls to random events periodically.  
      - Or have those events result from player movement. Decide here.  
        - Can technically change it later.  
      - If the random events are timed, make sure that you pause that counter in a menu state.  
    - Go over all todos to make sure you hit all timed events.  
      - Traps will need to be basically fully redone here.  
  - Add status effects.  
    - This is a natural follow-up to having health regen -- some status effects will cause health drain.  
    - Having just set up some sort of timing system, it makes sense to say "x amount of time or x moves without food causes hunger status."  
  - Put a morality field in the player object.  
    - This really doesn't have to do anything yet and is as easy to update as health.  
      - (When I'm typing this, I haven't begun thinking about coding combat. Maybe health was a nightmare to update, who knows.)  
  - Put a safe zone in the game (room 1).  
    - Make the random events that are combat-based not occur in the safe room.  
    - Make the safe room have an inventory.  
      - Make trapped items appear in that inventory.  
      - Figure out how to access it.  
    - Build out the feature to locate the safe zone.  
      - This one is a job and a half.  
      - Could I just say (safe zone is at [coordinates of all adjacencies needed to get to safety?])  
  - Put an economic zone in the game (at this stage, probably make attic safe and room 1 economic).  
    - Make the random events that are combat-based not occur in the economic zone.  
    - Make random barter events.  
      - Have them only occur in the economic zone.  
        - (Yes, really. When you're setting up the Metalwoods, is some guy gonna try to sell you a fish while you're fighting the fomorian?)  

- **MISC UPDATES**  
  - Trim the name entered by the user to be all lowercase but first letter upper case.  
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
  - And all spells.  
  - And all NPCs.  
  - And everything and anything else.  
    - Pay close attention to the conveyance. It'll be basically an NPC, I guess. It will have an inventory with functionally no weight limit.  

- **Phase 8: Persistence**  
  - Create methods for saving and loading game states to test persistence.  

- **Phase 9: The Actual Game Itself**  
  - Start coding the levels one-by-one, modularly add them to the game.  

## Information:  

If you somehow stumbled onto this page and are interested in someday playing a text-based adventure game set in a far-future Philadelphia that draws primary influence from "A Dark Room," "Unsleeping City," and "Annihilation" and is overall kind of a bummer,  
**don't read anything in the resources folder** because it contains significant spoilers.  
I'll make the test game playable and include instructions for that much later.  Current ETA is probably around 12/25/24
