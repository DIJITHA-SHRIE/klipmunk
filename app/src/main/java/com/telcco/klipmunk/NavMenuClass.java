package com.telcco.klipmunk;

import android.view.Menu;

import java.util.ArrayList;

/**
 * Created by PHD on 12/27/2018.
 */

public class NavMenuClass {
    Menu menu;
    ArrayList<String> items;

    public NavMenuClass(Menu menu,ArrayList items){

        this.items = items;
        this.menu = menu;

    }

    public Menu getMenu(){
        return menu;
    }

    public ArrayList getItems(){
        return items;
    }
}
