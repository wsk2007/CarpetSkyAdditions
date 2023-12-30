package com.jsorrell.carpetskyadditions.advancements.criterion;

import java.util.Optional;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.storage.loot.LootContext;

public class AllayVexTrigger extends SimpleCriterionTrigger<AllayVexTrigger.Conditions> {
    static final ResourceLocation ID = new SkyAdditionsResourceLocation("allay_vex");

    public void trigger(ServerPlayer player, Vex vex, Allay allay) {
        LootContext vexLootContext = EntityPredicate.createContext(player, vex);
        LootContext allayLootContext = EntityPredicate.createContext(player, allay);
        trigger(player, conditions -> conditions.matches(vexLootContext, allayLootContext));
    }

    @Override
    public Codec<AllayVexTrigger.Conditions> codec() {
        return AllayVexTrigger.Conditions.CODEC;
    }

    public static record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> vex,
            Optional<ContextAwarePredicate> allay) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<AllayVexTrigger.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                                .forGetter(AllayVexTrigger.Conditions::player),
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "vex")
                                .forGetter(AllayVexTrigger.Conditions::vex),
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "allay")
                                .forGetter(AllayVexTrigger.Conditions::allay))
                        .apply(instance, AllayVexTrigger.Conditions::new));

        public boolean matches(LootContext vexContext, LootContext allayContext) {
            return vex.get().matches(vexContext) && allay.get().matches(allayContext);
        }
    }
}
