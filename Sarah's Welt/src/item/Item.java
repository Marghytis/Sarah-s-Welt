package item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Texture;
import world.World;
import core.Window;
import core.geom.Quad;

public class Item {

	public static List<Item> list = new ArrayList<>();
	
	public static final Weapon sword = new Weapon(Res.ITEMS_WORLD.texs[0][0], Res.ITEMS_WEAPONS.texs[0][0], Res.ITEMS_INV.texs[0][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -21, 80, 40), 180, "Sword", 4);
	public static final Weapon axe = new Weapon(Res.ITEMS_WORLD.texs[1][0], Res.ITEMS_WEAPONS.texs[0][1], Res.ITEMS_INV.texs[1][0],
			new Quad(-25, -2, 50, 50), new Quad(-55, -19, 80, 40), 180, "Axe", 10);
	
	
	public Texture texWorld;
	public Texture texHand;
	public Texture texInv;
	public Quad boxWorld;
	public Quad boxHand;
	
	public int defaultRotationHand;
	
	public String name;
	public int id;

	public Item(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand,
			String name) {
		this.texWorld = texWorld;
		this.texHand = texHand;
		this.texInv = texinv;
		this.boxWorld = boxWorld;
		this.boxHand = boxHand;
		this.defaultRotationHand = defaultRotationHand;
		this.name = name;
		
		list.add(this);
		this.id = list.indexOf(this);
	}
	
	public void renderWorld(){
		boxWorld.drawTex(texWorld);
	}
	
	public void renderHand(){
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
