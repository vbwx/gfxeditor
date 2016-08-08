package gfxeditor.visitors;

import gfxeditor.shapes.Circle;
import gfxeditor.shapes.ImageAdapter;
import gfxeditor.shapes.Rectangle;
import gfxeditor.shapes.Triangle;

// Extension 3
public class BackwardVisitor extends Visitor
{
	public BackwardVisitor (int speed)
	{
		super(speed);
	}

	public void visitCircle (Circle c)
	{
		c.setColor(c.getColor().brighter());
	}

	public void visitImage (ImageAdapter a)
	{
		a.setX(a.getX() - getSpeed());
	}

	public void visitRectangle (Rectangle r)
	{
		r.setSize(r.getWidth() - getSpeed(), r.getHeight() - getSpeed());
	}

	public void visitTriangle (Triangle t)
	{
		t.setY(t.getY() - getSpeed());
	}
}
