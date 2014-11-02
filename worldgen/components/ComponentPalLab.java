package com.extviljuraaddon.worldgen.components;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import to.uk.ilexiconn.jurassicraft.ModBlocks;
import to.uk.ilexiconn.jurassicraft.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;

import com.extvil.extendedvillages.evcore.ExtendedVillages;
import com.extvil.extendedvillages.evcore.handler.ConfigHandler;
import com.rafamv.bygoneage.registry.BygoneAgeBlocks;
import com.rafamv.fossilhunting.blocks.FHBlocks;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class ComponentPalLab extends StructureVillagePieces.Village{
	
	private int averageGroundLevel = -1;
	private boolean isHalloween;
    private StructureVillagePieces.Start startPiece;
	private boolean ChestPlaced;
	
    public static final String LabContent       = "labChest";
	
    
    public static final WeightedRandomChestContent[] LabChestContents = new WeightedRandomChestContent[] {
		new WeightedRandomChestContent(ModItems.gypsumPowder, 0, 1, 5, 10), 
		new WeightedRandomChestContent(ModItems.fossil, 0, 1, 3, 5), 
		new WeightedRandomChestContent(ModItems.fossil, 0, 1, 3, 5), 
		new WeightedRandomChestContent(ModItems.fossil, 0, 1, 3, 5), 
		new WeightedRandomChestContent(ModItems.fossil, 0, 1, 3, 5), 
		new WeightedRandomChestContent(ModItems.fossil, 0, 1, 3, 5), 
		new WeightedRandomChestContent(ModItems.dinoBone, 0, 1, 3, 15), 
		new WeightedRandomChestContent(ModItems.dinoBone, 0, 1, 3, 15), 
		new WeightedRandomChestContent(ModItems.dinoBone, 0, 1, 3, 15), 
		new WeightedRandomChestContent(ModItems.dinoBone, 0, 1, 3, 15), 
		new WeightedRandomChestContent(ModItems.dinoBone, 0, 1, 3, 15), 
		new WeightedRandomChestContent(ModItems.dinoSteak, 0, 1, 3, 15), 
		new WeightedRandomChestContent(ModItems.dinoPad, 0, 1, 1, 5), 
		new WeightedRandomChestContent(ModItems.dinoSteak, 0, 1, 1, 5), 
		new WeightedRandomChestContent(ModItems.amber, 0, 1, 1, 5), 
		new WeightedRandomChestContent(ModItems.growthSerum, 0, 1, 1, 5)};
	
	
    private static final HashMap<String, ChestGenHooks> chestInfo = new HashMap<String, ChestGenHooks>();

	

	public ComponentPalLab()
	{
		
	}
	
    public ComponentPalLab(Start villagePiece, int par2, Random par3Random, StructureBoundingBox sbb, int coordBaseMode) {
        super();
        this.coordBaseMode = coordBaseMode;
        this.boundingBox = sbb;

    }
    
	public static ComponentPalLab buildComponent(Start villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int p5) {
        StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 11, 6, 11, coordBaseMode);
        return canVillageGoDeeper(box) && StructureComponent.findIntersecting(pieces, box) == null ? new ComponentPalLab(villagePiece, p5, random, box, coordBaseMode) : null;
    }
	
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {
        if (this.averageGroundLevel < 0) 
        {
            this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

            if (this.averageGroundLevel < 0)
                return true;

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 5, 0);
        }
        int x = this.boundingBox.minX;
        int y = this.boundingBox.minY;
        int z = this.boundingBox.minZ;
        
        Block walls;
        Block roof;
        Block Deco;
        Block Ground;
        Block Path;
        Block Floor;
        
    	BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(x, z);
        Calendar calendar = Calendar.getInstance();

        if((calendar.get(2) + 1 == 10 && calendar.get(5) >= 28 && calendar.get(5) <= 31) || (calendar.get(2) + 1 == 11 && calendar.get(5) >= 1 && calendar.get(5) <= 2))
        {  
        	isHalloween = true;
    	}
        else
        {
        	isHalloween = false;
        }
    	if(biome == BiomeGenBase.desert)
    	{
    		walls = Blocks.sandstone;
    		roof = ExtendedVillages.SmoothSand;
    		Deco = Blocks.sandstone;
    		Ground = Blocks.sand;
    		Path = Blocks.sandstone;
    		Floor = Deco;
    	}
    	else if(isHalloween)
    	{
    		walls = Blocks.stained_hardened_clay;
    		roof = Blocks.netherrack;
    		Deco = Blocks.hardened_clay;
    		Ground = Blocks.soul_sand;
    		Floor = Blocks.nether_brick;
    		Path = Blocks.gravel;
    	}
    	else
    	{
    		walls = FHBlocks.blockFHGypsumCobblestoneBlock;
    		roof = Blocks.oak_stairs;
    		Deco = Blocks.planks;
    		Ground = Blocks.grass;
    		Floor = Deco;
    		Path = Blocks.gravel;
    	}
    	
        spawnVillagers(world, sbb, 4, 1, 2, 2);

        this.fillWithBlocks(world, sbb, 1, 1, 1, 7, 4, 4, Blocks.air, Blocks.air, false);
        this.fillWithBlocks(world, sbb, 2, 1, 6, 8, 4, 10, Blocks.air, Blocks.air, false);
        this.fillWithBlocks(world, sbb, 2, 0, 5, 8, 0, 10, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 1, 0, 1, 7, 0, 4, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 0, 0, 0, 0, 3, 5, walls, walls, false);
        this.fillWithBlocks(world, sbb, 8, 0, 0, 8, 3, 10, walls, walls, false);
        this.fillWithBlocks(world, sbb, 1, 0, 0, 7, 2, 0, walls, walls, false);
        this.fillWithBlocks(world, sbb, 1, 0, 5, 2, 1, 5, walls, walls, false);
        this.fillWithBlocks(world, sbb, 2, 0, 6, 2, 3, 10, walls, walls, false);
        this.fillWithBlocks(world, sbb, 3, 0, 10, 7, 3, 10, walls, walls, false);
        this.fillWithBlocks(world, sbb, 1, 2, 0, 7, 3, 0, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 1, 2, 5, 2, 3, 5, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 0, 4, 1, 8, 4, 1, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 0, 4, 4, 3, 4, 4, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 0, 5, 2, 8, 5, 3, Deco, Deco, false);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 0, 4, 2, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 0, 4, 3, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 8, 4, 2, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 8, 4, 3, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 8, 4, 4, sbb);
        int i = this.getMetadataWithOffset(roof, 3);
        int j = this.getMetadataWithOffset(roof, 2);
        int k;
        int l;

        for (k = -1; k <= 2; ++k)
        {
            for (l = 0; l <= 8; ++l)
            {
                this.placeBlockAtCurrentPosition(world, roof, i, l, 4 + k, k, sbb);

                if ((k > -1 || l <= 1) && (k > 0 || l <= 3) && (k > 1 || l <= 4 || l >= 6))
                {
                    this.placeBlockAtCurrentPosition(world, roof, j, l, 4 + k, 5 - k, sbb);
                }
            }
        }

        this.fillWithBlocks(world, sbb, 3, 4, 5, 3, 4, 10, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 7, 4, 2, 7, 4, 10, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 4, 5, 4, 4, 5, 10, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 6, 5, 4, 6, 5, 10, Deco, Deco, false);
        this.fillWithBlocks(world, sbb, 5, 6, 3, 5, 6, 10, Deco, Deco, false);
        k = this.getMetadataWithOffset(roof, 0);
        int i1;

        for (l = 4; l >= 1; --l)
        {
            this.placeBlockAtCurrentPosition(world, Deco, 0, l, 2 + l, 7 - l, sbb);

            for (i1 = 8 - l; i1 <= 10; ++i1)
            {
                this.placeBlockAtCurrentPosition(world, roof, k, l, 2 + l, i1, sbb);
            }
        }

        l = this.getMetadataWithOffset(roof, 1);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 6, 6, 3, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 7, 5, 4, sbb);
        this.placeBlockAtCurrentPosition(world, roof, l, 6, 6, 4, sbb);
        int j1;

        for (i1 = 6; i1 <= 8; ++i1)
        {
            for (j1 = 5; j1 <= 10; ++j1)
            {
                this.placeBlockAtCurrentPosition(world, roof, l, i1, 12 - i1, j1, sbb);
            }
        }

        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 0, 2, 1, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 0, 2, 4, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 0, 2, 2, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 0, 2, 3, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 4, 2, 0, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 2, 0, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, 2, 0, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 8, 2, 1, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 8, 2, 2, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 8, 2, 3, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 8, 2, 4, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 8, 2, 5, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 8, 2, 6, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 8, 2, 7, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 8, 2, 8, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 8, 2, 9, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 2, 2, 6, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 2, 7, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 2, 8, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 2, 2, 9, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 4, 4, 10, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 4, 10, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, 4, 10, sbb);
        this.placeBlockAtCurrentPosition(world, Deco, 0, 5, 5, 10, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.air, 0, 2, 1, 0, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.air, 0, 2, 2, 0, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 2, 3, 1, sbb);
        this.placeDoorAtCurrentPosition(world, sbb, random, 2, 1, 0, this.getMetadataWithOffset(Blocks.wooden_door, 1));
        this.fillWithBlocks(world, sbb, 1, 0, -1, 3, 2, -1, Blocks.air, Blocks.air, false);

        placeChest(world, 1, 1, 3, 3, random, sbb);

        this.placeBlockAtCurrentPosition(world, ModBlocks.dnaCombinator, 0, 5, 1, 1, sbb);
        this.placeBlockAtCurrentPosition(world, ModBlocks.cultivateBottomOff, 0, 6, 1, 1, sbb);
        this.placeBlockAtCurrentPosition(world, ModBlocks.cultivateTopOff, 0, 6, 2, 1, sbb);

        this.placeBlockAtCurrentPosition(world, Blocks.crafting_table, 0, 3, 1, 5, sbb);
        this.placeBlockAtCurrentPosition(world, FHBlocks.blockFHCleaningTableBlock, 0, 3, 1, 6, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.cauldron, 0, 3, 1, 7, sbb);
        this.placeBlockAtCurrentPosition(world, FHBlocks.blockFHPaleontologyTableBlock, 0, 3, 1, 8, sbb);
        this.placeBlockAtCurrentPosition(world, FHBlocks.blockFHBarrelBlock, 0, 3, 1, 9, sbb);

        this.placeBlockAtCurrentPosition(world, ModBlocks.dinoPad, 0, 7, 2, 4, sbb);
        this.placeBlockAtCurrentPosition(world, ModBlocks.dnaExtractor, 0, 7, 2, 7, sbb);
        this.placeBlockAtCurrentPosition(world, ModBlocks.dnaCombinator, 0, 7, 2, 8, sbb);

        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 7, 1, 4, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 7, 1, 7, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 7, 1, 8, sbb);

        if (this.getBlockAtCurrentPosition(world, 2, 0, -1, sbb).getMaterial() == Material.air && this.getBlockAtCurrentPosition(world, 2, -1, -1, sbb).getMaterial() != Material.air)
        {
            this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, this.getMetadataWithOffset(Blocks.stone_stairs, 3), 2, 0, -1, sbb);
        }

        for (i1 = 0; i1 < 5; ++i1)
        {
            for (j1 = 0; j1 < 9; ++j1)
            {
                this.clearCurrentPositionBlocksUpwards(world, j1, 7, i1, sbb);
                this.func_151554_b(world, walls, 0, j1, -1, i1, sbb);
            }
        }

        for (i1 = 5; i1 < 11; ++i1)
        {
            for (j1 = 2; j1 < 9; ++j1)
            {
                this.clearCurrentPositionBlocksUpwards(world, j1, 7, i1, sbb);
                this.func_151554_b(world, walls, 0, j1, -1, i1, sbb);
            }
        }
        
        return true;    	
	}
    protected int func_151557_c(Block p_151557_1_, int p_151557_2_)
    {
        BiomeEvent.GetVillageBlockMeta event = new BiomeEvent.GetVillageBlockMeta(startPiece == null ? null : startPiece.biome, p_151557_1_, p_151557_2_);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Result.DENY) return event.replacement;
        if (this.isHalloween)
        {
            if (p_151557_1_ == Blocks.stained_hardened_clay)
            {
                return 1;
            }
        }

        return p_151557_2_;
    }
    @Override
    protected int getVillagerType(int par1) {
        return ConfigHandler.TannerID;
    }
    
    @Override
    protected void func_143012_a(NBTTagCompound nbt) {
        super.func_143012_a(nbt);
        nbt.setBoolean("Chest", ChestPlaced);
    }

    @Override
    protected void func_143011_b(NBTTagCompound nbt) {
        super.func_143011_b(nbt);
        ChestPlaced = nbt.getBoolean("Chest");
    }
    private static void addInfo(String category, WeightedRandomChestContent[] items, int min, int max)
    {
        chestInfo.put(category, new ChestGenHooks(category, items, min, max));
    }
    private void placeChest(World world, int x, int y, int z, int meta, Random rand, StructureBoundingBox sbb) {
        

        addInfo(LabContent, this.LabChestContents,   3,  7);

    	
    	int xx = this.getXWithOffset(x, z);
        int yy = this.getYWithOffset(y);
        int zz = this.getZWithOffset(x, z);

        if (!ChestPlaced && sbb.isVecInside(xx, yy, zz)) {
        	ChestPlaced = true;
            if (world.getBlock(xx, yy, zz) != Blocks.chest) {
                world.setBlock(xx, yy, zz, Blocks.chest, getMetadataWithOffset(Blocks.chest, meta), 2);
                TileEntityChest chest = (TileEntityChest) world.getTileEntity(xx, yy, zz);

                if (chest != null)
                    WeightedRandomChestContent.generateChestContents(rand, LabChestContents, chest, 10);
            }
        }
    }
}