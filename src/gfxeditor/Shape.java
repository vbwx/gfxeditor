package gfxeditor;

import gfxeditor.visitors.Visitor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

// Stage 1
/**
 * This is the abstract shape that is "produced" by the factories and
 * cloned by the prototypes.
 * The view representing the model only needs to take care of this abstract
 * base class.
 * 
 * <p><b>Note:</b> This abstract class does not store how the actual shape looks
 * like, i.e., you have to take care of that. Don't forget to use the properties
 * that are stored in this class, or, in case you don't need some of them,
 * at least override the getters and setters, so that its behavior is
 * predictable.
 * <br>In most cases, you will store the actual shape as a <code>java.awt.geom</code>
 * object; if the size <i>or</i> the position (<code>x</code>,<code>y</code>) of
 * the <code>Shape</code> object is changed, its geometry object (stored in the
 * derived class) also needs to be updated.
 * 
 * @author Bernhard Waldbrunner
 * @version 1.0
 */
public abstract class Shape implements Cloneable
{
	private int x, y, borderWidth;
	private Color color, borderColor;

	/**
	 * Returns the horizontal value of this shape's position.
	 * <p><b>Note:</b> You may want to override this method in a derived class.
	 * @return The <code>x</code> property
	 */
	public int getX () { return x; }

	/**
	 * Sets the horizontal value of this shape's position.
	 * @param x The new horizontal position of the shape
	 */
	public void setX (int x)
	{
		this.x = x;
		setSize(getWidth(), getHeight());
	}

	/**
	 * Returns the vertical value of this shape's position.
	 * <p><b>Note:</b> You may want to override this method in a derived class.
	 * @return The <code>y</code> property
	 */
	public int getY () { return y; }

	/**
	 * Sets the vertical value of this shape's position.
	 * @param y The new vertical position of the shape
	 */
	public void setY (int y)
	{
		this.y = y;
		setSize(getWidth(), getHeight());
	}
	
	/**
	 * Sets the position of this shape.
	 * This is equal to a call of <code>setX(int)</code> and <code>setY(int)</code>,
	 * but faster.
	 * @param p The new position of the shape
	 */
	public void setPosition (Point p)
	{
		x = p.x; y = p.y;
		setSize(getWidth(), getHeight());
	}
	
	/**
	 * Returns the fill color of this shape.
	 * <p><b>Note:</b> You may want to override this method in a derived class.
	 * @return The <code>color</code> property of the shape
	 */
	public Color getColor () { return color; }

	/**
	 * Sets the fill color of this shape.
	 * <p><b>Note:</b> You may want to override this method in a derived class,
	 * especially if your shape doesn't use a fill color.
	 * @param color The new fill color of the shape
	 */
	public void setColor (Color color) { this.color = color; }

	/**
	 * Returns the border/line width of this shape.
	 * @return The <code>borderWidth</code> property of the shape
	 */
	public int getBorderWidth () { return borderWidth; }

	/**
	 * Sets the border/line width of this shape.
	 * @param borderWidth The new border/line width of the shape
	 */
	public void setBorderWidth (int borderWidth) { this.borderWidth = borderWidth; }

	/**
	 * Returns the border color of this shape.
	 * @return The <code>borderColor</code> property of the shape
	 */
	public Color getBorderColor () { return borderColor; }

	/**
	 * Sets the border color of this shape.
	 * @param borderColor The new border color of the shape
	 */
	public void setBorderColor (Color borderColor) { this.borderColor = borderColor; }

	/**
	 * Constructs a generic shape with a width and height of 0, a border/line width of
	 * 0 and with black border color.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param color The fill color
	 */
	public Shape (int x, int y, Color color)
	{
		this.x = x;
		this.y = y;
		this.color = color;
		setSize(0, 0);
		borderColor = Color.BLACK;
		borderWidth = 0;
	}
	
	public abstract void accept (Visitor visitor);
	
	/**
	 * Sets the width and height of this shape.
	 * <p><b>Note:</b> This should be the method that creates the internal
	 * geometric representation of the actual shape, as it's called by the
	 * constructor and the setters of <code>Shape</code>.
	 * @param width The new width of the shape, in pixels
	 * @param height The new height of the shape, in pixels
	 */
	public abstract void setSize (int width, int height);
	
	/**
	 * Returns the width of this shape.
	 * @return The width of the shape
	 */
	public abstract int getWidth ();
	
	/**
	 * Returns the height of this shape.
	 * @return The height of the shape
	 */
	public abstract int getHeight ();
	
	/**
	 * Sets this shape's properties to their default values.
	 * <p><b>Note:</b> Preferably, this method only calls <code>setSize(int, int)</code>
	 * with default values.
	 */
	public abstract void setToDefaults ();
	
	/**
	 * Paints the actual shape using its internal geometric representation.
	 * This is a delegate method for the application's canvas.
	 * @param g The graphics context where the shape is to be painted
	 * @see gfxeditor.app.Canvas#paintComponents(java.awt.Graphics)
	 */
	public abstract void paint (Graphics2D g);
	
	/**
	 * Checks if a point is contained in this shape.
	 * <p><b>Note:</b> This method should delegate the task to <code>contains(Point)</code>
	 * of the internal geometric representation.
	 * @param p The position where a mouse button has been pressed
	 * @return <code>true</code> if the point is within the actual shape
	 */
	public abstract boolean contains (Point p);
	
	/**
	 * Checks if the shape is not visible when <code>paint(Graphics2D)</code>
	 * is called.
	 * This is most likely the case if the shape's width or height is less than or equal
	 * to 0, but it depends on the geometric representation.
	 * @return <code>true</code> if the actual shape is not visible when painted
	 */
	public abstract boolean isInvisible ();
	
	/**
	 * Returns the internal number of this shape.
	 * @return The internal number
	 * @since 2.0
	 */
	public abstract int getNumber ();
	
	/**
	 * Clones this shape, so that it can be used as a prototype.
	 * @return A copy of the actual shape
	 */
	@Override
	public abstract Object clone ();
}
