package com.vidal.gvapi.api;

import kamkeel.npcdbc.api.IDBCAddon;

public interface IKiHandler {
    void registerKiAttack(IDBCKiAttack attack);

    void deleteKiAttack(int id);

    void deleteKiAttack(String id);

    IDBCKiAttack getKiAttack(int id);

    IDBCKiAttack getKiAttack(String name);

    boolean hasKiAttack(int id);

    boolean hasKiAttack(String name);

    boolean hasKiAttack(IDBCAddon player, int id);

    boolean hasKiAttack(IDBCAddon player, String name);

    IDBCKiAttack[] getAllKiAttacks();

    void giveKiAttack(IDBCAddon player, IDBCKiAttack attack);

    void giveKiAttack(IDBCAddon player, String attack);

    void giveKiAttack(IDBCAddon player, int attack);
}
