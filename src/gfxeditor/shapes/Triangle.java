package gfxeditor.shapes;

import gfxeditor.Shape;
import gfxeditor.visitors.Visitor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

// Stage 3
/**
 * This shape represents a horizontal triangle, whose sides that are
 * pointing up or down are of equal length.
 * The origin of the triangle's bounding rectangle (<code>x</code>,<code>y</code>)
 * is in the upper left-hand corner.
 *
 * It internally uses a <code>java.awt.Polygon</code> object and
 * is automatically enumerated, starting at 1.
 *
 * @author Bernhard Waldbrunner
 * @version 1.0
 */
public class Triangle extends Shape
{
	private Polygon triangle;
	private boolean filled;
	private final int number;
	private static int count = 0;

	/**
	 * Returns the internal number of this shape.
	 * @return The internal number
	 * @since 2.0
	 */
	public int getNumber () { return number; }

	/**
	 * Returns the width of this triangle.
	 * @return The width of the shape
	 */
	public int getWidth ()
	{
		return (triangle.xpoints[1] < triangle.xpoints[0] ? -1 : 1) *
		       triangle.getBounds().width;
	}

	/**
	 * Returns the height of this triangle.
	 * @return The height of the shape
	 */
	public int getHeight ()
	{
		return (triangle.ypoints[1] < triangle.ypoints[2] ? -1 : 1) *
		       triangle.getBounds().height;
	}

	/**
	 * Returns the <code>filled</code> property of this triangle.
	 * @return <code>true</code> if the shape is filled when painted
	 */
	public boolean isFilled () { return filled; }

	/**
	 * Sets the <code>filled</code> property of this triangle.
	 * @param filled <code>true</code> if the shape is to be filled when painted
	 */
	public void setFilled (boolean filled) { this.filled = filled; }

	/**
	 * Constructs a filled triangle.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The fill color
	 */
	public Triangle (int x, int y, Color color)
	{
		super(x, y, color);
		number = ++count;
		filled = true;
	}

	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the triangle
	 */
	// actually not necessary for abstract factory pattern
	public Object clone ()
	{
		Triangle t = new Triangle(getX(), getY(), getColor());
		t.triangle = triangle;
		t.setBorderColor(getBorderColor());
		t.setBorderWidth(getBorderWidth());
		t.filled = filled;
		return t;
	}

	/**
	 * Sets the side lengths of this triangle, so that it fits inside a
	 * bounding rectangle with the given width and height.
	 * @param width The new width of the shape, in pixels
	 * @param height The new height of the shape, in pixels
	 */
	public void setSize (int width, int height)
	{
		triangle = new Polygon(new int[] {getX(), getX()+width, getX()+width/2},
		                       new int[] {getY()+height, getY()+height, getY()}, 3);
	}

	/**
	 * Paints the triangle.
	 * @param g The graphics context where the shape is to be painted
	 */
	public void paint (Graphics2D g)
	{
		if (filled) {
			g.setPaint(getColor());
			g.fill(triangle);
		}
		if (getBorderWidth() > 0) {
			g.setStroke(new BasicStroke(getBorderWidth()));
			g.setPaint(getBorderColor());
			g.draw(triangle);
		}
	}

	/**
	 * Returns the <code>String</code> representation of this shape.
	 * @return "Triangle" and the internal number of the triangle
	 */
	@Override
	public String toString ()
	{
		return "Triangle " + number;
	}

	/**
	 * Checks if a point is contained in this shape.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is within the triangle
	 */
	public boolean contains (Point p)
	{
		return triangle.contains(p);
	}

	/**
	 * Sets the width of the triangle to 30 and its height to 30.
	 */
	public void setToDefaults ()
	{
		setSize(30, 30);
	}

	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * @return <code>true</code> if the triangle's width or height is equal to 0
	 */
	public boolean isInvisible ()
	{
		return getWidth() == 0 || getHeight() == 0;
	}

	public void accept (Visitor visitor)
	{
		visitor.visitTriangle(this);
	}
}
