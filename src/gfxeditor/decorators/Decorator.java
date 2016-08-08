package gfxeditor.decorators;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.Color;
import java.awt.Point;

// Extension 2
public abstract class Decorator extends Shape
{
	private Shape content;
	
	public Shape getContent () { return content; }
	
	/**
	 * Returns the internal number of this shape.
	 * @return The internal number
	 * @since 2.0
	 */
	public int getNumber () { return content.getNumber(); }

	public Decorator (Shape content)
	{
		super(content.getX(), content.getY(), content.getColor());
		this.content = content;
	}
	
	public boolean contains (Point p)
	{
		return content.contains(p);
	}

	@Override
	public Color getBorderColor ()
	{
		return content.getBorderColor();
	}

	@Override
	public int getBorderWidth ()
	{
		return content.getBorderWidth();
	}

	@Override
	public Color getColor ()
	{
		return content.getColor();
	}

	@Override
	public int getX ()
	{
		return content.getX();
	}

	@Override
	public int getY ()
	{
		return content.getY();
	}

	@Override
	public void setBorderColor (Color borderColor)
	{
		content.setBorderColor(borderColor);
	}

	@Override
	public void setBorderWidth (int borderWidth)
	{
		content.setBorderWidth(borderWidth);
	}

	@Override
	public void setColor (Color color)
	{
		content.setColor(color);
	}

	@Override
	public void setPosition (Point p)
	{
		content.setPosition(p);
	}

	@Override
	public void setX (int x)
	{
		content.setX(x);
	}

	@Override
	public void setY (int y)
	{
		content.setY(y);
	}

	public int getHeight ()
	{
		return content.getHeight();
	}

	public int getWidth ()
	{
		return content.getWidth();
	}

	public boolean isInvisible ()
	{
		return content.isInvisible();
	}

	public void setSize (int width, int height)
	{
		if (content == null) return;
		content.setSize(width, height);
	}

	public void setToDefaults ()
	{
		content.setToDefaults();
	}
	
	public void accept (Visitor visitor)
	{
		content.accept(visitor);
	}
	
	@Override
	public String toString ()
	{
		return content.toString();
	}
}
