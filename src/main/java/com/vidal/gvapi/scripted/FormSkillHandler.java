package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.ISkill;
import com.vidal.gvapi.api.ISkillHandler;
import kamkeel.npcdbc.api.IDBCAddon;

import java.util.HashMap;

public class FormSkillHandler implements ISkillHandler {
    public static FormSkillHandler Instance = new FormSkillHandler();
    HashMap<Integer, FormSkill> skills;

    private FormSkillHandler() {
        Instance = this;
        skills = new HashMap<>();
    }

    public static FormSkillHandler getInstance() {
        return Instance;
    }

    public FormSkill createSkill(int id, String name, int tpCost, int mindCost) {
        return FormSkill.create(id, name, tpCost, mindCost);
    }

    @Override
    public void registerSkill(ISkill skill) {
        if (skill == null)
            return;

        skills.put(skill.getID(), (FormSkill) skill);
    }

    @Override
    public void deleteSkill(int id) {
        skills.remove(id);
    }

    @Override
    public void deleteSkill(String id) {
        FormSkill skill = getSkill(id);

        skills.remove(skill.getID());
    }

    @Override
    public FormSkill getSkill(int id) {
        if (skills.containsKey(id))
            return skills.get(id);

        return null;
    }

    @Override
    public FormSkill getSkill(String name) {
        FormSkill[] values = skills.values().toArray(new FormSkill[0]);

        for (FormSkill value : values) {
            if (value.getName().equals(name))
                return value;
        }

        return null;
    }

    @Override
    public boolean hasSkill(int id) {
        return skills.containsKey(id);
    }

    @Override
    public boolean hasSkill(String name) {
        FormSkill form = (FormSkill) getSkill(name);
        return form != null;
    }

    public boolean hasSkill(IDBCAddon player, FormSkill skill) {
        return hasSkill(player, skill.getID());
    }

    @Override
    public boolean hasSkill(IDBCAddon player, int id) {
        return player.hasCustomForm(id);
    }

    @Override
    public boolean hasSkill(IDBCAddon player, String name) {
        return player.hasCustomForm(name);
    }

    @Override
    public FormSkill[] getAllSkills() {
        return skills.values().toArray(new FormSkill[0]);
    }

    @Override
    public void giveSkill(IDBCAddon player, ISkill skill) {
        if (skill != null)
            skill.giveToPlayer(player);
    }

    @Override
    public void giveSkill(IDBCAddon player, String name) {
        FormSkill skill = getSkill(name);

        giveSkill(player, skill);
    }

    @Override
    public void giveSkill(IDBCAddon player, int id) {
        FormSkill skill = getSkill(id);

        giveSkill(player, skill);
    }


    public void removeSkill(IDBCAddon player, FormSkill skill) {
        if (skill != null)
            skill.removeFromPlayer(player);
    }

    public void removeSkill(IDBCAddon player, FormSkill skill, boolean refund) {
        if (skill != null)
            skill.removeFromPlayer(player, refund);
    }

    public void removeSkill(IDBCAddon player, FormSkill skill, boolean refund, boolean removeMastery) {
        if (skill != null)
            skill.removeFromPlayer(player, refund, removeMastery);
    }

    public void removeSkill(IDBCAddon player, String name) {
        FormSkill skill = getSkill(name);
        removeSkill(player, skill);
    }

    public void removeSkill(IDBCAddon player, String name, boolean refund) {
        FormSkill skill = getSkill(name);
        removeSkill(player, skill, refund);
    }

    public void removeSkill(IDBCAddon player, String name, boolean refund, boolean removeMastery) {
        FormSkill skill = getSkill(name);
        removeSkill(player, skill, refund, removeMastery);
    }

    public void removeSkill(IDBCAddon player, int id) {
        FormSkill skill = getSkill(id);
        removeSkill(player, skill);
    }

    public void removeSkill(IDBCAddon player, int id, boolean refund) {
        FormSkill skill = getSkill(id);
        removeSkill(player, skill, refund);
    }

    public void removeSkill(IDBCAddon player, int id, boolean refund, boolean removeMastery) {
        FormSkill skill = getSkill(id);
        removeSkill(player, skill, refund, removeMastery);
    }
}
