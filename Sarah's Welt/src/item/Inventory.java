package item;

import resources.Res;

public class Inventory {

	public static ItemStack[] stacks = new ItemStack[6];
	
	public static void reset(){
		for(int i = 0; i < Inventory.stacks.length; i++){
			Inventory.stacks[i] = new ItemStack(i);
		}
	}
	
	public static void render(){
		Res.INVENTORY.file.bind();
		for(ItemStack stack : Inventory.stacks){
			stack.render();
		}
		Res.ITEMS_INV.file.bind();
		for(ItemStack stack : Inventory.stacks){
			if(stack.item != null){
				stack.item.renderInv();
			}
		}
	}
}
