package com.vidal.gvapi.api;

import kamkeel.npcdbc.api.IDBCAddon;

public interface IDBCKiAttack extends IDataFragment {

    void setName(String name);

    void setTPCost(int cost);

    String getName();

    int getTPCost();

    void giveToPlayer(IDBCAddon player);

    void giveToPlayer(IDBCAddon player, int slot);

    void giveToPlayer(IDBCAddon player, int slot, boolean refund);

    void removeFromPlayer(IDBCAddon player);
}
