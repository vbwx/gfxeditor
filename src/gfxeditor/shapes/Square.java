package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

// Stage 4
/**
 * This shape represents a square.
 *
 * It internally uses a <code>java.awt.geom.Rectangle2D</code> object and
 * is automatically enumerated, starting at 1. The width and height of
 * this shape are always equal.
 *
 * @author vbwx
 * @version 1.0
 */
public class Square extends Shape
{
	private Rectangle2D square;
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
	 * Returns the diameter of this square in pixels.
	 * @return The width of the shape
	 */
	public int getWidth () { return (int)square.getWidth(); }

	/**
	 * Returns the diameter of this square in pixels.
	 * @return The height of the shape
	 */
	public int getHeight () { return (int)square.getHeight(); }

	/**
	 * Returns the <code>filled</code> property of this square.
	 * @return <code>true</code> if the shape is filled when painted
	 */
	public boolean isFilled () { return filled; }

	/**
	 * Sets the <code>filled</code> property of this square.
	 * @param filled <code>true</code> if the shape is to be filled when painted
	 */
	public void setFilled (boolean filled) { this.filled = filled; }

	/**
	 * Constructs a filled square.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The fill color
	 */
	public Square (int x, int y, Color color)
	{
		super(x, y, color);
		filled = true;
		number = count++;
	}

	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the square
	 */
	public Object clone ()
	{
		Square r = new Square(getX(), getY(), getColor());
		r.square = square;
		r.setBorderColor(getBorderColor());
		r.setBorderWidth(getBorderWidth());
		r.filled = filled;
		return r;
	}

	/**
	 * Sets the side length of the square to the smaller one of the parameters.
	 */
	public void setSize (int width, int height)
	{
		int l = Math.min(width, height);
		square = new Rectangle2D.Float(getX(), getY(), l, l);
	}

	/**
	 * Paints the square.
	 * @param g The graphics context where the shape is to be painted
	 */
	public void paint (Graphics2D g)
	{
		if (filled) {
			g.setPaint(getColor());
			g.fill(square);
		}
		if (getBorderWidth() > 0) {
			g.setStroke(new BasicStroke(getBorderWidth()));
			g.setPaint(getBorderColor());
			g.draw(square);
		}
	}

	/**
	 * Returns the <code>String</code> representation of this shape.
	 * @return "Square" and the internal number of the square
	 */
	@Override
	public String toString ()
	{
		return "Square " + number;
	}

	/**
	 * Checks if a point is contained in this shape.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is within the square
	 */
	public boolean contains (Point p)
	{
		return square.contains(p.getX(), p.getY());
	}

	/**
	 * Sets the side length of the square to 30 pixels.
	 */
	public void setToDefaults ()
	{
		setSize(30, 30);
	}

	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * @return <code>true</code> if the square's side length is
	 * less than or equal to 0
	 */
	public boolean isInvisible ()
	{
		return getWidth() <= 0 || getHeight() <= 0;
	}

	public void accept (Visitor visitor) { }
}
