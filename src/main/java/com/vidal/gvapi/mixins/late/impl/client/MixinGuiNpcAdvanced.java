package com.vidal.gvapi.mixins.late.impl.client;

import com.vidal.gvapi.mixins.RoleTypeHelper;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.mainmenu.GuiNpcAdvanced;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiNpcAdvanced.class, remap = false)
public abstract class MixinGuiNpcAdvanced extends GuiNPCInterface2 implements IGuiData {
    public MixinGuiNpcAdvanced(EntityNPCInterface npc) {
        super(npc);
    }

    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Lnoppes/npcs/client/gui/mainmenu/GuiNpcAdvanced;addButton(Lnoppes/npcs/client/gui/util/GuiNpcButton;)V", ordinal = 1, remap = false), remap = true)
    private void redirectButtonCreation(GuiNpcAdvanced instance, GuiNpcButton guiNpcButton) {
        instance.addButton(new GuiNpcButton(8, this.guiLeft + 85, guiNpcButton.yPosition, 155, 20, new String[]{"role.none", "role.trader", "role.follower", "role.bank", "role.transporter", "role.mailman", NoppesStringUtils.translate(new Object[]{"role.companion", "(WIP)"}), "role.mentor"}, this.npc.advanced.role.ordinal()));
    }

    @Inject(method = "setGuiData", remap = false, at = @At("TAIL"))
    private void injectGuiDataHandling(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey("RoleData")) {
            if (this.npc.advanced.role == RoleTypeHelper.mentor()) {
                // TODO OPEN MENTOR SCREEN HERE!!!!
            }
        }
    }

}
