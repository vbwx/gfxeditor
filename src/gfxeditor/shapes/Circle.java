package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

// Stage 2
/**
 * This shape represents a circle.
 *
 * It internally uses a <code>java.awt.geom.Ellipse2D</code> object and
 * is automatically enumerated, starting at 1. The width and height of
 * this shape are always equal.
 *
 * @author Bernhard Waldbrunner
 * @version 1.0
 */
public class Circle extends Shape
{
	private Ellipse2D circle;
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
	 * Returns the diameter of this circle in pixels.
	 * @return The width of the shape
	 */
	public int getWidth () { return (int)circle.getWidth(); }

	/**
	 * Returns the diameter of this circle in pixels.
	 * @return The height of the shape
	 */
	public int getHeight () { return (int)circle.getHeight(); }

	/**
	 * Returns the <code>filled</code> property of this circle.
	 * @return <code>true</code> if the shape is filled when painted
	 */
	public boolean isFilled () { return filled; }

	/**
	 * Sets the <code>filled</code> property of this circle.
	 * @param filled <code>true</code> if the shape is to be filled when painted
	 */
	public void setFilled (boolean filled) { this.filled = filled; }

	/**
	 * Constructs a filled circle.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The fill color
	 */
	public Circle (int x, int y, Color color)
	{
		super(x, y, color);
		filled = true;
		number = count++;
	}

	/**
	 * Paints the circle.
	 * @param g The graphics context where the shape is to be painted
	 */
	public void paint (Graphics2D g)
	{
		if (filled) {
			g.setPaint(getColor());
			g.fill(circle);
		}
		if (getBorderWidth() > 0) {
			g.setStroke(new BasicStroke(getBorderWidth()));
			g.setPaint(getBorderColor());
			g.draw(circle);
		}
	}

	/**
	 * Sets the diameter of the circle to the smaller one of the parameters.
	 */
	public void setSize (int width, int height)
	{
		int d = Math.min(width, height);
		circle = new Ellipse2D.Float(getX(), getY(), d, d);
	}

	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the circle
	 */
	public Object clone ()
	{
		Circle c = new Circle(getX(), getY(), getColor());
		c.circle = circle;
		c.setBorderWidth(getBorderWidth());
		c.setBorderColor(getBorderColor());
		c.filled = filled;
		return c;
	}

	/**
	 * Returns the <code>String</code> representation of this shape.
	 * @return "Circle" and the internal number of the circle
	 */
	@Override
	public String toString ()
	{
		return "Circle " + number;
	}

	/**
	 * Checks if a point is contained in this shape.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is within the circle
	 */
	public boolean contains (Point p)
	{
		return circle.contains(p.getX(), p.getY());
	}

	/**
	 * Sets the diameter of the circle to 30 pixels.
	 */
	public void setToDefaults ()
	{
		setSize(30, 30);
	}

	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * @return <code>true</code> if the circle's diameter is less than or equal to 0
	 */
	public boolean isInvisible ()
	{
		return getWidth() <= 0 || getHeight() <= 0;
	}

	public void accept (Visitor visitor)
	{
		visitor.visitCircle(this);
	}
}
