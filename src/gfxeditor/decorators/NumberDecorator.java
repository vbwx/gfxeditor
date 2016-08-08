package gfxeditor.decorators;

import gfxeditor.Shape;
import java.awt.Color;
import java.awt.Graphics2D;

// Extension 2
public class NumberDecorator extends Decorator
{
	public NumberDecorator (Shape content)
	{
		super(content);
	}

	public Object clone ()
	{
		return new NumberDecorator((Shape)getContent().clone());
	}

	public void paint (Graphics2D g)
	{
		Shape s = getContent();
		s.paint(g);
		g.setPaint(Color.WHITE);
		g.drawString(String.valueOf(s.getNumber()),
		             s.getX() + s.getWidth()/2 - 4*(1+s.getNumber()/10),
		             s.getY() + s.getHeight()/2 + 5);
	}
}
