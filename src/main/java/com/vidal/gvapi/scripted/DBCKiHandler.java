package com.vidal.gvapi.scripted;

import JinRyuu.DragonBC.common.DBCKiTech;
import com.vidal.gvapi.api.IDBCKiAttack;
import com.vidal.gvapi.api.IKiHandler;
import kamkeel.npcdbc.api.IDBCAddon;
import noppes.npcs.api.INbt;

import com.vidal.gvapi.utils.DBCKiData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class DBCKiHandler implements IKiHandler {
    public static DBCKiHandler Instance = new DBCKiHandler();
    HashMap<Integer, DBCKiAttack> attacks;

    private DBCKiHandler() {
        Instance = this;
        attacks = new HashMap<>();
        loadAllAttacks();
    }

    public static DBCKiHandler getInstance() {
        return Instance;
    }

    private void loadAllAttacks() {
        Stream<String> DBCNames = Arrays.stream(DBCKiData.DBCAttackNames);

        DBCNames.forEach(name -> {
            int id = DBCKiData.getKiAttackIndex(name);

            DBCKiAttack attack = DBCKiAttack.create(name);
            attacks.put(id, attack);
        });
    }

    @Override
    public DBCKiAttack getKiAttack(int id) {
        return attacks.get(id);
    }

    @Override
    public DBCKiAttack getKiAttack(String name) {
        DBCKiAttack[] values = attacks.values().toArray(new DBCKiAttack[0]);

        for (DBCKiAttack value : values) {
            if (value.getName().equalsIgnoreCase(name))
                return value;
        }

        return null;
    }

    @Override
    public boolean hasKiAttack(int id) {
        return attacks.containsKey(id);
    }

    @Override
    public boolean hasKiAttack(String name) {
        return getKiAttack(name) != null;
    }

    @Override
    public boolean hasKiAttack(IDBCAddon player, int id) {
        INbt nbt = player.getNbt().getCompound("PlayerPersisted");
        String tagName = "jrmcTech";

        for (int i = 5; i <= 8; i++) {
            String tagContents = nbt.getString(tagName + i);

            if (tagContents == null || tagContents.isEmpty())
                continue;

            return Integer.parseInt(tagContents.trim()) == id;
        }

        return false;
    }

    @Override
    public boolean hasKiAttack(IDBCAddon player, String name) {
        DBCKiAttack attack = getKiAttack(name);
        if (attack == null)
            return false;

        return hasKiAttack(player, attack.id);
    }

    @Override
    public IDBCKiAttack[] getAllKiAttacks() {
        return attacks.values().toArray(new DBCKiAttack[0]);
    }

    // GIVE ATTACK USING IDBCKIATTACK
    @Override
    public void giveKiAttack(IDBCAddon player, IDBCKiAttack attack) {
        attack.giveToPlayer(player);
    }

    public void giveKiAttack(IDBCAddon player, IDBCKiAttack attack, Integer slot) {
        attack.giveToPlayer(player, slot.intValue());
    }

    public void giveKiAttack(IDBCAddon player, IDBCKiAttack attack, Integer slot, boolean overrideSlot) {
        player.sendMessage("????");
        attack.giveToPlayer(player, slot.intValue(), overrideSlot);
    }

    // GIVE ATTACK USING ATTACK NAME
    @Override
    public void giveKiAttack(IDBCAddon player, String name) {
        giveKiAttack(player, name, null, true);
    }

    public void giveKiAttack(IDBCAddon player, String name, Integer slot) {
        giveKiAttack(player, name, slot.intValue(), true);
    }

    public void giveKiAttack(IDBCAddon player, String name, Integer slot, boolean overrideSlot) {
        DBCKiAttack attack = getKiAttack(name);
        if (attack != null)
            attack.giveToPlayer(player, slot.intValue(), overrideSlot);
    }

    // GIVE ATTACK USING ATTACK ID
    @Override
    public void giveKiAttack(IDBCAddon player, int id) {
        giveKiAttack(player, id, null, true);
    }

    public void giveKiAttack(IDBCAddon player, int id, Integer slot) {
        giveKiAttack(player, id, slot.intValue(), true);
    }

    public void giveKiAttack(IDBCAddon player, int id, Integer slot, boolean overrideSlot) {
        DBCKiAttack attack = getKiAttack(id);
        if (attack != null)
            attack.giveToPlayer(player, slot.intValue(), overrideSlot);
    }

    // REMOVE ATTACK USING IDBCKIATTACK
    public void removeKiAttack(IDBCAddon player, DBCKiAttack attack) {
        attack.removeFromPlayer(player);
    }

    public void removeKiAttack(IDBCAddon player, DBCKiAttack attack, boolean refund) {
        attack.removeFromPlayer(player, refund);
    }

    // REMOVE ATTACK USING ATTACK NAME
    public void removeKiAttack(IDBCAddon player, String name) {
        removeKiAttack(player, name, true);
    }

    public void removeKiAttack(IDBCAddon player, String name, boolean refund) {
        DBCKiAttack attack = getKiAttack(name);
        if (attack != null)
            attack.removeFromPlayer(player, refund);
    }

    // REMOVE ATTACK USING ATTACK ID
    public void removeKiAttack(IDBCAddon player, int id) {
        removeKiAttack(player, id, true);
    }

    public void removeKiAttack(IDBCAddon player, int id, boolean refund) {
        DBCKiAttack attack = getKiAttack(id);
        if (attack != null)
            attack.removeFromPlayer(player, refund);
    }

    @Override
    public void registerKiAttack(IDBCKiAttack attack) {

    }

    @Override
    public void deleteKiAttack(int id) {

    }

    @Override
    public void deleteKiAttack(String id) {

    }
}
