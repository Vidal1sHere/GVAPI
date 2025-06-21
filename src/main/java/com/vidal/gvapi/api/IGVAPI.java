package com.vidal.gvapi.api;

public interface IGVAPI {
    ISkillHandler getFormSkillHandler();

    ISkillHandler getDBCSkillHandler();

    IKiHandler getDBCKiHandler();

    String getCustomKiHandler();

    String getMentorHandler();
}
