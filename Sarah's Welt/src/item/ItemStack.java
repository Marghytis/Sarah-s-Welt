package item;

import resources.Res;
import world.World;
import core.Window;
import core.geom.Quad;


public class ItemStack extends Quad{

	public Item item = null;
	public int count = 0;
	public int slot;
	public Inventory inv;
	
	public ItemStack(int slot, Inventory inv){
		super((slot+1)*(Window.WIDTH/7) -50, Window.HEIGHT/5 -50, 100, 100);
		this.slot = slot;
		this.inv = inv;
	}
	
	public void render(){
		Res.INVENTORY.file.bind();
		drawTex(Res.INVENTORY.texs[0][slot == World.sarah.inventory.selectedItem ? 1 : 0]);
		
		Res.ITEMS_INV.file.bind();
		if(item != null && item.texInv != null){
			drawTex(item.texInv);
		}
	}
	
	public String toString(){
		if(item != null && item != Item.fist){
			return item.name + "\nValue: " + item.value;
		} else {
			return "Noooothing!!!";
		}
	}
}
