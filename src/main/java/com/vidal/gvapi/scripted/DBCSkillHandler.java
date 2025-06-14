package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.ISkill;
import com.vidal.gvapi.api.ISkillHandler;
import kamkeel.npcdbc.api.IDBCAddon;
import kamkeel.npcdbc.util.DBCUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static JinRyuu.JRMCore.JRMCoreH.DBCSkillNames;
import static JinRyuu.JRMCore.server.config.dbc.JGConfigUltraInstinct.CONFIG_UI_LEVELS;

public class DBCSkillHandler implements ISkillHandler {
    public static DBCSkillHandler Instance = new DBCSkillHandler();
    HashMap<Integer, DBCSkill> skills;
    HashMap<Integer, Integer> skillsMaxLevel;

    private DBCSkillHandler() {
        Instance = this;
        skills = new HashMap<>();
        loadAllSkills();
    }

    public static DBCSkillHandler getInstance() {
        return Instance;
    }

    private void loadAllSkills() {
        Stream<String> DBCNames = Arrays.stream(DBCSkillNames);

        DBCNames.forEach(name -> {
            int id = DBCUtils.getDBCSkillIndex(name);

            DBCSkill skill = DBCSkill.create(name);
            skills.put(id, skill);
        });
    }

    @Override
    public DBCSkill getSkill(int id) {
        return skills.get(id);
    }

    @Override
    public DBCSkill getSkill(String name) {
        DBCSkill[] values = skills.values().toArray(new DBCSkill[0]);

        for (DBCSkill value : values) {
            if (value.getName().equalsIgnoreCase(name))
                return value;
        }

        return null;
    }

    public int getSkillMaxLevel(DBCSkill skill) {
        return skill.getMaxLevel();
    }

    public int getSkillMaxLevel(String name) {
        DBCSkill skill = getSkill(name);
        return getSkillMaxLevel(skill);
    }

    public int getSkillMaxLevel(int id) {
        DBCSkill skill = getSkill(id);
        return getSkillMaxLevel(skill);
    }

    public int[] getAllSkillMaxLevels() {
        skillsMaxLevel = new HashMap<>();

        DBCSkill[] allSkills = getAllSkills();
        ArrayList<Integer> maxLevels = new ArrayList<>();

        for (DBCSkill skill : allSkills) {
            int maxLevel = skill.getMaxLevel();
            maxLevels.add(maxLevel);
        }

        return maxLevels.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public boolean hasSkill(int id) {
        return skills.containsKey(id);
    }

    @Override
    public boolean hasSkill(String name) {
        return getSkill(name) != null;
    }

    public boolean hasSkill(IDBCAddon player, DBCSkill skill) {
        return doesPlayerKnowSkill(player, skill);
    }

    @Override
    public boolean hasSkill(IDBCAddon player, int id) {
        DBCSkill skill = getSkill(id);
        return hasSkill(player, skill);
    }

    @Override
    public boolean hasSkill(IDBCAddon player, String name) {
        DBCSkill skill = getSkill(name);
        return hasSkill(player, skill);
    }

    private boolean doesPlayerKnowSkill(IDBCAddon player, DBCSkill skill) {
        return player.getSkillLevel(skill.name) > 0;
    }

    @Override
    public DBCSkill[] getAllSkills() {
        return skills.values().toArray(new DBCSkill[0]);
    }

    // GIVE SKILL USING ISKILL
    @Override
    public void giveSkill(IDBCAddon player, ISkill skill) {
        if (skill != null && !hasSkill(player, (DBCSkill) skill))
            skill.giveToPlayer(player);
    }

    public void giveSkill(IDBCAddon player, ISkill skill, int level) {
        if (skill != null && !hasSkill(player, (DBCSkill) skill))
            skill.giveToPlayer(player, level, false);
    }

    public void giveSkill(IDBCAddon player, ISkill skill, boolean ignoreTP) {
        if (skill != null && !hasSkill(player, (DBCSkill) skill))
            skill.giveToPlayer(player, 1, ignoreTP);
    }

    public void giveSkill(IDBCAddon player, ISkill skill, int level, boolean ignoreTP) {
        if (skill != null && !hasSkill(player, (DBCSkill) skill))
            skill.giveToPlayer(player, level, ignoreTP);
    }

    // GIVE SKILL USING SKILL NAME
    @Override
    public void giveSkill(IDBCAddon player, String name) {
        DBCSkill skill = getSkill(name);
        giveSkill(player, skill, 1, false);
    }

    public void giveSkill(IDBCAddon player, String name, int level) {
        DBCSkill skill = getSkill(name);
        giveSkill(player, skill, level, false);
    }

    public void giveSkill(IDBCAddon player, String name, boolean ignoreTP) {
        DBCSkill skill = getSkill(name);
        giveSkill(player, skill, 1, ignoreTP);
    }

    public void giveSkill(IDBCAddon player, String name, int level, boolean ignoreTP) {
        DBCSkill skill = getSkill(name);
        giveSkill(player, skill, level, ignoreTP);
    }

    // GIVE SKILL USING SKILL ID
    @Override
    public void giveSkill(IDBCAddon player, int id) {
        DBCSkill skill = getSkill(id);
        giveSkill(player, skill);
    }

    public void giveSkill(IDBCAddon player, int id, int level) {
        DBCSkill skill = getSkill(id);
        giveSkill(player, skill, level);
    }

    public void giveSkill(IDBCAddon player, int id, boolean ignoreTP) {
        DBCSkill skill = getSkill(id);
        giveSkill(player, skill, ignoreTP);
    }

    public void giveSkill(IDBCAddon player, int id, int level, boolean ignoreTP) {
        DBCSkill skill = getSkill(id);
        giveSkill(player, skill, level, ignoreTP);
    }

    // GIVE ALL SKILLS
    public void giveAllSkills(IDBCAddon player) {
        giveSkills(player, 1, false);
    }

    public void giveAllSkills(IDBCAddon player, int level) {
        giveSkills(player, level, false);
    }

    public void giveAllSkills(IDBCAddon player, boolean ignoreTP) {
        giveSkills(player, 1, ignoreTP);
    }

    public void giveAllSkills(IDBCAddon player, int level, boolean ignoreTP) {
        giveSkills(player, level, ignoreTP);
    }

    private void giveSkills(IDBCAddon player, int level, boolean ignoreTP) {
        DBCSkill[] skills = getAllSkills();

        for (DBCSkill skill : skills) {
            if (hasSkill(player, skill))
                continue;

            giveSkill(player, skill, level, ignoreTP);
        }
    }

    public void removeSkill(IDBCAddon player, DBCSkill skill) {
        if (skill != null && hasSkill(player, skill))
            skill.removeFromPlayer(player);
    }

    public void removeSkill(IDBCAddon player, DBCSkill skill, boolean refund) {
        if (skill != null && hasSkill(player, skill))
            skill.removeFromPlayer(player, refund);
    }

    public void removeSkill(IDBCAddon player, String name) {
        DBCSkill skill = getSkill(name);
        removeSkill(player, skill);
    }

    public void removeSkill(IDBCAddon player, String name, boolean refund) {
        DBCSkill skill = getSkill(name);
        removeSkill(player, skill, refund);
    }

    public void removeSkill(IDBCAddon player, int id) {
        DBCSkill skill = getSkill(id);
        removeSkill(player, skill);
    }

    public void removeSkill(IDBCAddon player, int id, boolean refund) {
        DBCSkill skill = getSkill(id);
        removeSkill(player, skill, refund);
    }

    public void removeAllSkills(IDBCAddon player) {
        removeSkills(player, true);
    }

    public void removeAllSkills(IDBCAddon player, boolean refund) {
        removeSkills(player, refund);
    }

    private void removeSkills(IDBCAddon player, boolean refund) {
        DBCSkill[] skills = getAllSkills();

        for (DBCSkill skill : skills) {
            if (!hasSkill(player, skill))
                continue;

            removeSkill(player, skill, refund);
        }
    }

    @Override
    public void registerSkill(ISkill skill) {

    }

    @Override
    public void deleteSkill(int id) {

    }

    @Override
    public void deleteSkill(String id) {

    }
}
