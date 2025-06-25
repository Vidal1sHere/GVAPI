package com.vidal.gvapi.mixins;

import noppes.npcs.constants.EnumRoleType;

public class RoleTypeHelper {

    private static EnumRoleType internal_MENTOR = null;

    public static EnumRoleType mentor() {
        if (internal_MENTOR == null) {
            internal_MENTOR = internal_getMentor();
        }
        return internal_MENTOR;
    }

    private static EnumRoleType internal_getMentor() {
        for (EnumRoleType type : EnumRoleType.values()) {
            if ("MENTOR".equals(type.name())) return type;
        }
        throw new IllegalStateException("MENTOR not found in EnumRoleType");
    }
}
