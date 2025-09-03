package gfxeditor.factory;

import gfxeditor.Shape;
import java.awt.Color;

// Stage 3
/**
 * This is the abstract factory class used to instantiate <code>Shape</code> objects.
 *
 * In terms of usage, it is compatible to the prototype pattern.
 *
 * @see gfxeditor.Shape
 * @see gfxeditor.prototype.ShapeTool
 * @author vbwx
 * @version 1.0
 */
public abstract class ShapeFactory
{
	/**
	 * Creates a new shape with the given properties.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param c The fill color
	 * @return The actual shape
	 */
	public abstract Shape createShape (int x, int y, Color c);

	/**
	 * Returns the name of this factory.
	 * @return The name of the shape factory
	 */
	public abstract String getName ();
}
