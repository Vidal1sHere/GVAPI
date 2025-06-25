package com.vidal.gvapi.mixins.impl.common;

import com.vidal.gvapi.mixins.RoleTypeHelper;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.roles.IRole;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.scripted.entity.ScriptLiving;
import noppes.npcs.scripted.entity.ScriptNpc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ScriptNpc.class, remap = false)
public abstract class MixinScriptNpc<T extends EntityNPCInterface> extends ScriptLiving<T> implements ICustomNpc {

    @Shadow(remap = false)
    public EntityNPCInterface npc;

    public MixinScriptNpc(T entity) {
        super(entity);
    }

    @Inject(method = "getRole", at = @At("HEAD"), remap = false, cancellable = true)
    public void fixRoleGetter(CallbackInfoReturnable<IRole> cir) {
        if (this.npc.advanced.role == RoleTypeHelper.mentor()) {
            // TODO UNCOMMENT THIS WHEN YOU ADD SCRIPTABLE ROLE CODE
            // cir.setReturnValue(new SCRIPTVARIANTHERE(this.npc));
        }
    }
}
