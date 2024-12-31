![In The City Icon](https://github.com/nicktagliamonte/In-the-City/raw/main/in-the-city/src/main/java/resources/images/icon.ico)
# In the City

## Installation Instructions  
**Note:** This game is currently only compatible with Windows systems.  

1. **Download the Installer**  
   Go to the [Releases page](https://github.com/nicktagliamonte/In-the-City/releases/) and download the latest installer (`InTheCity-0.2.exe`).
   
2. **Run the Installer**  
   Extract if needed, then double-click the `.exe` file to launch the installer. The game will be installed in **Program Files/InTheCity**.

3. **Play the Game**  
   Go to **Program Files/InTheCity** and double-click the `.exe` to launch the game, or create a shortcut for easier access.

4. **Save the Game**
   Save files will automatically be placed in **Program Files/InTheCity/app/src/main/java/resources/saved games**. They will automatically be timestamped, so loading the most recent save is easy.
   If you move the file after it's made, complications with loading can occur. It's best to keep the save files at that location. They're only really useful when activated by the game anyway, and that's where it will look for them.
   They're also just plain old JSON files though, so if you want to manually write new bizarre items or other data into the game, that's where to do it. Let me know what happens.

---

## About In The City

**In The City** is a text-based adventure set in a far-future version of Philadelphia. Your goal is survival—overcoming hunger, thirst, and adversaries while exploring, interacting with NPCs, and completing quests.

### Key Features:

- **Game Engine:**
  - Main loop and room exploration from JSON.
  - Interactable objects (take/use/drop).
  - Command words work (e.g., take, use, drop).

- **Game Mechanics:**
  - **Character/Combat**: Neutral and adversarial NPCs, party member, combat system.
  - **Dialogue**: NPC conversations, with alignment affecting interactions.
  - **Status Effects**: Hunger and thirst affect combat and health.
  - **Player Alignment**: Changes with dialogue choices and combat; affects NPC reactions.
  - **Safe Zone**: Store items, avoid combat, and track location with `Locate` command.
  - **Economic Zone**: Increased barter events, decreased combat events.

- **Advanced Mechanics:**
  - **Stealth**: Hide to lower strength and increase wisdom.
  - **Theft**: Steal high-value items from NPCs, affecting alignment.
  - **Lockpicking**: Guess the number mini-game with skill-based hints.
  - **Leveling**: Increase player attributes, scale XP, and level up.
  
- **Puzzles & Quests:**
  - Puzzle types: Combination locks, dials, mastermind-style puzzles, and escape rooms.
  - Reward system: Hints and player rewards for puzzle completion.
  - **Quest System**: Multiple objectives across different regions, with a variety of puzzles and encounters.
  
- **Current Progress:**
  - Three quests across two regions, in a basic, loosely connected (read: barely a) story.
  - Ongoing testing with a full story planned for the future.

### Testing Status
Testing is ongoing, with the goal of expanding the story based on feedback.

---

## System Requirements
- **OS**: Windows 10 or later (for compatibility)
- **RAM**: Minimum of 4 GB

---

## Troubleshooting
If you encounter issues, visit the [Issues section](https://github.com/nicktagliamonte/In-the-City/issues) or open a new issue for support.
You can also just contact me directly, because the only people seeing this page know me in real life.
