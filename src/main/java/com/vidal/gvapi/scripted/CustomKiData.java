package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.ICustomKiData;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.INbt;

import java.util.*;

public class CustomKiData implements ICustomKiData {
    private String name;
    private int id;
    private byte type, speed, damage, color;
    private boolean effect;
    private CustomKiSounds sounds;
    private CustomKiUpgrades upgrades;

    public CustomKiData(String name, int id, byte type, byte speed, byte damage, boolean effect, byte color, CustomKiSounds sounds, CustomKiUpgrades upgrades) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.speed = speed;
        this.damage = damage;
        this.effect = effect;
        this.color = color;
        this.sounds = sounds;
        this.upgrades = upgrades;
    }

    public static ICustomKiData create(String name, int id, Map<String, Object> settings) {
        settings = tweakSettings(name, id, settings);

        byte type = ((Number) settings.get("type")).byteValue();
        byte speed = ((Number) settings.get("speed")).byteValue();
        byte damage = ((Number) settings.get("damage")).byteValue();
        byte color = ((Number) settings.get("color")).byteValue();
        boolean effect = ((Boolean) settings.get("effect"));

        type = (byte) Math.max(0, Math.min(8, type));
        speed = type == 7 || type == 8 ? 1 : (byte) Math.max(0, Math.min(2, speed));
        damage = (byte) Math.max(0, Math.min(100, damage));
        color = (byte) Math.max(0, Math.min(30, color));

        Map<String, Object> upgradesMap = (Map<String, Object>) settings.get("upgrades");
        byte upgradeSpeed = ((Number) upgradesMap.get("speed")).byteValue();
        byte damageIncrease = ((Number) upgradesMap.get("damageIncrease")).byteValue();
        byte energyReduction = ((Number) upgradesMap.get("energyReduction")).byteValue();
        byte castTime = ((Number) upgradesMap.get("casttime")).byteValue();
        byte cooldown = ((Number) upgradesMap.get("cooldown")).byteValue();
        byte density = ((Number) upgradesMap.get("density")).byteValue();
        byte size = ((Number) upgradesMap.get("size")).byteValue();

        Map<String, Object> soundsMap = (Map<String, Object>) settings.get("sounds");
        byte charge = ((Number) soundsMap.get("charge")).byteValue();
        byte fire = ((Number) soundsMap.get("fire")).byteValue();
        byte moving = ((Number) soundsMap.get("moving")).byteValue();

        CustomKiData.CustomKiSounds sounds = tweakSounds(type, new CustomKiData.CustomKiSounds(charge, fire, moving));
        CustomKiData.CustomKiUpgrades upgrades = tweakUpgrades(new CustomKiData.CustomKiUpgrades(upgradeSpeed, damageIncrease, energyReduction, castTime, cooldown, density, size));

        return new CustomKiData(name, id, type, speed, damage, effect, color, sounds, upgrades);
    }

    private static Map<String, Object> tweakSettings(String name, int id, Map<String, Object> settings) {
        if (settings == null)
            settings = new HashMap<String, Object>();

        settings.put("id", id);
        settings.put("name", name);

        settings.putIfAbsent("type", 0);
        settings.putIfAbsent("speed", 0);
        settings.putIfAbsent("damage", 10);
        settings.putIfAbsent("effect", false);
        settings.putIfAbsent("color", 0);

        Map<String, Object> sounds = (Map<String, Object>) settings.getOrDefault("sounds", new HashMap<>());
        sounds.putIfAbsent("charge", 0);
        sounds.putIfAbsent("fire", 0);
        sounds.putIfAbsent("moving", 0);
        settings.put("sounds", sounds);

        Map<String, Object> upgrades = (Map<String, Object>) settings.getOrDefault("upgrades", new HashMap<>());
        upgrades.putIfAbsent("speed", 0);
        upgrades.putIfAbsent("damageIncrease", 0);
        upgrades.putIfAbsent("energyReduction", 0);
        upgrades.putIfAbsent("casttime", 0);
        upgrades.putIfAbsent("cooldown", 0);
        upgrades.putIfAbsent("density", 0);
        upgrades.putIfAbsent("size", 0);
        settings.put("upgrades", upgrades);

        return settings;
    }

    private static List<Object> getKiUpgrades(Map<String, Object> upgrades) {
        Object[] values = upgrades.values().toArray();
        int[] kiUpgrades = new int[values.length];

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            kiUpgrades[i] = ((Number) value).intValue();
        }

        int level = 0;
        String kiUpgradesString = "";
        for (int i = 0; i < kiUpgrades.length; i++) {
            level += kiUpgrades[i];
            kiUpgradesString += kiUpgrades[i] + (i == kiUpgrades.length - 1 ? "" : ",");
        }

        ArrayList<Object> kiUpgradeData = new ArrayList<>();

        kiUpgradeData.add(0);
        kiUpgradeData.add(0);
        kiUpgradeData.add(2, level);
        kiUpgradeData.add(0);
        kiUpgradeData.add(4, kiUpgradesString);

        return kiUpgradeData;
    }

    private static CustomKiSounds tweakSounds(byte type, ICustomKiSounds sounds) {
        switch (type) {
            case 2 -> {
                sounds.setCharge((byte) Math.max(0, Math.min(1, sounds.getCharge())));
                sounds.setFire((byte) 0);
                sounds.setMoving((byte) 0);
            }
            case 3 -> {
                sounds.setCharge((byte) 0);
                sounds.setFire((byte) Math.max(0, Math.min(1, sounds.getFire())));
                sounds.setMoving((byte) 0);
            }
            default -> {
                sounds.setCharge((byte) Math.max(0, Math.min(12, sounds.getCharge())));
                sounds.setFire((byte) Math.max(0, Math.min(12, sounds.getFire())));
                sounds.setMoving((byte) Math.max(0, Math.min(1, sounds.getMoving())));
            }
        }

        return (CustomKiSounds) sounds;
    }

    private static CustomKiUpgrades tweakUpgrades(ICustomKiUpgrades upgrades) {
        upgrades.setSpeed((byte) Math.max(0, Math.min(upgrades.getSpeed(), 10)));
        upgrades.setDamageIncrease((byte) Math.max(0, Math.min(upgrades.getDamageIncrease(), 10)));
        upgrades.setEnergyReduction((byte) Math.max(0, Math.min(upgrades.getEnergyReduction(), 10)));
        upgrades.setCastTime((byte) Math.max(0, Math.min(upgrades.getCastTime(), 10)));
        upgrades.setCooldown((byte) Math.max(0, Math.min(upgrades.getCooldown(), 10)));
        upgrades.setDensity((byte) Math.max(0, Math.min(upgrades.getDensity(), 10)));
        upgrades.setSize((byte) Math.max(0, Math.min(upgrades.getSize(), 10)));

        return (CustomKiUpgrades) upgrades;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setType(byte type) {
        type = (byte) Math.max(0, Math.min(8, type));
        this.type = type;
    }

    @Override
    public void setSpeed(byte speed) {
        if (this.type == 7 || this.type == 8)
            return;

        speed = (byte) Math.max(0, Math.min(2, speed));
        this.speed = speed;
    }

    @Override
    public void setDamage(byte damage) {
        damage = (byte) Math.max(0, Math.min(100, damage));
        this.damage = damage;
    }

    @Override
    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    @Override
    public void setColor(byte color) {
        color = (byte) Math.max(0, Math.min(30, color));
        this.color = color;
    }

    @Override
    public void setSounds(ICustomKiSounds sounds) {
        this.sounds = (CustomKiSounds) sounds;
    }

    @Override
    public void setUpgrades(ICustomKiUpgrades upgrades) {
        this.upgrades = (CustomKiUpgrades) upgrades;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte getType() {
        return this.type;
    }

    @Override
    public byte getSpeed() {
        return this.speed;
    }

    @Override
    public byte getDamage() {
        return this.damage;
    }

    @Override
    public boolean hasEffect() {
        return this.effect;
    }

    @Override
    public byte getColor() {
        return this.color;
    }

    @Override
    public ICustomKiSounds getSounds() {
        return this.sounds;
    }

    @Override
    public ICustomKiUpgrades getUpgrades() {
        return this.upgrades;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setByte("type", type);
        compound.setByte("speed", speed);
        compound.setByte("damage", damage);
        compound.setByte("color", color);
        compound.setBoolean("effect", effect);

        NBTTagCompound soundsC = new NBTTagCompound();
        soundsC.setByte("charge", sounds.charge);
        soundsC.setByte("fire", sounds.fire);
        soundsC.setByte("moving", sounds.moving);

        NBTTagCompound upgradesC = new NBTTagCompound();
        upgradesC.setByte("speed", upgrades.speed);
        upgradesC.setByte("damageIncrease", upgrades.damageIncrease);
        upgradesC.setByte("energyReduction", upgrades.energyReduction);
        upgradesC.setByte("castTime", upgrades.castTime);
        upgradesC.setByte("cooldown", upgrades.cooldown);
        upgradesC.setByte("density", upgrades.density);
        upgradesC.setByte("size", upgrades.size);

        compound.setTag("sounds", soundsC);
        compound.setTag("upgrades", upgradesC);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.setType(nbt.getByte("type"));
        this.setSpeed(nbt.getByte("speed"));
        this.setDamage(nbt.getByte("damage"));
        this.setColor(nbt.getByte("color"));
        this.setEffect(nbt.getBoolean("effect"));

        NBTTagCompound soundsC = nbt.getCompoundTag("sounds");
        this.getSounds().setCharge(soundsC.getByte("charge"));
        this.getSounds().setFire(soundsC.getByte("fire"));
        this.getSounds().setMoving(soundsC.getByte("moving"));

        NBTTagCompound upgradesC = nbt.getCompoundTag("upgrades");
        this.getUpgrades().setSpeed(upgradesC.getByte("speed"));
        this.getUpgrades().setDamageIncrease(upgradesC.getByte("damageIncrease"));
        this.getUpgrades().setEnergyReduction(upgradesC.getByte("energyReduction"));
        this.getUpgrades().setCastTime(upgradesC.getByte("castTime"));
        this.getUpgrades().setCooldown(upgradesC.getByte("cooldown"));
        this.getUpgrades().setDensity(upgradesC.getByte("density"));
        this.getUpgrades().setSize(upgradesC.getByte("size"));
    }

    public String toString() {
        byte charge = sounds.charge;
        byte fire = sounds.fire;
        byte moving = sounds.moving;

        List<Object> kiData = new ArrayList<>(Arrays.asList(
            name, 0, id + " ", type, speed, damage, effect ? 1 : 0,
            0, 0, 0, color, 1,
            charge, fire, moving
        ));

        HashMap<String, Object> upgradesMap = new HashMap<>();
        upgradesMap.put("speed", upgrades.speed);
        upgradesMap.put("damageIncrease", upgrades.damageIncrease);
        upgradesMap.put("energyReduction", upgrades.energyReduction);
        upgradesMap.put("castTime", upgrades.castTime);
        upgradesMap.put("cooldown", upgrades.cooldown);
        upgradesMap.put("density", upgrades.density);
        upgradesMap.put("size", upgrades.size);

        List<Object> kiUpgrades = getKiUpgrades(upgradesMap);

        for (Object value : kiUpgrades) {
            kiData.add(value);
        }

        String kiDataString = "";
        for (int i = 0; i < kiData.size(); i++) {
            String value = kiData.get(i).toString();
            kiDataString += value + (i == kiData.size() - 1 ? "" : ";");
        }

        return kiDataString;
    }

    private static class CustomKiSounds implements ICustomKiSounds {
        private byte charge;
        private byte fire;
        private byte moving;

        public CustomKiSounds(byte charge, byte fire, byte moving) {
            this.charge = charge;
            this.fire = fire;
            this.moving = moving;
        }

        @Override
        public void setCharge(byte charge) {
            this.charge = charge;
        }

        @Override
        public void setFire(byte fire) {
            this.fire = fire;
        }

        @Override
        public void setMoving(byte moving) {
            this.moving = moving;
        }

        @Override
        public byte getCharge() {
            return this.charge;
        }

        @Override
        public byte getFire() {
            return this.fire;
        }

        @Override
        public byte getMoving() {
            return this.moving;
        }
    }

    private static class CustomKiUpgrades implements ICustomKiUpgrades {
        byte speed, damageIncrease, energyReduction, castTime, cooldown, density, size;

        private CustomKiUpgrades(byte speed, byte damageIncrease, byte energyReduction,
                                byte castTime, byte cooldown, byte density, byte size) {
            this.speed = speed;
            this.damageIncrease = damageIncrease;
            this.energyReduction = energyReduction;
            this.castTime = castTime;
            this.cooldown = cooldown;
            this.density = density;
            this.size = size;
        }

        @Override
        public void setSpeed(byte speed) {
            this.speed = speed;
        }

        @Override
        public void setDamageIncrease(byte damageIncrease) {
            this.damageIncrease = damageIncrease;
        }

        @Override
        public void setEnergyReduction(byte energyReduction) {
            this.energyReduction = energyReduction;
        }

        @Override
        public void setCastTime(byte castTime) {
            this.castTime = castTime;
        }

        @Override
        public void setCooldown(byte cooldown) {
            this.cooldown = cooldown;
        }

        @Override
        public void setDensity(byte density) {
            this.density = density;
        }

        @Override
        public void setSize(byte size) {
            this.size = size;
        }

        @Override
        public byte getSpeed() {
            return this.speed;
        }

        @Override
        public byte getDamageIncrease() {
            return this.damageIncrease;
        }

        @Override
        public byte getEnergyReduction() {
            return this.energyReduction;
        }

        @Override
        public byte getCastTime() {
            return this.castTime;
        }

        @Override
        public byte getCooldown() {
            return this.cooldown;
        }

        @Override
        public byte getDensity() {
            return this.density;
        }

        @Override
        public byte getSize() {
            return this.size;
        }
    }
}
