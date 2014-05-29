package item;

import resources.Res;

public class Inventory {

	public static ItemStack[] stacks = new ItemStack[6];
	
	public static int selectedItem;
	
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
	}
	
	public static void mouseClickedAt(float x, float y){
		for(ItemStack stack : Inventory.stacks){
			if(stack.contains(x, y)){
				selectedItem = stack.slot;
			}
		}
	}
	
	public static boolean addItem(Item item){
		for(int i = 0; i < Inventory.stacks.length; i++){
			if(Inventory.stacks[i].item == item){
				return false;
			} else if (Inventory.stacks[i].item == null){
				Inventory.stacks[i].item = item;
				return true;
			}
		}
		return false;
	}
}
