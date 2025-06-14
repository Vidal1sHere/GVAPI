package com.vidal.gvapi.api;

import kamkeel.npcdbc.api.IDBCAddon;

public interface ISkillHandler {

    void registerSkill(ISkill skill);

    void deleteSkill(int id);

    void deleteSkill(String id);

    ISkill getSkill(int id);

    ISkill getSkill(String name);

    boolean hasSkill(int id);

    boolean hasSkill(String name);

    boolean hasSkill(IDBCAddon player, int id);

    boolean hasSkill(IDBCAddon player, String name);

    ISkill[] getAllSkills();

    void giveSkill(IDBCAddon player, ISkill skill);

    void giveSkill(IDBCAddon player, String skill);

    void giveSkill(IDBCAddon player, int skill);
}
