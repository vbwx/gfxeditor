package gfxeditor.visitors;

import gfxeditor.shapes.Circle;
import gfxeditor.shapes.ImageAdapter;
import gfxeditor.shapes.Rectangle;
import gfxeditor.shapes.Triangle;

// Extension 3
public abstract class Visitor
{
	private final int speed;
	
	public int getSpeed () { return speed; }
	
	public Visitor (int speed)
	{
		this.speed = speed;
	}
	
	public abstract void visitTriangle (Triangle t);
	
	public abstract void visitRectangle (Rectangle r);
	
	public abstract void visitImage (ImageAdapter a);
	
	public abstract void visitCircle (Circle c);
}
