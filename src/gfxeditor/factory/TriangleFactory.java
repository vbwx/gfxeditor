package gfxeditor.factory;

import gfxeditor.Shape;
import gfxeditor.shapes.Triangle;
import java.awt.Color;

// Stage 3
/**
 * This is the factory class used to instantiate <code>Triangle</code> objects.
 *
 * @see gfxeditor.shapes.Triangle
 * @author vbwx
 * @version 1.0
 */
public class TriangleFactory extends ShapeFactory
{
	/**
	 * Returns the name of this factory.
	 * @return "Triangle"
	 */
	public String getName () { return "Triangle"; }

	/**
	 * Creates a new shape with the given properties.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param c The fill color
	 * @return The triangle
	 */
	public Shape createShape (int x, int y, Color c)
	{
		return new Triangle(x, y, c);
	}
}
