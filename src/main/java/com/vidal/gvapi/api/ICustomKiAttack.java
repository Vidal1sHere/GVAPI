package com.vidal.gvapi.api;

public interface ICustomKiAttack extends IDBCKiAttack{
    ICustomKiData getAttackData();

    void setAttackData(ICustomKiData data);
}
