package com.vidal.gvapi.mixins;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class LateMixinLoader implements ILateMixinLoader {

    public static final MixinEnvironment.Side side = MixinEnvironment.getCurrentEnvironment().getSide();
    @Override
    public String getMixinConfig() {
        return "mixins.gvapi.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if (side == MixinEnvironment.Side.CLIENT) {
            mixins.add("client.MixinGuiNpcAdvanced");
        }

        mixins.add("common.MixinDataAdvanced");
        mixins.add("common.MixinEntityNpcInterface");
        mixins.add("common.MixinScriptNpc");
        mixins.add("common.MixinEnumRoleType");

        return mixins;
    }
}
