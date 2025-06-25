package com.vidal.gvapi.api.roles;

import com.vidal.gvapi.api.IDBCKiAttack;
import com.vidal.gvapi.api.ISkill;
import noppes.npcs.api.roles.IRole;

public interface IRoleMentor extends IRole {

    void setAlignment(int alignment);
    int getAlignment();

    void setSkill(ISkill skill);
    ISkill getSkill(int id);
    ISkill getSkill(String name);
    ISkill[] getAllSkills();

    void removeSkill(ISkill skill);
    void removeSkill(int id);
    void removeSkill(String name);

    void setTechnique(IDBCKiAttack technique);
    IDBCKiAttack getTechnique(int id);
    IDBCKiAttack getTechnique(String name);
    IDBCKiAttack[] getAllTechniques();

    void removeTechnique(IDBCKiAttack technique);
    void removeTechnique(String name);
    void removeTechnique(int id);
}
