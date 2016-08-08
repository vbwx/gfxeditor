package gfxeditor.app;

import gfxeditor.Model;
import gfxeditor.Shape;
import gfxeditor.factory.ImageFactory;
import gfxeditor.factory.LineFactory;
import gfxeditor.factory.OvalFactory;
import gfxeditor.factory.ShapeFactory;
import gfxeditor.factory.TriangleFactory;
import gfxeditor.prototype.ShapeTool;
import gfxeditor.shapes.Circle;
import gfxeditor.shapes.Rectangle;
import gfxeditor.shapes.Square;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Stage 1
/**
 * This class initializes the Graphics Editor application,
 * defines its behavior for model-related events,
 * generates the views and changes the model.
 *
 * @version 2.0
 * @author Bernhard Waldbrunner
 * @see gfxeditor.Model
 */
public class Controller
implements MouseListener, MouseMotionListener, ActionListener, KeyListener,
           ListSelectionListener
{
	private Model model;
	private Canvas canvas;
	private DocumentWindow window;
	private static ShapeTool[] prototypes;
	private static ShapeFactory[] factories;
	private ShapeTool tool; // currently used
	private ShapeFactory factory; // currently used
	private boolean moved;
	private Point origin;

	/**
	 * The width of the screen, in pixels.
	 */
	static final int SCR_WIDTH;
	/**
	 * The height of the screen, in pixels.
	 */
	static final int SCR_HEIGHT;
	/**
	 * The application's resources path.
	 */
	static final String RES_PATH;

	static {
		SCR_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
		SCR_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
		RES_PATH = "/res/";
		prototypes = new ShapeTool[] {
			new ShapeTool(new Rectangle(-1, -1, null)),
			new ShapeTool(new Square(-1, -1, null)),
			new ShapeTool(new Circle(-1, -1, null))
		};
		try {
			factories = new ShapeFactory[] {
				new OvalFactory(),
				new LineFactory(),
				new TriangleFactory(),
				new ImageFactory(Controller.class.getResource(RES_PATH + "Smiley.png"))
			};
		} catch (IOException ex) { ex.printStackTrace(); }
	}

	/**
	 * Returns the model linked to this controller.
	 * @return The <code>Model</code> linked to this controller
	 */
	public Model getModel () { return model; }

	/**
	 * Returns the canvas managed by this controller.
	 * @return The <code>Canvas</code> managed by this controller
	 */
	public Canvas getCanvas () { return canvas; }

	/**
	 * Returns the available prototypes that can be used as tools.
	 * @return An array of <code>ShapeTool</code> objects
	 * @see gfxeditor.prototype.ShapeTool
	 */
	public static ShapeTool[] getPrototypes () { return prototypes; }

	/**
	 * Returns the available abstract factories that can be used as tools.
	 * @return An array of <code>ShapeFactory</code> objects
	 * @see gfxeditor.factory.ShapeFactory
	 */
	public static ShapeFactory[] getFactories () { return factories; }

	/**
	 * Constructs a controller that links itself to a new <code>Model</code>,
	 * <code>Canvas</code> and <code>DocumentWindow</code>.
	 * Also adds the views as <code>GraphicsListener</code> to the model,
	 * shows the window and loads the <i>Nimbus</i> look-and-feel, or
	 * <i>Metal</i> if the former is unavailable.
	 */
	public Controller ()
	{
		model = new Model();
		canvas = new Canvas(this);
		window = new DocumentWindow(this);
		selectTool("");
		model.addGraphicsListener(canvas);
		model.addGraphicsListener(window);
		try {
			UIManager.setLookAndFeel(selectPLAF());
			SwingUtilities.updateComponentTreeUI(window);
		} catch (Exception ex) { }
		window.setVisible(true);
	}

	private String selectPLAF ()
	{
		String laf = "";
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if (info.getClassName().endsWith("NimbusLookAndFeel"))
				return info.getClassName();
			else if (info.getClassName().endsWith("MetalLookAndFeel"))  // fallback
				laf = info.getClassName();
		}
		return laf;
	}

	/**
	 * Starts the application.
	 * @param args (Not used)
	 */
	public static void main (String[] args)
	{
		new Controller();
	}

	/**
	 * (Does nothing)
	 */
	public void mouseClicked (MouseEvent e) { }

	/**
	 * (Does nothing)
	 */
	public void mouseEntered (MouseEvent e) { }

	/**
	 * (Does nothing)
	 */
	public void mouseExited (MouseEvent e) { }

	/**
	 * Performs the function of the active tool on the point where the
	 * mouse button has been pressed.
	 * If the select tool is active, shape selection is delegated to the model.
	 * Otherwise a new shape will be created at the specified point.
	 * @see gfxeditor.Model#select(Point)
	 */
	public void mousePressed (MouseEvent e)
	{
		origin = e.getPoint();
		if (tool != null) {
			try {
				model.add(tool.createShape(origin.x, origin.y, randomColor()));
			} catch (CloneNotSupportedException ex) {
				JOptionPane.showMessageDialog(window, ex, "", JOptionPane.ERROR_MESSAGE);
			}
		} else if (factory != null) {
			model.add(factory.createShape(origin.x, origin.y, randomColor()));
		} else { // selection mode
			model.removeNumberDecorators();
			model.select(origin);
		}
		moved = false;
	}

	/**
	 * Finishes the resizing of the newly created shape.
	 */
	public void mouseReleased (MouseEvent e)
	{
		if (tool != null || factory != null)
			model.finish(!moved);
		origin = null;
	}

	/**
	 * Resizes the newly created shape or moves the selected shape,
	 * depending on the active tool.
	 */
	public void mouseDragged (MouseEvent e)
	{
		moved = true;
		if (tool != null || factory != null)
			model.resize(e.getPoint());
		else if (model.getSelected() != null) {
			Shape s = model.getSelected();
			model.moveSelected(new Point(s.getX()+e.getPoint().x-origin.x,
			                             s.getY()+e.getPoint().y-origin.y));
		}
		origin = e.getPoint();
	}

	/**
	 * (Does nothing)
	 */
	public void mouseMoved (MouseEvent e) { }

	/**
	 * Selects the tool selected in the tool bar.
	 * This can be a <code>ShapeTool</code> (prototype), a <code>ShapeFactory</code>,
	 * or the select tool, which is default and not represented by an object.
	 */
	public void actionPerformed (ActionEvent e)
	{
		selectTool(e.getActionCommand());
	}

	private void selectTool (String name)
	{
		tool = null;
		factory = null;
		if (name.equals("")) {
			canvas.setCursor(Canvas.SELECT_CURSOR);
		} else {
			model.setSelected(null);
			canvas.setCursor(Canvas.EDIT_CURSOR);
			for (ShapeTool t : prototypes)
				if (name.equals(t.getName())) {
					tool = t;
					return;
				}
			for (ShapeFactory f : factories)
				if (name.equals(f.getName())) {
					factory = f;
					return;
				}
		}
	}

	private static Color randomColor ()
	{
		Random rnd = new Random();
		return new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}

	/**
	 * Loads a GIF file from the resources folder.
	 * @param name The name of the image, without file name extension
	 * @return An <code>ImageIcon</code> containing the GIF data
	 * @see #RES_PATH
	 */
	protected static Icon loadImage (String name)
	{
		return new ImageIcon(Controller.class.getResource(RES_PATH + name + ".gif"));
	}

	/**
	 * Deselects all shapes on <i>Esc</i>, or deletes the selected shape on <i>Del</i>
	 * and <i>Backspace</i>, or adds a number decorator to all shapes on <i>Shift</i>.
	 * @see gfxeditor.Model#addNumberDecorators()
	 */
	public void keyPressed (KeyEvent e)
	{
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			model.setSelected(null); break;
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			model.delete(model.getSelected()); break;
		case KeyEvent.VK_SHIFT:
			model.addNumberDecorators(); break;
		case KeyEvent.VK_RIGHT:
			model.animateForwards(2); break;
		case KeyEvent.VK_LEFT:
			model.animateBackwards(2); break;
		}
	}

	/**
	 * Removes all number decorators from the shapes on <i>Shift</i>.
	 * @see gfxeditor.Model#removeNumberDecorators()
	 */
	public void keyReleased (KeyEvent e)
	{
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			model.removeNumberDecorators(); break;
		}
	}

	/**
	 * (Does nothing)
	 */
	public void keyTyped (KeyEvent e) { }

	/**
	 * Selects the shape corresponding to the selected value from the <code>JList</code>.
	 * Also activates the select tool in the tool bar.
	 * @see gfxeditor.Model#setSelected(Shape)
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		Object value = ((JList)e.getSource()).getSelectedValue();
		if (value == null) return;
		model.setSelected((Shape)value);
		window.activateSelectTool();
	}
}
