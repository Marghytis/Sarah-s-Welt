package item;



public class Inventory {

	public ItemStack[] stacks;
	public int coins;
	
	public int selectedItem;
		
	public Inventory(int itemAmount, Item... items){
		stacks = new ItemStack[itemAmount];
		for(int i = 0; i < stacks.length; i++){
			stacks[i] = new ItemStack(i, this);
			if(i < items.length){
				stacks[i].item = items[i];
			} else {
				stacks[i].item = Item.fist;
			}
			coins = 0;
		}
		selectedItem = 0;
	}
	
	public Item getSelectedItem(){
		return stacks[selectedItem].item;
	}
	
	public void update(float delta){
		for(ItemStack stack : stacks){
			if(stack.item != null && stack.item.coolDown > 0) stack.item.coolDown -= delta;
		}
	}
	
	public boolean addItem(Item item){
		for(int i = 0; i < stacks.length; i++){
			if(stacks[i].item == item){
				return false;
			} else if (stacks[i].item == Item.fist){
				stacks[i].item = item;
				return true;
			}
		}
		return false;
	}
}
