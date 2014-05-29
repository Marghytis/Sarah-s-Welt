package item;


public class Inventory {

	public static ItemStack[] stacks = new ItemStack[6];
	
	static int selectedItem;
	
	public static Item getSelectedItem(){
		return stacks[selectedItem].item;
	}
	
	public static void update(int delta){
		for(ItemStack stack : stacks){
			if(stack.item != null && stack.item.coolDown > 0) stack.item.coolDown -= delta;
		}
		stacks[4].item = Item.horn;
	}
	
	public static void reset(){
		for(int i = 0; i < Inventory.stacks.length; i++){
			Inventory.stacks[i] = new ItemStack(i);
			Inventory.stacks[i].item = Item.fist;
		}
	}
	
	public static void render(){
		for(ItemStack stack : Inventory.stacks){
			stack.render();
		}
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
