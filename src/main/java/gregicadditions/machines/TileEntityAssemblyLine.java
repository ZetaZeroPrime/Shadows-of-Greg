package gregicadditions.machines;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.recipes.GARecipeMaps;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class TileEntityAssemblyLine extends RecipeMapMultiblockController {
    public TileEntityAssemblyLine(String metaTileEntityId) {
        super(metaTileEntityId, GARecipeMaps.ASSEMBLY_LINE_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new TileEntityAssemblyLine(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start(BACK, DOWN, RIGHT)
                .aisle("#Y#", "GSG", "RTR", "FIF")
                .aisle("#Y#", "GAG", "RTR", "FIF").setRepeatable(3, 14)
                .aisle("#Y#", "GAG", "RTR", "COC")
                .where('S', selfPredicate())
                .where('C', statePredicate(getCasingState()))
                .where('F', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS)))
                .where('O', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.EXPORT_ITEMS)))
                .where('Y', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.INPUT_ENERGY)))
                .where('I', tilePredicate((state, tile) -> {
                    return tile.metaTileEntityId.equals(MetaTileEntities.ITEM_IMPORT_BUS[0].metaTileEntityId);
                }))
                .where('G', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING)))
                .where('A', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ASSEMBLER_CASING)))
                .where('R', statePredicate(GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.REINFORCED_GLASS)))
                .where('T', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.TUNGSTENSTEEL_GEARBOX_CASING)))
                .where('#', blockPredicate(Blocks.AIR))
                .build();
        /*return FactoryBlockPattern.start(RIGHT, UP, FRONT)
                .aisle("YXX", "XXX")
                .aisle("YXX", "XXX")
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.INPUT_ENERGY, MultiblockAbility.EXPORT_ITEMS)))
                //.where('#', blockPredicate(Blocks.AIR))
                .where('Y', selfPredicate())
                .build();*/
    }

    @Override
    public ICubeRenderer getBaseTexture() {
        return Textures.SOLID_STEEL_CASING;
    }

    protected IBlockState getCasingState() {
        return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID);
    }
}

