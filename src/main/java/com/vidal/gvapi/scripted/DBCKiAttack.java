package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.IDBCKiAttack;
import com.vidal.gvapi.utils.DBCTechData;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.INbt;
import kamkeel.npcdbc.api.IDBCAddon;

public class DBCKiAttack implements IDBCKiAttack {

    public int id;
    public String name;
    public int tpCost;

    private DBCKiAttack(String name) {
        this.name = name;
        this.id = DBCTechData.getKiAttackIndex(this.name);
        this.tpCost = DBCTechData.DBCAttackTPCosts[this.id];
    }

    public static DBCKiAttack create(String name) {
        if (DBCTechData.getKiAttackIndex(name) == -1)
            return null;

        return new DBCKiAttack(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setTPCost(int cost) {
        this.tpCost = cost;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTPCost() {
        return this.tpCost;
    }

    @Override
    public void giveToPlayer(IDBCAddon player) {
        int slot = getFirstFreeSlot(player);

        if (slot == 0) return;
        giveKiToPlayer(player, slot, false);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, int slot) {
        giveKiToPlayer(player, slot, false);
    }

    public void giveToPlayer(IDBCAddon player, int slot, boolean overrideSlot) {
        giveKiToPlayer(player, slot, overrideSlot);
    }

    @Override
    public void removeFromPlayer(IDBCAddon player) {
        removeKiFromPlayer(player, true);
    }

    public void removeFromPlayer(IDBCAddon player, boolean refund) {
        removeKiFromPlayer(player, refund);
    }

    private void giveKiToPlayer(IDBCAddon player, Integer slot, boolean overrideSlot) {
        if (player.getDBCPlayer().getTP() < this.tpCost)
            return;

        if (slot != null) {
            slot = Math.max(1, Math.min(4, slot)) + 4;
        } else {
            return;
        }

        String id = this.id + "";
        INbt nbt = player.getNbt().getCompound("PlayerPersisted");
        String slotName = "jrmcTech" + slot;

        if (nbt.getString(slotName) == null || nbt.getString(slotName).trim().isEmpty() || overrideSlot)
            nbt.setString(slotName, id);

        player.getDBCPlayer().setTP(player.getDBCPlayer().getTP() - this.tpCost);
    }

    private void removeKiFromPlayer(IDBCAddon player, boolean refund) {
        int slot = getAttackSlot(player);

        if (slot == 0)
            return;

        INbt nbt = player.getNbt().getCompound("PlayerPersisted");
        String slotName = "jrmcTech" + slot;

        nbt.setString(slotName, " ");
        if (refund)
            player.getDBCPlayer().setTP(player.getDBCPlayer().getTP() + this.tpCost);
    }

    private int getFirstFreeSlot(IDBCAddon player) {
        String tagName = "jrmcTech";
        INbt nbt = player.getNbt().getCompound("PlayerPersisted");

        for (int i = 5; i <= 8; i++) {
            String tagContents = nbt.getString(tagName + i);

            if (tagContents == null || tagContents.trim().isEmpty())
                return i;
        }

        return 0;
    }

    private int getAttackSlot(IDBCAddon player) {
        String tagName = "jrmcTech";
        INbt nbt = player.getNbt().getCompound("PlayerPersisted");

        for (var i = 5; i <= 8; i++) {
            String tagContents = nbt.getString(tagName + i);

            if (tagContents == null || tagContents.trim().isEmpty())
                continue;

            int foundId = Integer.parseInt(tagContents.trim());
            if (foundId == this.id)
                return i;
        }

        return 0;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setString("name", name);
        compound.setInteger("ID", id);
        compound.setInteger("tpCost", tpCost);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        name = compound.getString("name");
        id = compound.getInteger("ID");
        tpCost = compound.getInteger("tpCost");
    }
}
