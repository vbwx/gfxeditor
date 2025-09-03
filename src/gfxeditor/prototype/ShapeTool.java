package gfxeditor.prototype;

import gfxeditor.Shape;
import java.awt.Color;

// Stage 2
/**
 * This class simply takes a <code>Shape</code> as a prototype and
 * clones it.
 *
 * In terms of usage, it is compatible to the factory method pattern.
 *
 * @see gfxeditor.factory.ShapeFactory
 * @author vbwx
 * @version 1.0
 */
public class ShapeTool
{
	private final Shape prototype;

	/**
	 * Returns the name of this tool.
	 * @return The simple class name of the prototype
	 */
	public String getName ()
	{
		return prototype.getClass().getSimpleName();
	}

	/**
	 * Constructs a shape tool.
	 * @param prototype The shape to be cloned
	 * @see gfxeditor.Shape
	 */
	public ShapeTool (Shape prototype)
	{
		this.prototype = prototype;
	}

	/**
	 * Creates a new shape based on the prototype and sets some of its properties.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param c The fill color
	 * @return The actual shape
	 * @throws CloneNotSupportedException
	 */
	public Shape createShape (int x, int y, Color c)
	throws CloneNotSupportedException
	{
		Shape s = (Shape)prototype.clone();
		s.setX(x); s.setY(y); s.setColor(c);
		return s;
	}
}
