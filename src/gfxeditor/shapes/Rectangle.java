package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

// Stage 2
/**
 * This shape represents a rectangle.
 *
 * It internally uses a <code>java.awt.geom.Rectangle2D</code> object and
 * is automatically enumerated, starting at 1.
 *
 * @author vbwx
 * @version 1.0
 */
public class Rectangle extends Shape
{
	private Rectangle2D rectangle;
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
	 * Returns the width of this rectangle.
	 * @return The width of the shape
	 */
	public int getWidth () { return (int)rectangle.getWidth(); }

	/**
	 * Returns the height of this rectangle.
	 * @return The height of the shape
	 */
	public int getHeight () { return (int)rectangle.getHeight(); }

	/**
	 * Returns the <code>filled</code> property of this rectangle.
	 * @return <code>true</code> if the shape is filled when painted
	 */
	public boolean isFilled () { return filled; }

	/**
	 * Sets the <code>filled</code> property of this rectangle.
	 * @param filled <code>true</code> if the shape is to be filled when painted
	 */
	public void setFilled (boolean filled) { this.filled = filled; }

	/**
	 * Constructs a filled rectangle.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The fill color
	 */
	public Rectangle (int x, int y, Color color)
	{
		super(x, y, color);
		filled = true;
		number = count++;
	}

	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the rectangle
	 */
	public Object clone ()
	{
		Rectangle r = new Rectangle(getX(), getY(), getColor());
		r.rectangle = rectangle;
		r.setBorderColor(getBorderColor());
		r.setBorderWidth(getBorderWidth());
		r.filled = filled;
		return r;
	}

	/**
	 * Sets the width and height of this rectangle.
	 * @param width The new width of the shape, in pixels
	 * @param height The new height of the shape, in pixels
	 */
	public void setSize (int width, int height)
	{
		rectangle = new Rectangle2D.Float(getX(), getY(), width, height);
	}

	/**
	 * Paints the rectangle.
	 * @param g The graphics context where the shape is to be painted
	 */
	public void paint (Graphics2D g)
	{
		if (filled) {
			g.setPaint(getColor());
			g.fill(rectangle);
		}
		if (getBorderWidth() > 0) {
			g.setStroke(new BasicStroke(getBorderWidth()));
			g.setPaint(getBorderColor());
			g.draw(rectangle);
		}
	}

	/**
	 * Returns the <code>String</code> representation of this shape.
	 * @return "Rectangle" and the internal number of the rectangle
	 */
	@Override
	public String toString ()
	{
		return "Rectangle " + number;
	}

	/**
	 * Checks if a point is contained in this shape.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is within the rectangle
	 */
	public boolean contains (Point p)
	{
		return rectangle.contains(p.getX(), p.getY());
	}

	/**
	 * Sets the width of the rectangle to 40 and its height to 20.
	 */
	public void setToDefaults ()
	{
		setSize(40, 20);
	}

	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * @return <code>true</code> if the rectangle's width or height is less than or
	 * equal to 0
	 */
	public boolean isInvisible ()
	{
		return getWidth() <= 0 || getHeight() <= 0;
	}

	public void accept (Visitor visitor)
	{
		visitor.visitRectangle(this);
	}
}
