package com.vidal.gvapi.mixins.impl.common;

import noppes.npcs.constants.EnumRoleType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(EnumRoleType.class)
@Unique
public abstract class MixinEnumRoleType {

    @Shadow
    @Final
    @Mutable
    private static EnumRoleType[] $VALUES;

    private static final EnumRoleType MENTOR = gvapi$addVariant("MENTOR");

    @Invoker("<init>")
    private static EnumRoleType gvapi$invokeInit(String internalName, int internalId) {
        throw new AssertionError();
    }

    private static EnumRoleType gvapi$addVariant(final String name) {
        ArrayList<EnumRoleType> variants = new ArrayList<>(Arrays.asList(MixinEnumRoleType.$VALUES));
        EnumRoleType newType = gvapi$invokeInit(name, variants.size());
        variants.add(newType);
        MixinEnumRoleType.$VALUES = variants.toArray(new EnumRoleType[0]);
        return newType;
    }

}
