package com.vidal.gvapi.api;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.INbt;

public interface ICustomKiData {
    void setName(String name);

    void setType(byte type);

    void setSpeed(byte speed);

    void setDamage(byte damage);

    void setEffect(boolean effect);

    void setColor(byte color);

    String getName();

    byte getType();

    byte getSpeed();

    byte getDamage();

    boolean hasEffect();

    byte getColor();

    ICustomKiSounds getSounds();

    ICustomKiUpgrades getUpgrades();

    void setSounds(ICustomKiSounds sounds);

    void setUpgrades(ICustomKiUpgrades upgrades);

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound nbt);

    interface ICustomKiSounds {
        void setCharge(byte charge);
        void setFire(byte fire);
        void setMoving(byte moving);

        byte getCharge();
        byte getFire();
        byte getMoving();
    }

    interface ICustomKiUpgrades {
        void setSpeed(byte speed);
        void setDamageIncrease(byte damageIncrease);
        void setEnergyReduction(byte energyReduction);
        void setCastTime(byte castTime);
        void setCooldown(byte cooldown);
        void setDensity(byte density);
        void setSize(byte size);

        byte getSpeed();
        byte getDamageIncrease();
        byte getEnergyReduction();
        byte getCastTime();
        byte getCooldown();
        byte getDensity();
        byte getSize();
    }
}
