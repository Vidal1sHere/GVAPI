package com.vidal.gvapi.scripted;

import com.vidal.gvapi.api.IGVAPI;

public class GVAPI implements IGVAPI {
    private static GVAPI instance;

    private final FormSkillHandler formSkillHandler = FormSkillHandler.getInstance();
    private final DBCSkillHandler dbcSkillHandler = DBCSkillHandler.getInstance();
    private final String kiHandler = "new MiHandler()";
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
    public String getCustomKiHandler() {
        return kiHandler;
    }

    @Override
    public String getMentorHandler() {
        return mentorHandler;
    }
}
