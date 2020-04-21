package com.dreammaster.bartworksHandler;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.items.SimpleSubItemClass;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.objects.GT_Fluid;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.dreammaster.bartworksHandler.BacteriaRegistry.CultureSet;
import static com.github.bartimaeusnek.bartworks.API.BioRecipeAdder.addBacterialVatRecipe;
import static gregtech.api.enums.GT_Values.*;

public class BioItemLoader {
    private static final Item BIOTEMS;
    private static final GT_Fluid[] BIOFLUIDS;
    private static ItemStack[] BIOTEMSSTACKS;
    static {

        BIOTEMS = new SimpleSubItemClass(new String[]{"itemCollagen","itemGelatin","itemAgar"}).setCreativeTab(MainMod.BIO_TAB);
        GameRegistry.registerItem(BIOTEMS,"GTNHBioItems","bartworks");
        BIOTEMSSTACKS = new ItemStack[]{
                new ItemStack(BIOTEMS,1,0),
                new ItemStack(BIOTEMS,1,1),
                new ItemStack(BIOTEMS,1,2),

        };
        BIOFLUIDS = new GT_Fluid[]{
                new GT_Fluid("GelatinMixture", "molten.autogenerated", new short[]{255, 255, 125}),
                new GT_Fluid("MeatExtract", "molten.autogenerated", new short[]{160, 70, 50}),
                new GT_Fluid("UnknownNutrientAgar", "molten.autogenerated", new short[]{175, 133, 0}),
                new GT_Fluid("SeaweedBroth", "molten.autogenerated", new short[]{60, 200, 0})
        };

        for (GT_Fluid gtFluid : BIOFLUIDS){
                FluidRegistry.registerFluid(gtFluid);
        }

        RA.addChemicalRecipe(
                Materials.MeatRaw.getDust(2),new ItemStack(Items.bone,2),Materials.DilutedSulfuricAcid.getFluid(500),
                Materials.Water.getFluid(500),BIOTEMSSTACKS[0],
             800,480
        );
        RA.addChemicalRecipe(
                Materials.MeatRaw.getDust(2),Materials.Bone.getDust(4),Materials.DilutedSulfuricAcid.getFluid(500),
                Materials.Water.getFluid(500),BIOTEMSSTACKS[0],
                800,480
        );

        RA.addChemicalRecipe(
                BIOTEMSSTACKS[0],Materials.Water.getCells(1),Materials.PhosphoricAcid.getFluid(1000),
                new FluidStack(BIOFLUIDS[0],2000),Materials.Empty.getCells(1),
                400,480
        );
        RA.addChemicalRecipe(
                BIOTEMSSTACKS[0],Materials.PhosphoricAcid.getCells(1),Materials.Water.getFluid(1000),
                new FluidStack(BIOFLUIDS[0],2000),Materials.Empty.getCells(1),
                400,480
        );

        RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),NI,new FluidStack(BIOFLUIDS[0],4000),
                NF,Materials.Phosphorus.getDust(1),BIOTEMSSTACKS[1],NI,NI,NI,NI,
                null,600,480
        );

        RA.addMixerRecipe(
                GT_Utility.getIntegratedCircuit(11),BIOTEMSSTACKS[1],NI,NI,NI,NI, GT_ModHandler.getDistilledWater(1000),
                NF,BIOTEMSSTACKS[2],600,480
        );

        RA.addFluidExtractionRecipe(
                Materials.MeatRaw.getDust(1),
                NI,new FluidStack(BIOFLUIDS[1],125),
                0,300,120
        );

        RA.addMultiblockChemicalRecipe(
                new ItemStack[]{
                        BIOTEMSSTACKS[2].copy().splitStack(8),
                        ItemList.Circuit_Chip_Stemcell.get(16),
                        Materials.Salt.getDust(64)
                },
                new FluidStack[]{
                        FluidRegistry.getFluidStack("unknowwater",4000),
                        Materials.PhthalicAcid.getFluid(3000),
                        new FluidStack(BIOFLUIDS[1],1000)
                },
                new FluidStack[]{
                        new FluidStack(BIOFLUIDS[2],8000)
                },
                null,
                1600, BW_Util.getMachineVoltageFromTier(8)
        );
        addBacterialVatRecipe(
                new ItemStack[]{
                        ItemList.IC2_Energium_Dust.get(8),
                        Materials.Mytryl.getDust(1),
                        GT_ModHandler.getModItem("harvestcraft","seaweedItem",64)
                },
                CultureSet.get("TcetiEBac"),
                new FluidStack[]{new FluidStack(BIOFLUIDS[2],1600)},
                new FluidStack[]{new FluidStack(BIOFLUIDS[3],50)},
                1200,BW_Util.getMachineVoltageFromTier(8),100,8,0,false
        );
        for (int i = 0; i < OreDictionary.getOres("cropTcetiESeaweed").size(); i++) {
            RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(i),NI,new FluidStack(BIOFLUIDS[3],1000),NF,OreDictionary.getOres("cropTcetiESeaweed").get(i).copy().splitStack(64),NI,NI,NI,NI,NI,null,40,BW_Util.getMachineVoltageFromTier(8));
        }
    }

}