package com.extviljuraaddon.worldgen.structurehandlers;

import java.util.List;
import java.util.Random;

import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

import com.extviljuraaddon.worldgen.components.ComponentPalLab;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class PalLabHandler implements IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i) {
        return new PieceWeight(ComponentPalLab.class, 3, 1);
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentPalLab.class;
    }

    @Override
    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int p5) {
        return ComponentPalLab.buildComponent(startPiece, pieces, random, x, y, z, coordBaseMode, p5);
    }

}
