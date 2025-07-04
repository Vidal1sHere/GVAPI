package com.vidal.gvapi.mixins.late.impl.common;

import com.vidal.gvapi.mixins.RoleTypeHelper;
import noppes.npcs.DataAdvanced;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DataAdvanced.class, remap = false)
public abstract class MixinDataAdvanced {

    @Shadow(remap = false)
    public EnumRoleType role;
    @Shadow(remap = false)
    public EntityNPCInterface npc;

    @Inject(method = "setRole", at = @At("TAIL"), remap = false)
    private void updateRoleSetting(int i, CallbackInfo ci) {
        // TODO change this if statement to this when you add mentor class
        // if (this.role == RoleTypeHelper.mentor() && !(this.npc.roleInterface instanceof MentorClassHere))
        if (this.role == RoleTypeHelper.mentor()) {

            // TODO set roleInterface to new mentor instance.
            // this.npc.roleInterace = new MentorClassHere(npc);
        }
    }
}
