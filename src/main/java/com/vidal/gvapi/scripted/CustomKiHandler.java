package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.IDBCKiAttack;
import com.vidal.gvapi.api.IKiHandler;
import com.vidal.gvapi.utils.DataWriter;
import kamkeel.npcdbc.api.IDBCAddon;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.LogWriter;
import noppes.npcs.scripted.CustomNPCsException;
import noppes.npcs.util.NBTJsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CustomKiHandler implements IKiHandler {
    public static CustomKiHandler Instance = new CustomKiHandler();
    public DataWriter<CustomKiAttack> dataWriter = new DataWriter<>("customkiattacks", CustomKiAttack::getName);
    HashMap<Integer, CustomKiAttack> attacks;

    private CustomKiHandler() {
        Instance = this;
        attacks = dataWriter.getDataMap();
    }

    public static CustomKiHandler getInstance() {
        return Instance;
    }

    public IDBCKiAttack createKiAttack(String name, int id, int tpCost, Map<String, Object> settings) {
        return CustomKiAttack.create(name, id, tpCost, settings);
    }

    public IDBCKiAttack createKiAttack(String name, int id, int tpCost) {
        return CustomKiAttack.create(name, id, tpCost, new HashMap<String, Object>());
    }

    public void load() {
        dataWriter.clear();
        attacks = dataWriter.getDataMap();

        LogWriter.info("Loading custom ki attacks...");
        dataWriter.readDataMap();
        loadData();
        LogWriter.info("Done loading custom ki attacks...");
    }

    private void loadData() {
        attacks.clear();

        File dir = dataWriter.getDir();
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            for (File file : dir.listFiles()) {
                if (!file.isFile() || !file.getName().endsWith(".json"))
                    continue;
                try {
                    CustomKiAttack attack = new CustomKiAttack();
                    NBTTagCompound nbt = NBTJsonUtil.LoadFile(file);
                    LogWriter.info(nbt);
                    attack.readFromNBT(nbt);
                    attack.name = file.getName().substring(0, file.getName().length() - 5);

                    if (attack.id == -1) {
                        attack.id = dataWriter.getUnusedId();
                    }

                    int originalID = attack.id;
                    int setID = attack.id;
                    HashMap<Integer, String> bootOrder = dataWriter.getBootOrder();
                    while (bootOrder.containsKey(setID) || attacks.containsKey(setID)) {
                        if (bootOrder.containsKey(setID))
                            if (bootOrder.get(setID).equals(attack.name))
                                break;

                        setID++;
                    }

                    attack.id = setID;
                    if (originalID != setID) {
                        LogWriter.info("Found Custom Ki attack ID Mismatch: " + attack.name + ", New ID: " + setID);
                        attack.save();
                    }

                    attacks.put(attack.id, attack);
                } catch (Exception e) {
                    LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
                }
            }
        }
        dataWriter.saveDataLoadMap();
    }

    @Override
    public IDBCKiAttack getKiAttack(int id) {
        if (id == -1)
            return null;
        return this.attacks.get(id);
    }

    @Override
    public IDBCKiAttack getKiAttack(String name) {
        CustomKiAttack[] values = attacks.values().toArray(new CustomKiAttack[0]);

        for (CustomKiAttack value : values) {
            if (value.getName().equalsIgnoreCase(name))
                return value;
        }

        return null;
    }

    @Override
    public boolean hasKiAttack(int id) {
        return getKiAttack(id) != null;
    }

    @Override
    public boolean hasKiAttack(String name) {
        return getKiAttack(name) != null;
    }

    @Override
    public boolean hasKiAttack(IDBCAddon player, int id) {
        return false;
    }

    @Override
    public boolean hasKiAttack(IDBCAddon player, String name) {
        return false;
    }

    @Override
    public IDBCKiAttack[] getAllKiAttacks() {
        return attacks.values().toArray(new CustomKiAttack[0]);
    }

    public CustomKiAttack saveAttack(CustomKiAttack attack) {
        CustomKiAttack existing = attacks.get(attack.getID());

        if (attack.getID() < 0 || (existing != null && !existing.getName().equals(attack.getName()))) {
            attack.setID(dataWriter.getUnusedId());
        }

        while (true) {
            IDBCKiAttack nameCheck = getKiAttack(attack.getName());
            if (nameCheck == null || (nameCheck instanceof CustomKiAttack && ((CustomKiAttack) nameCheck).getID() == attack.getID()))
                break;
            attack.setName(attack.getName() + "_");
        }

        attacks.remove(attack.getID());
        attacks.put(attack.getID(), attack);

        dataWriter.saveDataLoadMap();

        // Save KiAttack File
        File dir = dataWriter.getDir();
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, attack.getName() + ".json_new");
        File file2 = new File(dir, attack.getName() + ".json");

        try {
            NBTTagCompound nbtTagCompound = (attack).writeToNBT();
            NBTJsonUtil.SaveFile(file, nbtTagCompound);
            if (file2.exists())
                file2.delete();
            file.renameTo(file2);
        } catch (Exception e) {
            LogWriter.except(e);
        }
        return attacks.get(attack.getID());
    }

    @Override
    public void giveKiAttack(IDBCAddon player, IDBCKiAttack attack) {
        attack.giveToPlayer(player);
    }

    @Override
    public void giveKiAttack(IDBCAddon player, String name) {
        CustomKiAttack attack = (CustomKiAttack) getKiAttack(name);
        if (attack != null)
            attack.giveToPlayer(player);
    }

    @Override
    public void giveKiAttack(IDBCAddon player, int id) {
        CustomKiAttack attack = (CustomKiAttack) getKiAttack(id);
        if (attack != null)
            attack.giveToPlayer(player);
    }

    @Override
    public void registerKiAttack(IDBCKiAttack attack) {
        if (!(attack instanceof CustomKiAttack))
            throw new CustomNPCsException("Attack must be an instance of CustomKiAttack");

        CustomKiAttack atk = (CustomKiAttack) attack;

        saveAttack(atk);
    }

    @Override
    public void deleteKiAttack(int id) {
        CustomKiAttack attack = (CustomKiAttack) getKiAttack(id);
        deleteKiAttack(attack);
    }

    @Override
    public void deleteKiAttack(String name) {
        CustomKiAttack attack = (CustomKiAttack) getKiAttack(name);
        deleteKiAttack(attack);
    }

    public void deleteKiAttack(CustomKiAttack attack) {
        if (attack != null) {
            CustomKiAttack foundAttack = this.attacks.remove(attack.getID());
            if (foundAttack != null && foundAttack.getName() != null) {
                dataWriter.deleteFile(foundAttack.getName());
                dataWriter.saveDataLoadMap();
            }
        }
    }
}
