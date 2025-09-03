package gfxeditor.event;

import gfxeditor.Shape;
import java.util.EventObject;

// Stage 1
/**
 * This class represents an event that occurs when anything in the
 * model has changed.
 *
 * <p><b>Note:</b> This also includes the (de)selection of a drawing object.
 * <br>The <code>Shape</code> object stored in this class is always required.
 *
 * @author vbwx
 * @version 1.0
 * @see GraphicsListener
 */
@SuppressWarnings("serial")
public class GraphicsEvent extends EventObject
{
	private Shape shape;

	/**
	 * Returns the changed shape.
	 * @return The <code>Shape</code> object that has been changed,
	 * or <code>null</code> if a shape has been removed from the model or
	 * all shapes have been deselected.
	 */
	public Shape getShape () { return shape; }

	/**
	 * Constructs a graphics event, storing the changed shape object.
	 * @param source The model firing this event
	 * @param shape The <code>Shape</code> object that has been changed,
	 * or <code>null</code> if a shape has been removed from the model or
	 * all shapes have been deselected.
	 */
	public GraphicsEvent (Object source, Shape shape)
	{
		super(source);
		this.shape = shape;
	}
}
