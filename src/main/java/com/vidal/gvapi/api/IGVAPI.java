package com.vidal.gvapi.api;

public interface IGVAPI {
    ISkillHandler getFormSkillHandler();

    ISkillHandler getDBCSkillHandler();

    IKiHandler getDBCKiHandler();

    IKiHandler getCustomKiHandler();

    String getMentorHandler();
}
