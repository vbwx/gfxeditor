package gfxeditor.factory;

import gfxeditor.Shape;
import gfxeditor.shapes.Line;
import java.awt.Color;

// Stage 3
/**
 * This is the factory class used to instantiate <code>Line</code> objects.
 *
 * @see gfxeditor.shapes.Line
 * @author vbwx
 * @version 1.0
 */
public class LineFactory extends ShapeFactory
{
	/**
	 * Returns the name of this factory.
	 * @return "Line"
	 */
	public String getName () { return "Line"; }

	/**
	 * Creates a new shape with the given properties.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param c The fill color
	 * @return The line
	 */
	public Shape createShape (int x, int y, Color c)
	{
		return new Line(x, y, c);
	}
}
