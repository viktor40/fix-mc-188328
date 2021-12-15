# Fix MC-188328
## About
This is a simple mod fixing the bug [MC-188328](https://bugs.mojang.com/browse/MC-188328).
This bug relates to boat pushing mechanics. This mod was primarily made as a POC on how to fix this bug, and as a way to confirm that my proposed fix to this bug would actually work.

## The bug and it's fix
In this section we use fabric mappings!

The bug stems from a supposed typo in the `net.minecraft.entity.vehicle.boatEntity` class. When the hitboxes of a boat and an entity collide the `pushAwayFrom` method is called:
```Java
@Override
public void pushAwayFrom(Entity entity) {
    if (entity instanceof BoatEntity) {
        if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
            super.pushAwayFrom(entity);
        }
    } else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
        super.pushAwayFrom(entity);
    }
}
```

On line 7 we see the following:
```Java
else if (arg.getBoundingBox().minY <= this.getBoundingBox().minY)
```

This part is only called when the entity it is trying to push away is not a boat. (e.g. animals, monsters etc). We can see that we are comparing `entity.getBoundingBox().minY` to `this.getBoundingBox().minY`. 
Or in other words, we're comparing if the bottom of the entity hitbox is lower or on the same height as the bottom of the boat hitbox.
This results in the bug that when the entity's bottom of the hitbox is higher than the boats hitbox, the boat won't push the entity even though they're clearly touching and it should push.

This can be easily fixed by instead of checking if the bottom of the boat hitbox is lower, we check if the top hitbox is lower. We do this by mixing into the method and adding that extra clause if all previous clauses fail, so we're not calling the `super.pushAwayFrom(entity)` method twice.

In other words, this code gets added to the method:
```Java
if (!(entity instanceof BoatEntity)) {
            if (!(entity.getBoundingBox().minY <= this.getBoundingBox().minY)) {
                if (entity.getBoundingBox().minY <= this.getBoundingBox().maxY) {
                    super.pushAwayFrom(entity);
                }
            }
        }
```

Making it so the method becomes functionally the same as:
```Java
@Override
public void pushAwayFrom(Entity entity) {
    if (entity instanceof BoatEntity) {
        if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
            super.pushAwayFrom(entity);
        }
    } else if (entity.getBoundingBox().minY <= this.getBoundingBox().maxY) {
        super.pushAwayFrom(entity);
    }
}
```