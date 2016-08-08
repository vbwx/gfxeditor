package gfxeditor.factory;

import gfxeditor.Shape;
import gfxeditor.shapes.Oval;
import java.awt.Color;

// Stage 4
/**
 * This is the factory class used to instantiate <code>Oval</code> objects.
 * 
 * @see gfxeditor.shapes.Oval
 * @author Bernhard Waldbrunner
 * @version 1.0
 */
public class OvalFactory extends ShapeFactory
{
	/**
	 * Returns the name of this factory.
	 * @return "Oval"
	 */
	public String getName () { return "Oval"; }
	
	/**
	 * Creates a new shape with the given properties.
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param c The fill color
	 * @return The oval
	 */
	public Shape createShape (int x, int y, Color c)
	{
		return new Oval(x, y, c);
	}
}
