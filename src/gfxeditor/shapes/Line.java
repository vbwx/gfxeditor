package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

// Stage 3
/**
 * This shape represents a line.
 *
 * It internally uses a <code>java.awt.geom.Line2D</code> object and
 * is automatically enumerated, starting at 1.
 *
 * @author vbwx
 * @version 1.0
 */
public class Line extends Shape
{
	private Line2D line;
	private final int number;
	private static int count = 0;

	/**
	 * Returns the internal number of this shape.
	 * @return The internal number
	 * @since 2.0
	 */
	public int getNumber () { return number; }

	/**
	 * Returns the width of this line in pixels.
	 * @return The width of the shape
	 */
	public int getWidth () { return (int)(line.getX2()-line.getX1()); }

	/**
	 * Returns the height of this line in pixels.
	 * @return The height of the shape
	 */
	public int getHeight () { return (int)(line.getY2()-line.getY1()); }

	/**
	 * Returns the color of this line, which is equal to the border color.
	 * @return The color of the shape
	 */
	@Override
	public Color getColor () { return getBorderColor(); }

	/**
	 * Sets the color of this line.
	 */
	@Override
	public void setColor (Color c) { setBorderColor(c); }

	/**
	 * Constructs a line with a width and height of 0 and a thickness of 1 px.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The color
	 */
	public Line (int x, int y, Color color)
	{
		super(x, y, null);
		setBorderColor(color);
		number = ++count;
		setBorderWidth(1);
	}

	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the line
	 */
	// actually not necessary for abstract factory pattern
	public Object clone ()
	{
		Line l = new Line(getX(), getY(), getColor());
		l.line = line;
		l.setBorderWidth(getBorderWidth());
		return l;
	}

	/**
	 * Sets the width and height of this line.
	 * @param width The new width of the line, in pixels
	 * @param height The new height of the line, in pixels
	 */
	public void setSize (int width, int height)
	{
		line = new Line2D.Float(getX(), getY(), getX()+width, getY()+height);
	}

	/**
	 * Paints the line.
	 * @param g The graphics context where the shape is to be painted
	 */
	public void paint (Graphics2D g)
	{
		if (getBorderWidth() > 0) {
			g.setPaint(getBorderColor());
			g.setStroke(new BasicStroke(getBorderWidth()));
			g.draw(line);
		}
	}

	/**
	 * Returns the <code>String</code> representation of this shape.
	 * @return "Line" and the internal number of the line
	 */
	@Override
	public String toString ()
	{
		return "Line " + number;
	}

	/**
	 * Checks if a point is contained in this shape.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is approximately on the line
	 */
	public boolean contains (Point p)
	{
		return line.ptSegDist(p) <= 2.0;
	}

	/**
	 * Sets the width and height of this line, so that its end point is at (0,0).
	 */
	public void setToDefaults ()
	{
		setSize(-getX(), -getY());
	}

	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * @return <code>true</code> if the thickness of this line
	 * is less than or equal to 0
	 */
	public boolean isInvisible ()
	{
		return getBorderWidth() <= 0;
	}

	public void accept (Visitor visitor) { }
}
