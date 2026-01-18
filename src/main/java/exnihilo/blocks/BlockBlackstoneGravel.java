package exnihilo.blocks;

import java.util.List;

import net.minecraft.block.BlockGravel;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.data.ModData;

public class BlockBlackstoneGravel extends BlockGravel {

    public BlockBlackstoneGravel() {
        setHardness(0.6F);
        setStepSound(soundTypeGravel);
    }

    @Override
    public String getUnlocalizedName() {
        return ModData.ID + ".gravel_blackstone";
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(ModData.TEXTURE_LOCATION + ":IconBlockCrushedBlackstone");
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List items) {
        items.add(new ItemStack(item, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int id, int meta) {
        return this.blockIcon;
    }
}
