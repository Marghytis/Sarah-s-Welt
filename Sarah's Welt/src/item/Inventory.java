package item;

import org.lwjgl.opengl.GL11;

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
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslatef(Window.WIDTH*7.0f/8, Window.HEIGHT*7.0f/8, 0);
		Res.MONEYBAG.file.bind();
		Res.MONEYBAG.box.drawTex(Res.MONEYBAG.texs[0][0]);
		float xText = - 50 - (Res.font.getWidth(coins + "")/3);
		float yText = - (Res.font.getHeight()/2);
		GL11.glColor3f(0.9f, 0.8f, 0.1f);
		Res.font.drawString(xText, yText, coins + "", 1, 1);
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
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
