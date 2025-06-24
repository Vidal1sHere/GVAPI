package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.ICustomKiAttack;
import com.vidal.gvapi.api.ICustomKiData;
import kamkeel.npcdbc.api.IDBCAddon;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.LogWriter;
import noppes.npcs.api.INbt;

import java.util.Map;

public class CustomKiAttack implements ICustomKiAttack {

    public int id;
    public String name;
    public int tpCost;
    private ICustomKiData data;

    private CustomKiAttack(String name, int id, int tpCost, Map<String, Object> settings) {
        this.name = name;
        this.id = id;
        this.tpCost = tpCost;
        this.data = CustomKiData.create(name, id, settings);
    }

    public CustomKiAttack() {
        this.name = "Unnamed Attack";
        this.id = -1;
        this.tpCost = 0;
        this.data = CustomKiData.create(this.name, this.id, null);
    }

    public static CustomKiAttack create(String name, int id, int tpCost, Map<String, Object> settings) {
        return new CustomKiAttack(name, id, tpCost, settings);
    }

    @Override
    public void setAttackData(ICustomKiData data) {
        this.data = data;
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
    public ICustomKiData getAttackData() {
        return this.data;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
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
            slot = Math.max(1, Math.min(4, slot));
        } else {
            return;
        }

        String id = this.data.toString();
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

        for (int i = 1; i <= 4; i++) {
            String tagContents = nbt.getString(tagName + i);

            if (tagContents == null || tagContents.trim().isEmpty())
                return i;
        }

        return 0;
    }

    private int getAttackSlot(IDBCAddon player) {
        INbt nbt = player.getNbt().getCompound("PlayerPersisted");
        String[] attackDataSplit = this.data.toString().split(";");

        String attackName = attackDataSplit[0];
        String attackCreator = attackDataSplit[2];

        for (int i = 1; i <= 4; i++) {
            String playerAttackSlot = nbt.getString("jrmcTech" + i);

            if (playerAttackSlot == null || playerAttackSlot.trim().isEmpty())
                continue;

            String[] playerAttackData = playerAttackSlot.split(";");
            String playerAttackName = playerAttackData[0];
            String playerAttackCreator = playerAttackData[2];

            if (attackName.equals(playerAttackName) && attackCreator.equals(playerAttackCreator))
                return i;
        }

        return 0;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        CustomKiData data = (CustomKiData) this.getAttackData();

        compound.setInteger("ID", id);
        compound.setString("name", name);
        compound.setInteger("tpCost", tpCost);

        NBTTagCompound dataCompound = data.writeToNBT();
        compound.setTag("data", dataCompound);

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagCompound dataCompound = compound.getCompoundTag("data");

        if (compound.hasKey("ID"))
            this.id = compound.getInteger("ID");
        else if (CustomKiHandler.getInstance() != null)
            this.id = CustomKiHandler.getInstance().dataWriter.getUnusedId();

        this.name = compound.getString("name");
        this.tpCost = compound.getInteger("tpCost");
        this.data = CustomKiData.create(this.name, this.id, null);

        this.data.readFromNBT(dataCompound);
    }

    public CustomKiAttack save() {
        return CustomKiHandler.getInstance().saveAttack(this);
    }
}
