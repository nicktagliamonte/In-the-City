package com.nicktagliamonte.items;

import java.util.*;

public class SmallTrap extends Trap {
    public SmallTrap() {
        super("Small Trap", "A small trap. Good for finding simple supplies.", 1, true, 1);
        Map<Item, Integer> cost = new HashMap<Item, Integer>();
        //TODO: in codebase creation, once i have a scrap item created, update this to cost 1 scrap
        cost.put(new FuelCell(), 1);
        super.setCost(cost);
    }

    public void springTrap() {
        //TODO: generate the items the trap got through some random function
            //possible valid items will be scrap/water -> food -> medical
        //call super.springTrap(map of items) with those randomly generated items
        //destroy trap once done
    }

    public void expire() {
        //TODO: based on some time function, which may need to be a field in this file, remove the trap from a rooms item list
            //call super.expire
    }
}
