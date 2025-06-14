package com.vidal.gvapi.api;

public interface IGVAPI {
    ISkillHandler getFormSkillHandler();

    ISkillHandler getDBCSkillHandler();

    String getCustomKiHandler();

    String getMentorHandler();
}
