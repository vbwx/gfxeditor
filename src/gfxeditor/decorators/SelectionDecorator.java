package gfxeditor.decorators;

import gfxeditor.Shape;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

// Extension 2
public class SelectionDecorator extends Decorator
{
	public SelectionDecorator (Shape content)
	{
		super(content);
	}

	public Object clone ()
	{
		return new SelectionDecorator((Shape)getContent().clone());
	}

	public void paint (Graphics2D g)
	{
		Shape s = getContent();
		s.paint(g);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
		            5F, new float[] {5F}, 0F));
		g.drawRect((getWidth()  < 0 ? getX() + getWidth()  : getX()) - 1,
		           (getHeight() < 0 ? getY() + getHeight() : getY()) - 1,
		           Math.abs(getWidth()) + 1,
		           Math.abs(getHeight()) + 1);
	}
}
