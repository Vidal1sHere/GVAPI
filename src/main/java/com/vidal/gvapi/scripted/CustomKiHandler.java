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

    public CustomKiAttack createFromString(String data) {
        String[] splitData = data.split(";");

        int id;
        String name = splitData[0];
        String idName = splitData[2];
        int tpCost = 0;
        byte type = (byte) Integer.parseInt(splitData[3]);
        byte speed = (byte) Integer.parseInt(splitData[4]);
        byte damage = (byte) Integer.parseInt(splitData[5]);
        boolean effect = Integer.parseInt(splitData[6]) == 1;
        byte color = (byte) Integer.parseInt(splitData[10]);
        byte charge = (byte) Integer.parseInt(splitData[12]);
        byte fire = (byte) Integer.parseInt(splitData[13]);
        byte moving = (byte) Integer.parseInt(splitData[14]);

        try {
            id = Integer.parseInt(idName.trim());
        } catch (NumberFormatException e) {
            id = dataWriter.getUnusedId();
        }

        HashMap<String, Object> settings = new HashMap<>();

        settings.put("type", type);
        settings.put("speed", speed);
        settings.put("damage", damage);
        settings.put("color", color);
        settings.put("effect", effect);

        HashMap<String, Object> sounds = new HashMap<>();
        sounds.put("charge", charge);
        sounds.put("fire", fire);
        sounds.put("moving", moving);
        settings.put("sounds", sounds);

        HashMap<String, Object> upgrades;

        if (splitData.length > 15) {
            upgrades = getKiUpgradeData(data);
        } else {
            upgrades = new HashMap<>();
            upgrades.put("speed", 0);
            upgrades.put("damageIncrease", 0);
            upgrades.put("energyReduction", 0);
            upgrades.put("castTime", 0);
            upgrades.put("cooldown", 0);
            upgrades.put("density", 0);
            upgrades.put("size", 0);
        }

        settings.put("upgrades", upgrades);

        return CustomKiAttack.create(name, id, tpCost, settings);
    }

    private HashMap<String, Object> getKiUpgradeData(String data) {
        HashMap<String, Object> upgrades = new HashMap<>();
        String[] dataSplit = data.split(";")[19].split(",");
        String[] dataNames = {"speed", "damageIncrease", "energyReduction", "castTime", "cooldown", "density", "size"};

        for (int i = 0; i < dataSplit.length; i++) {
            int upgrade = Integer.parseInt(dataSplit[i]);
            upgrades.put(dataNames[i], upgrade);
        }

        return upgrades;
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
                    CustomKiAttack attack = CustomKiAttack.create();
                    NBTTagCompound nbt = NBTJsonUtil.LoadFile(file);
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

    public void registerFromSlot(IDBCAddon player, Integer slot) {
        slot = Math.max(1, Math.min(4, slot));

        String tech = player.getNbt().getCompound("PlayerPersisted").getString("jrmcTech" + slot);
        if (tech == null || tech.trim().isEmpty())
            return;

        CustomKiAttack attack = createFromString(tech);
        registerKiAttack(attack);
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
