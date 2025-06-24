package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.IGVAPI;
import com.vidal.gvapi.api.IKiHandler;

public class GVAPI implements IGVAPI {
    private static GVAPI instance;

    private final FormSkillHandler formSkillHandler = FormSkillHandler.getInstance();
    private final DBCSkillHandler dbcSkillHandler = DBCSkillHandler.getInstance();
    private final DBCKiHandler dbcKiHandler = DBCKiHandler.getInstance();
    private final CustomKiHandler customKiHandler = CustomKiHandler.getInstance();
    private final String mentorHandler = "new MentorHandler()";

    public static GVAPI getInstance() {
        if (instance != null)
            return instance;

        instance = new GVAPI();

        return instance;
    }

    @Override
    public FormSkillHandler getFormSkillHandler() {
        return formSkillHandler;
    }

    @Override
    public DBCSkillHandler getDBCSkillHandler() {
        return dbcSkillHandler;
    }

    @Override
    public IKiHandler getDBCKiHandler() {
        return dbcKiHandler;
    }

    @Override
    public IKiHandler getCustomKiHandler() {
        return customKiHandler;
    }

    @Override
    public String getMentorHandler() {
        return mentorHandler;
    }
}
