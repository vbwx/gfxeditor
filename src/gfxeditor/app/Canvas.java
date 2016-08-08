package gfxeditor.app;

import gfxeditor.Model;
import gfxeditor.Shape;
import gfxeditor.event.GraphicsEvent;
import gfxeditor.event.GraphicsListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

// Stage 1
/**
 * Displays the shape objects stored in the model.
 * 
 * Also sends all user-triggered events to the controller.
 * 
 * @author Bernhard Waldbrunner
 * @version 1.0
 * @see gfxeditor.Model
 */
@SuppressWarnings("serial")
public final class Canvas extends JPanel implements GraphicsListener
{
	private Model model;
	
	/**
	 * The standard cursor for the select tool.
	 */
	final static Cursor SELECT_CURSOR;
	/**
	 * The standard cursor for any other tool (when creating shapes).
	 */
	final static Cursor EDIT_CURSOR;
	
	static {
		SELECT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
		EDIT_CURSOR   = new Cursor(Cursor.CROSSHAIR_CURSOR);
	}
	
	/**
	 * Constructs a canvas with white background.
	 * Also registers the controller as <code>MouseListener</code>,
	 * <code>MouseMotionListener</code> and <code>KeyListener</code>.
	 * @param ctrl The <code>Controller</code> managing this canvas
	 */
	public Canvas (Controller ctrl)
	{
		model = ctrl.getModel();
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(152, 200));
		addMouseListener(ctrl);
		addMouseMotionListener(ctrl);
		addKeyListener(ctrl);
	}
	
	/**
	 * Paints all of the model's shapes with anti-aliasing.
	 * @see gfxeditor.Shape#paint(Graphics2D)
	 */
	@Override
	protected void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D gfx = (Graphics2D)g;
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                     RenderingHints.VALUE_ANTIALIAS_ON);
		for (Shape s : model.getShapes())
			s.paint(gfx);
	}

	/**
	 * (Does nothing)
	 */
	public void shapeSelected (GraphicsEvent e) { }

	/**
	 * Repaints the canvas.
	 */
	public void shapeAdded (GraphicsEvent e)
	{
		repaint();
	}

	/**
	 * Repaints the canvas.
	 */
	public void shapeChanged (GraphicsEvent e)
	{
		repaint();
	}

	/**
	 * Repaints the canvas.
	 */
	public void shapeDeleted (GraphicsEvent e)
	{
		repaint();
	}
}
