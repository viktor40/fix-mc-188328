package fixmc188328.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "pushAwayFrom", at = @At("TAIL"))
    public void fixBoatBug(Entity entity, CallbackInfo ci) {
        if (!(entity instanceof BoatEntity)) {
            if (!(entity.getBoundingBox().minY <= this.getBoundingBox().minY)) {
                if (entity.getBoundingBox().minY <= this.getBoundingBox().maxY) {
                    super.pushAwayFrom(entity);
                }
            }
        }
    }
}
