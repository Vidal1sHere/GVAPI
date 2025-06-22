package com.vidal.gvapi.api;

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

    ISounds getSounds();

    IUpgrades getUpgrades();

    void setSounds(ISounds sounds);

    void setUpgrades(IUpgrades upgrades);

    interface ISounds {
        void setCharge(byte charge);
        void setFire(byte fire);
        void setMoving(byte moving);

        byte getCharge();
        byte getFire();
        byte getMoving();
    }

    interface IUpgrades {
        void setSpeed(byte speed);
        void setDamageIncrease(byte damageIncrease);
        void setEnergyReduction(byte energyReduction);
        void setCastTime(byte castTime);
        void setDensity(byte density);
        void setSize(byte size);

        byte getSpeed();
        byte getDamageIncrease();
        byte getEnergyReduction();
        byte getCastTime();
        byte getDensity();
        byte getSize();
    }
}
