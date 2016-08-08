package gfxeditor.event;

import java.util.EventListener;

// Stage 1
/**
 * An <code>EventListener</code> that has to be implemented by every view.
 * 
 * The model manages a list of such listeners.
 * 
 * @author Bernhard Waldbrunner
 * @version 1.0
 * @see GraphicsEvent
 */
public interface GraphicsListener extends EventListener
{
	/**
	 * Invoked when a shape has been changed (moved or resized).
	 * @param e The event object containing the <code>Shape</code>
	 */
	public void shapeChanged (GraphicsEvent e);
	
	/**
	 * Invoked when a shape has been added to the model.
	 * @param e The event object containing the <code>Shape</code>
	 */
	public void shapeAdded (GraphicsEvent e);
	
	/**
	 * Invoked when a shape has been removed from the model.
	 * @param e The event object containing the <code>Shape</code>
	 */
	public void shapeDeleted (GraphicsEvent e);
	
	/**
	 * Invoked when a shape has been selected or the selection
	 * has been cleared.
	 * @param e The event object containing the <code>Shape</code>
	 */
	public void shapeSelected (GraphicsEvent e);
}
