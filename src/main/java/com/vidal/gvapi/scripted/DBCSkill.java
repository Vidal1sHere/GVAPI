package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.ISkill;
import kamkeel.npcdbc.api.IDBCAddon;
import kamkeel.npcdbc.util.DBCUtils;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

import static JinRyuu.JRMCore.JRMCoreH.DBCSkillsIDs;
import static JinRyuu.JRMCore.server.config.dbc.JGConfigUltraInstinct.CONFIG_UI_LEVELS;

public class DBCSkill implements ISkill {

    public int id = 0;
    public String name = "";
    public int tpCost = 0;
    public int mindCost = 0;

    private DBCSkill(String name) {
        this.name = name;
        this.id = DBCUtils.getDBCSkillIndex(this.name);
        this.tpCost = DBCUtils.calculateDBCSkillTPCost(this.id, 1);
        this.mindCost = DBCUtils.calculateDBCSkillMindCost(this.id, 1);
    }

    public static DBCSkill create(String name) {
        if (DBCUtils.getDBCSkillIndex(name) == -1)
            return null;

        return new DBCSkill(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setTPCost(int cost) {

    }

    @Override
    public void setMindCost(int cost) {

    }

    @Override
    public void giveToPlayer(IDBCAddon player) {
        giveSkillToPlayer(player, 1, false);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, int level) {
        giveSkillToPlayer(player, Math.max(1, Math.min(getMaxLevel(), level)), false);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, boolean ignoreTP) {
        giveSkillToPlayer(player, 1, ignoreTP);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, int level, boolean ignoreTP) {
        giveSkillToPlayer(player, Math.max(1, Math.min(getMaxLevel(), level)), ignoreTP);
    }

    private void giveSkillToPlayer(IDBCAddon player, int level, boolean ignoreTP) {
        if (player.getSkillLevel(this.name) > 0)
            return;

        int TP = player.getTP();
        if (TP < this.tpCost)
            return;

        level -= 1;

        String skills = player.getSkills();

        String dbcId = getDBCName() + level;

        if (skills.equals(","))
            player.setSkills(dbcId);
        else
            player.setSkills(skills + "," + dbcId);

        if (ignoreTP) return;

        player.setTP(player.getTP() - this.tpCost);
    }

    @Override
    public void removeFromPlayer(IDBCAddon player) {
        removeSkillFromPlayer(player, true);
    }

    public void removeFromPlayer(IDBCAddon player, boolean refund) {
        removeSkillFromPlayer(player, refund);
    }

    private void removeSkillFromPlayer(IDBCAddon player, boolean refund) {
        if (player.getSkillLevel(this.name) < 1)
            return;

        String skills = player.getSkills();
        String newSkills = removeSkillFromString(skills, getDBCName(), player.getSkillLevel(this.name));

        player.setSkills(newSkills);

        if (refund)
            player.setTP(player.getTP() + this.tpCost);
    }

    private String removeSkillFromString(String string, String id, int level) {
        String[] skills = string.split(",");
        level -= 1;

        List<String> skillList = new ArrayList<>();
        for (String skill : skills) {
            skillList.add(skill.trim());
        }

        skillList.remove(id + (level + ""));

        if (skillList.isEmpty()) {
            return ",";
        }

        return String.join(",", skillList);
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTPCost() {
        return this.tpCost;
    }

    public int getTPCost(int level) {
        return DBCUtils.calculateDBCSkillTPCost(this.id, Math.max(1, Math.min(getMaxLevel(), level)));
    }

    @Override
    public int getMindCost() {
        return this.mindCost;
    }

    public int getMindCost(int level) {
        return DBCUtils.calculateDBCSkillTPCost(this.id, Math.max(1, Math.min(getMaxLevel(), level)));
    }

    public String getDBCName() {
        int id = DBCUtils.getDBCSkillIndex(this.name);
        return DBCSkillsIDs[id];
    }

    public int getMaxLevel() {
        int level = 10;

        switch (getDBCName()) {
            case "GF" -> level = 3;
            case "GD" -> level = 1;
            case "UI" -> level = CONFIG_UI_LEVELS;
        }

        return level;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setString("name", name);
        compound.setInteger("ID", id);
        compound.setInteger("tpCost", tpCost);
        compound.setInteger("mindCost", mindCost);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        name = compound.getString("name");
        id = compound.getInteger("ID");
        tpCost = compound.getInteger("tpCost");
        mindCost = compound.getInteger("mindCost");
    }
}
