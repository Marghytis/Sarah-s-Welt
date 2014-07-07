package item;

import core.Window;
import resources.Res;


public class Inventory {

	public static ItemStack[] stacks = new ItemStack[6];
	public static int coins;
	
	public static int selectedItem;
	
	public static Item getSelectedItem(){
		return stacks[selectedItem].item;
	}
	
	public static void update(float delta){
		for(ItemStack stack : stacks){
			if(stack.item != null && stack.item.coolDown > 0) stack.item.coolDown -= delta;
		}
	}
	
	public static void reset(){
		for(int i = 0; i < Inventory.stacks.length; i++){
			Inventory.stacks[i] = new ItemStack(i);
			Inventory.stacks[i].item = Item.fist;
			coins = 0;
		}
	}
	
	public static void render(){
		for(ItemStack stack : Inventory.stacks){
			stack.render();
		}
//		float xText = x + (size.x/2) - (Res.font.getWidth(text)/3);
//		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
		Res.font.drawString(Window.WIDTH/2, Window.HEIGHT/2, "Coins: " + coins, 1, 1);
	}
	
	public static boolean mouseClickedAt(float x, float y){
		for(ItemStack stack : Inventory.stacks){
			if(stack.contains(x, y)){
				selectedItem = stack.slot;
				return true;
			}
		}
		return false;
	}
	
	public static boolean addItem(Item item){
		for(int i = 0; i < Inventory.stacks.length; i++){
			if(Inventory.stacks[i].item == item){
				return false;
			} else if (Inventory.stacks[i].item == Item.fist){
				Inventory.stacks[i].item = item;
				return true;
			}
		}
		return false;
	}
}
