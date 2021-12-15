package fixmc188328.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
            method = "pushAwayFrom",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/util/math/Box.minY:D",
                    ordinal = 2
            )
    )
    public double redirectBoxMinY(Box instance) {
        return this.getBoundingBox().maxY;
    }

}
