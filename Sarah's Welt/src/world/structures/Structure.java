package world.structures;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import resources.Shader;
import resources.Texture;
import util.Animator;
import world.Node;
import world.Thing;
import world.World;
import core.Settings;
import core.geom.Vec;

public abstract class Structure extends Thing{

	public static void renderStructures(boolean front){
		for(ArrayList<Structure> list : World.structures){
			if(list.size() > 0){
				list.get(0).animator.tex.bind();
				list.forEach((c) -> {if(front == c.front){c.render();}});
				
				if(Settings.shader && list.get(0) instanceof Flower){
					GL11.glPushMatrix();
					GL11.glLoadIdentity();
					World.light.bind();
						Shader.Test.bind();
							list.forEach((c) -> {
								GL11.glPushMatrix();
								if(front == c.front){
									((Flower)c).renderLight();
								}
								GL11.glPopMatrix();
							});
						Shader.Test.release();
					World.light.release();
					GL11.glPopMatrix();
					GL11.glColor4f(1, 1, 1, 1);
				}
			}
		}
		Texture.bindNone();
		if(Settings.hitbox){
			for(ArrayList<Structure> list : World.structures){
				list.forEach((c) -> {
					GL11.glPushMatrix();
					GL11.glTranslatef(c.pos.x, c.pos.y, 0);
					c.animator.tex.box.outline();
					GL11.glPopMatrix();
				});
			}
		}
	}

	public Structure(Animator ani, Vec pos, Node worldLink){
		super(ani, pos, worldLink);
	}
}
