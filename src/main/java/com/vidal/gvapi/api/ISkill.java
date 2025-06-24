package com.vidal.gvapi.api;


import kamkeel.npcdbc.api.IDBCAddon;

public interface ISkill extends IDataFragment {

    void setName(String name);

    void setTPCost(int cost);

    void setMindCost(int cost);

    void giveToPlayer(IDBCAddon player);

    void giveToPlayer(IDBCAddon player, int level);

    void giveToPlayer(IDBCAddon player, boolean ignoreTP);

    void giveToPlayer(IDBCAddon player, int level, boolean ignoreTP);

    void removeFromPlayer(IDBCAddon player);

    int getID();

    String getName();

    int getTPCost();

    int getMindCost();
}
