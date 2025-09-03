package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

// Stage 4
/**
 * This shape represents an oval.
 *
 * It internally uses a <code>java.awt.geom.Ellipse2D</code> object and
 * is automatically enumerated, starting at 1.
 *
 * @author vbwx
 * @version 1.0
 */
public class Oval extends Shape
{
	private Ellipse2D oval;
	private boolean filled;
	private static int count = 0;
	private final int number;

	/**
	 * Returns the internal number of this shape.
	 * @return The internal number
	 * @since 2.0
	 */
	public int getNumber () { return number; }

	/**
	 * Returns the width of this oval.
	 * @return The width of the shape
	 */
	public int getWidth () { return (int)oval.getWidth(); }

	/**
	 * Returns the height of this oval.
	 * @return The height of the shape
	 */
	public int getHeight () { return (int)oval.getHeight(); }

	/**
	 * Returns the <code>filled</code> property of this oval.
	 * @return <code>true</code> if the shape is filled when painted
	 */
	public boolean isFilled () { return filled; }

	/**
	 * Sets the <code>filled</code> property of this oval.
	 * @param filled <code>true</code> if the shape is to be filled when painted
	 */
	public void setFilled (boolean filled) { this.filled = filled; }

	/**
	 * Constructs a filled oval.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The fill color
	 */
	public Oval (int x, int y, Color color)
	{
		super(x, y, color);
		filled = true;
		number = ++count;
	}

	/**
	 * Paints the oval.
	 * @param g The graphics context where the shape is to be painted
	 */
	public void paint (Graphics2D g)
	{
		if (filled) {
			g.setPaint(getColor());
			g.fill(oval);
		}
		if (getBorderWidth() > 0) {
			g.setStroke(new BasicStroke(getBorderWidth()));
			g.setPaint(getBorderColor());
			g.draw(oval);
		}
	}

	/**
	 * Sets the width and height of this oval.
	 * @param width The new width of the shape, in pixels
	 * @param height The new height of the shape, in pixels
	 */
	public void setSize (int width, int height)
	{
		oval = new Ellipse2D.Float(getX(), getY(), width, height);
	}

	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the oval
	 */
	public Object clone ()
	{
		Oval c = new Oval(getX(), getY(), getColor());
		c.oval = oval;
		c.setBorderWidth(getBorderWidth());
		c.setBorderColor(getBorderColor());
		c.filled = filled;
		return c;
	}

	/**
	 * Returns the <code>String</code> representation of this shape.
	 * @return "Oval" and the internal number of the oval
	 */
	@Override
	public String toString ()
	{
		return "Oval " + number;
	}

	/**
	 * Checks if a point is contained in this shape.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is within the oval
	 */
	public boolean contains (Point p)
	{
		return oval.contains(p.getX(), p.getY());
	}

	/**
	 * Sets the width of the oval to 40 and its height to 25.
	 */
	public void setToDefaults ()
	{
		setSize(40, 25);
	}

	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * @return <code>true</code> if the oval's width or height is less than or
	 * equal to 0
	 */
	public boolean isInvisible ()
	{
		return getWidth() <= 0 || getHeight() <= 0;
	}

	public void accept (Visitor visitor) { }
}
