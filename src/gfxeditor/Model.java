package gfxeditor;

import gfxeditor.decorators.Decorator;
import gfxeditor.decorators.NumberDecorator;
import gfxeditor.decorators.SelectionDecorator;
import gfxeditor.event.GraphicsEvent;
import gfxeditor.event.GraphicsListener;
import gfxeditor.visitors.BackwardVisitor;
import gfxeditor.visitors.ForwardVisitor;
import gfxeditor.visitors.Visitor;
import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

// Stage 1
/**
 * Contains a list of shapes and
 * also serves as a data model for the <code>JList</code> used in
 * <code>DocumentWindow</code>.
 * 
 * @author Bernhard Waldbrunner
 * @version 2.0
 * @see gfxeditor.event.GraphicsEvent
 */
public class Model implements ListModel
{
	private Set<GraphicsListener> graphicsListeners;
	private Set<ListDataListener> dataListeners;
	private List<Shape> shapes;
	private Shape selected, current;
	
	private static final byte ADDED = 1, DELETED = 2, CHANGED = 3, SELECTED = 4;
	
	/**
	 * Returns the list of shapes.
	 * @return An unmodifiable list of <code>Shape</code> objects
	 */
	public List<Shape> getShapes ()
	{
		return Collections.unmodifiableList(shapes);
	}
	
	/**
	 * Returns the currently selected shape.
	 * @return The currently selected <code>Shape</code> object
	 */
	public Shape getSelected () { return selected; }
	
	/**
	 * Returns the newly created shape.
	 * @return The newly created <code>Shape</code> object
	 */
	public Shape getCurrent () { return current; }
	
	/**
	 * Sets the currently selected shape.
	 * @param s The selected <code>Shape</code> object, or <code>null</code>
	 * if everything is to be deselected
	 */
	public void setSelected (Shape s)
	{
		if (selected == s) return;
		selected = removeSelectionDecorator(selected);
		if (s != null)
			selected = addSelectionDecorator(s);
		fireGraphicsEvent(selected, CHANGED);
		fireGraphicsEvent(selected, SELECTED);
	}
	
	/**
	 * Constructs a Graphics Editor model.
	 */
	public Model ()
	{
		graphicsListeners = new HashSet<GraphicsListener>();
		dataListeners = new HashSet<ListDataListener>();
		shapes = new LinkedList<Shape>();
	}
	
	/**
	 * Returns the registered graphics listeners.
	 * @return An array of <code>GraphicsListener</code> objects
	 */
	public GraphicsListener[] getGraphicsListeners ()
	{
		return graphicsListeners.toArray(new GraphicsListener[] {});
	}
	
	/**
	 * Removes a graphics listener from this model.
	 * @param l The <code>GraphicsListener</code> object to be removed
	 */
	public void removeGraphicsListener (GraphicsListener l)
	{
		if (l != null) graphicsListeners.remove(l);
	}
	
	/**
	 * Registers a graphics listener for this model.
	 * @param l The <code>GraphicsListener</code> object to be registered
	 */
	public void addGraphicsListener (GraphicsListener l)
	{
		if (l != null) graphicsListeners.add(l);
	}
	
	private void fireGraphicsEvent (Shape s, byte cause)
	{
		GraphicsEvent e = new GraphicsEvent(this, s);
		for (GraphicsListener l : graphicsListeners) {
			switch (cause) {
			case ADDED:    l.shapeAdded(e);    break;
			case DELETED:  l.shapeDeleted(e);  break;
			case SELECTED: l.shapeSelected(e); break;
			case CHANGED:  l.shapeChanged(e);  break;
			}
		}
	}
	
	private void fireListDataEvent (int cause, int index)
	{
		ListDataEvent e = new ListDataEvent(this, cause, index, index);
		for (ListDataListener l : dataListeners) {
			switch (cause) {
			case ListDataEvent.INTERVAL_ADDED:   l.intervalAdded(e);   break;
			case ListDataEvent.INTERVAL_REMOVED: l.intervalRemoved(e); break;
			case ListDataEvent.CONTENTS_CHANGED: l.contentsChanged(e); break;
			}
		}
	}
	
	/**
	 * Adds a shape to this model.
	 * Also fires a <code>GraphicsEvent</code> and a <code>ListDataEvent</code> to
	 * the registered views.
	 * @param s The <code>Shape</code> object to be added
	 */
	public void add (Shape s)
	{
		if (s == null) return;
		shapes.add(s);
		current = s;
		fireListDataEvent(ListDataEvent.INTERVAL_ADDED, shapes.size()-1);
		fireGraphicsEvent(s, ADDED);
	}
	
	/**
	 * Sets the <code>current</code> property (newly created shape) to <code>null</code>.
	 * May also assign default values to the current shape; deletes the newly created
	 * shape if it's invisible.
	 * @param useDefaults <code>true</code> if the shape should use its default values
	 * @see Shape#setToDefaults()
	 * @see Shape#isInvisible()
	 */
	public void finish (boolean useDefaults)
	{
		if (current == null) return;
		if (useDefaults) {
			current.setToDefaults();
			fireGraphicsEvent(current, CHANGED);
		}
		if (current.isInvisible())
			delete(current);
		current = null;
	}
	
	/**
	 * Removes a shape from this model.
	 * The <code>selected</code> property is only changed if the removed shape has been
	 * selected before (which is most likely the case).
	 * Also fires a <code>GraphicsEvent</code> and a <code>ListDataEvent</code> to
	 * the registered views.
	 * @param s The <code>Shape</code> object to be removed
	 */
	public void delete (Shape s)
	{
		if (s == null) return;
		int idx = shapes.indexOf(s);
		if (idx == -1) return;
		shapes.remove(s);
		if (s == selected) selected = null;
		s = null;
		fireListDataEvent(ListDataEvent.INTERVAL_REMOVED, idx);
		fireGraphicsEvent(s, DELETED);
	}
	
	/**
	 * Selects the shape at the given point, or sets the <code>selected</code> property to
	 * <code>null</code> if there is no shape at this position.
	 * If shapes are overlapping, the one above the other, i.e., the newer one is
	 * selected. Also fires a <code>GraphicsEvent</code> to the registered views.
	 * @param p The position where the mouse button has been pressed
	 * @see Shape#contains(Point)
	 */
	public void select (Point p)
	{
		if (p == null) return;
		ListIterator<Shape> li = shapes.listIterator(shapes.size());
		Shape sel = selected;
		selected = null;
		while (li.hasPrevious()) {
			Shape s = li.previous();
			if (s.contains(p)) {
				selected = s;
				break;
			}
		}
		if (selected == sel) return;
		removeSelectionDecorator(sel);
		if (selected != null)
			selected = addSelectionDecorator(selected);
		fireGraphicsEvent(selected, CHANGED);
		fireGraphicsEvent(selected, SELECTED);
	}
	
	/**
	 * Resizes the <i>current</i> shape, so that it's no bigger than the
	 * bounding rectangle specified by its position (<code>x</code>|<code>y</code>)
	 * and the given point.
	 * Also fires a <code>GraphicsEvent</code> to the registered views.
	 * @param p The lower right point of the bounding rectangle
	 * @see Shape#setSize(int, int)
	 */
	public void resize (Point p)
	{
		if (current == null || p == null) return;
		current.setSize(p.x - current.getX(), p.y - current.getY());
		fireGraphicsEvent(current, CHANGED);
	}
	
	/**
	 * Resizes the <i>selected</i> shape, so that it's no bigger than the
	 * bounding rectangle specified by its position (<code>x</code>|<code>y</code>)
	 * and the given point.
	 * Also fires a <code>GraphicsEvent</code> to the registered views.
	 * @param p The lower right point of the bounding rectangle
	 * @see Shape#setSize(int, int)
	 */
	public void resizeSelected (Point p)
	{
		if (selected == null || p == null) return;
		selected.setSize(p.x - selected.getX(), p.y - selected.getY());
		fireGraphicsEvent(selected, CHANGED);
	}
	
	/**
	 * Moves the <i>current</i> shape to the given point.
	 * Also fires a <code>GraphicsEvent</code> to the registered views.
	 * @param p The new position of the shape
	 * @see Shape#setPosition(Point)
	 */
	public void move (Point p)
	{
		if (current == null || p == null) return;
		current.setPosition(p);
		fireGraphicsEvent(current, CHANGED);
	}

	/**
	 * Moves the <i>selected</i> shape to the given point.
	 * Also fires a <code>GraphicsEvent</code> to the registered views.
	 * @param p The new position of the shape
	 * @see Shape#setPosition(Point)
	 */
	public void moveSelected (Point p)
	{
		if (selected == null || p == null) return;
		selected.setPosition(p);
		fireGraphicsEvent(selected, CHANGED);
	}

	/**
	 * Registers a list data listener for this model.
	 * @param l The <code>ListDataListener</code> object to be registered
	 */
	public void addListDataListener (ListDataListener l)
	{
		if (l != null) dataListeners.add(l);
	}

	/**
	 * Returns the shape at the given position.
	 * @param index The number of the shape within the list
	 * @return A <code>Shape</code> object
	 */
	public Object getElementAt (int index)
	{
		return shapes.get(index);
	}

	/**
	 * Returns the number of shapes in this model.
	 * @return The size of the list
	 */
	public int getSize ()
	{
		return shapes.size();
	}
	
	/**
	 * Removes a list data listener from this model.
	 * @param l The <code>ListDataListener</code> object to be removed
	 */
	public void removeListDataListener (ListDataListener l)
	{
		if (l != null) dataListeners.remove(l);
	}
	
	public void addNumberDecorators ()
	{
		ListIterator<Shape> li = shapes.listIterator();
		while (li.hasNext()) {
			Shape s = li.next();
			if (!(s instanceof NumberDecorator))
				li.set(new NumberDecorator(s));
		}
		fireGraphicsEvent(null, CHANGED);
	}
	
	public void removeNumberDecorators ()
	{
		ListIterator<Shape> li = shapes.listIterator();
		while (li.hasNext()) {
			Shape s = li.next();
			if (s instanceof NumberDecorator)
				li.set(((Decorator)s).getContent());
		}
		fireGraphicsEvent(null, CHANGED);
	}
	
	private Shape addSelectionDecorator (Shape sel)
	{
		int idx = shapes.indexOf(sel);
		if (idx >= 0) {
			Shape decoration = new SelectionDecorator(sel);
			shapes.set(idx, decoration);
			return decoration;
		}
		return sel;
	}
	
	private Shape removeSelectionDecorator (Shape sel)
	{
		int idx = shapes.indexOf(sel);
		if (idx >= 0 && sel instanceof SelectionDecorator) {
			Shape content = ((Decorator)sel).getContent();
			shapes.set(idx, content);
		}
		return null;
	}
	
	public void animateForwards (int speed)
	{
		Visitor animator = new ForwardVisitor(speed);
		for (Shape s : shapes)
			s.accept(animator);
		fireGraphicsEvent(null, CHANGED);
	}
	
	public void animateBackwards (int speed)
	{
		Visitor animator = new BackwardVisitor(speed);
		for (Shape s : shapes)
			s.accept(animator);
		fireGraphicsEvent(null, CHANGED);
	}
}
