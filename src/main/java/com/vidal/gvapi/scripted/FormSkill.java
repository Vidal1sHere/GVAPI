package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.ISkill;
import kamkeel.npcdbc.api.IDBCAddon;
import kamkeel.npcdbc.api.form.IForm;
import kamkeel.npcdbc.controllers.FormController;
import noppes.npcs.scripted.CustomNPCsException;

public class FormSkill implements ISkill {
    private int id = -1;
    private String name = "";
    private int tpCost = 0;
    private int mindCost = 0;

    private FormSkill(int id, String name, int tpCost, int mindCost) {
        this.id = id;
        this.name = name;
        this.tpCost = tpCost;
        this.mindCost = mindCost;
    }

    public static FormSkill create(int id, String name, int tpCost, int mindCost) {
        if (!FormController.getInstance().has(id))
            throw new CustomNPCsException("Form with ID " + id + " does not exist!");

        FormController.getInstance().get(id).setMindRequirement(mindCost);

        return new FormSkill(id, name, tpCost, mindCost);
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setTPCost(int cost) {
        this.tpCost = cost;
    }

    @Override
    public void setMindCost(int cost) {
        this.mindCost = cost;
        FormController.getInstance().get(this.id).setMindRequirement(this.mindCost);
    }

    @Override
    public void giveToPlayer(IDBCAddon player) {
        giveSkillToPlayer(player, 0, false);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, int level) {
        giveSkillToPlayer(player, level, false);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, boolean ignoreTP) {
        giveSkillToPlayer(player, 0, ignoreTP);
    }

    @Override
    public void giveToPlayer(IDBCAddon player, int level, boolean ignoreTP) {
        giveSkillToPlayer(player, level, ignoreTP);
    }

    private void giveSkillToPlayer(IDBCAddon player, int level, boolean ignoreTP) {
        int TP = player.getTP();
        if (TP < this.tpCost && !ignoreTP)
            return;

        IForm form = FormController.getInstance().get(this.id);

        if (!form.raceEligible(player))
            return;

        form.assignToPlayer(player);

        if (level > 0)
            player.setCustomMastery(form, level);

        if (ignoreTP) return;

        player.setTP(TP - this.tpCost);
    }

    @Override
    public void removeFromPlayer(IDBCAddon player) {
        removeFormFromPlayer(player, true, false);
    }

    public void removeFromPlayer(IDBCAddon player, boolean refund) {
        removeFormFromPlayer(player, refund, false);
    }

    public void removeFromPlayer(IDBCAddon player, boolean refund, boolean removeMastery) {
        removeFormFromPlayer(player, refund, removeMastery);
    }

    private void removeFormFromPlayer(IDBCAddon player, boolean refund, boolean removeMastery) {
        IForm form = FormController.getInstance().get(this.id);

        form.removeFromPlayer(player, removeMastery);

        if (refund)
            player.setTP(player.getTP() + this.tpCost);
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

    @Override
    public int getMindCost() {
        return this.mindCost;
    }
}
