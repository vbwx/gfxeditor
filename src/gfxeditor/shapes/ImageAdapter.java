package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

// Extension 1
public class ImageAdapter extends Shape
{
	private Shape box;
	private Image image;

	public Image getImage () { return image; }

	public void setImage (Image img) { image = img; }

	public int getNumber () { return box.getNumber(); }

	public ImageAdapter (Shape box, Image img)
	{
		super(0, 0, null);
		this.box = box;
		setImage(img);
	}

	public Object clone ()
	{
		return new ImageAdapter((Shape)box.clone(), image);
	}

	public boolean contains (Point p)
	{
		return box.contains(p);
	}

	public int getHeight ()
	{
		return box.getHeight();
	}

	public int getWidth ()
	{
		return box.getWidth();
	}

	public boolean isInvisible ()
	{
		return box.isInvisible() || image == null;
	}

	public void paint (Graphics2D g)
	{
		box.paint(g);
		g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
	}

	public void setSize (int width, int height)
	{
		if (box == null) return;
		box.setSize(width, height);
	}

	public void setToDefaults ()
	{
		box.setToDefaults();
	}

	@Override
	public Color getBorderColor ()
	{
		return box.getBorderColor();
	}

	@Override
	public int getBorderWidth ()
	{
		return box.getBorderWidth();
	}

	@Override
	public Color getColor ()
	{
		return box.getColor();
	}

	@Override
	public int getX ()
	{
		return box.getX();
	}

	@Override
	public int getY ()
	{
		return box.getY();
	}

	@Override
	public void setBorderColor (Color borderColor)
	{
		box.setBorderColor(borderColor);
	}

	@Override
	public void setBorderWidth (int borderWidth)
	{
		box.setBorderWidth(borderWidth);
	}

	@Override
	public void setColor (Color color)
	{
		box.setColor(color);
	}

	@Override
	public void setPosition (Point p)
	{
		box.setPosition(p);
	}

	@Override
	public void setX (int x)
	{
		box.setX(x);
	}

	@Override
	public void setY (int y)
	{
		box.setY(y);
	}

	@Override
	public String toString ()
	{
		return "Image+" + box.toString();
	}

	public void accept (Visitor visitor)
	{
		visitor.visitImage(this);
	}
}
