package item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Texture;
import util.Animation;
import world.World;
import world.creatures.Sarah;
import core.Window;
import core.geom.Quad;

public class Item {

	public static List<Item> list = new ArrayList<>();

	public static final Weapon fist = new Weapon(null, null, null, null, null, 180, "Fist", 300, 2, Sarah.punch);
	public static final Sword sword = new Sword(Res.ITEMS_WORLD.texs[0][0], Res.ITEMS_WEAPONS.texs[0][0], Res.ITEMS_INV.texs[0][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -21, 80, 40), 180, "Sword", 500, 4);
	public static final Sword axe = new Sword(Res.ITEMS_WORLD.texs[1][0], Res.ITEMS_WEAPONS.texs[0][1], Res.ITEMS_INV.texs[1][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -19, 80, 40), 180, "Axe", 1000, 10);
	public static final Sword stick = new Sword(Res.ITEMS_WORLD.texs[2][0], Res.ITEMS_WEAPONS.texs[0][2], Res.ITEMS_INV.texs[3][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -21, 80, 40), 180, "Stick", 500, 3);
	public static final Sword candyCane = new Sword(Res.ITEMS_WORLD.texs[3][0], Res.ITEMS_WEAPONS.texs[0][3], Res.ITEMS_INV.texs[5][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -19, 80, 40), 180, "Candy cane", 1000, 3);
	public static final Sword shovel = new Sword(Res.ITEMS_WORLD.texs[4][0], Res.ITEMS_WEAPONS.texs[0][4], Res.ITEMS_INV.texs[4][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -19, 80, 40), 180, "Shovel", 700, 4);
	public static final MagicWeapon horn = new MagicWeapon(Res.ITEMS_WORLD.texs[4][0], Res.ITEMS_WEAPONS.texs[0][4], Res.ITEMS_INV.texs[5][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -19, 80, 40), 180, "Horn", 1000, 3, 2);
	
	public Texture texWorld;
	public Texture texHand;
	public Texture texInv;
	public Quad boxWorld;
	public Quad boxHand;
	
	public int defaultRotationHand;
	
	public String name;
	public int id;
	int coolDownStart;
	int coolDown;
	public Animation animation;

	public Item(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand,
			String name, int coolDownStart, Animation animation) {
		this.texWorld = texWorld;
		this.texHand = texHand;
		this.texInv = texinv;
		this.boxWorld = boxWorld;
		this.boxHand = boxHand;
		this.defaultRotationHand = defaultRotationHand;
		this.name = name;
		this.coolDownStart = coolDownStart;
		this.animation = animation;
		
		list.add(this);
		this.id = list.indexOf(this);
	}
	
	public boolean use(float x, float y){
		if(coolDown <= 0){
			World.sarah.useItem(this);
			coolDown = coolDownStart;
			return true;
		}
		return false;
	}
	
	public void renderWorld(){
		if(texWorld == null) return;
		boxWorld.drawTex(texWorld);
	}
	
	public void renderHand(){
		if(texHand == null) return;
		int[] handPos = World.sarah.getHandPosition();
		int[] sarahHandPos = new int[]{handPos[0], handPos[1], handPos[2]};
		if(World.sarah.mirrored){
			sarahHandPos[0] = (int) (World.sarah.animator.tex.box.size.x - sarahHandPos[0]);
			sarahHandPos[2] = 180 - sarahHandPos[2];
		}
		
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslatef((Window.WIDTH/2) + World.sarah.animator.tex.box.x + sarahHandPos[0], (Window.HEIGHT/2) + World.sarah.animator.tex.box.y + sarahHandPos[1], 0);
		GL11.glRotatef(defaultRotationHand + sarahHandPos[2], 0, 0, 1);
		
		texHand.file.bind();
		if(!World.sarah.mirrored){
			boxHand.drawTex(texHand);
		} else {
			boxHand.drawTexFlipped(texHand);
		}
	
		GL11.glPopMatrix();
	}
	
}
