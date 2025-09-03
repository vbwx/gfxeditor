package gfxeditor.app;

import gfxeditor.Shape;
import gfxeditor.event.GraphicsEvent;
import gfxeditor.event.GraphicsListener;
import gfxeditor.factory.ShapeFactory;
import gfxeditor.prototype.ShapeTool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.metal.MetalLookAndFeel;

// Stage 1
/**
 * This class builds the GUI.
 *
 * <p>It primarily contains two views that
 * listen to changes in the model, i.e., the canvas and a <code>JList</code> of
 * drawing objects.
 *
 * @author vbwx
 * @version 1.0
 * @see Canvas
 */
@SuppressWarnings("serial")
public final class DocumentWindow extends JFrame implements GraphicsListener
{
	private JToolBar toolbar;
	private JSplitPane pane;
	private JList list;
	private JToggleButton select;

	/**
	 * Constructs a document window.
	 * Also loads the prototypes/factories
	 * from the <code>Controller</code> into the tool bar and adds the
	 * <code>Canvas</code> object from the <code>Controller</code>.
	 * <p><b>Note:</b> When this constructor is called, the <code>Model</code>
	 * and the <code>Canvas</code> already have to exist in the controller,
	 * along with the prototypes and factories.
	 * @param ctrl The controller managing this view
	 * @see Controller
	 */
	public DocumentWindow (Controller ctrl)
	{
		super("GfxEditor");
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(300, 225));
		setPreferredSize(new Dimension(400, 300));

		toolbar = new JToolBar(SwingConstants.VERTICAL);
		loadTools(ctrl);
		toolbar.setFloatable(false);
		add(toolbar, BorderLayout.WEST);

		pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pane.setOneTouchExpandable(true);
		pane.setResizeWeight(1);
		pane.setDividerLocation(240);
		list = new JList(ctrl.getModel());
		list.setBorder(BorderFactory.createEmptyBorder());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(ctrl);
		list.addKeyListener(ctrl);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Shapes", SwingConstants.CENTER), BorderLayout.NORTH);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);
		panel.setMinimumSize(new Dimension(90, 200));
		pane.setRightComponent(panel);
		pane.setLeftComponent(ctrl.getCanvas());
		pane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0,
			MetalLookAndFeel.getControlDarkShadow()));
		add(pane, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocation((Controller.SCR_WIDTH-getWidth())/2,
                    (Controller.SCR_HEIGHT-getHeight())/2);
	}

	private void loadTools (Controller ctrl)
	{
		ButtonGroup bg = new ButtonGroup();
		select = new JToggleButton(Controller.loadImage("Select"));
		select.setActionCommand("");
		select.addActionListener(ctrl);
		select.setFocusable(false);
		toolbar.add(select);
		bg.add(select);
		bg.setSelected(select.getModel(), true);

		JToggleButton btn;
		for (ShapeTool tool : Controller.getPrototypes()) {
			btn = new JToggleButton(Controller.loadImage(tool.getName()));
			btn.setActionCommand(tool.getName());
			btn.addActionListener(ctrl);
			btn.setFocusable(false);
			toolbar.add(btn);
			bg.add(btn);
		}
		for (ShapeFactory factory : Controller.getFactories()) {
			btn = new JToggleButton(Controller.loadImage(factory.getName()));
			btn.setActionCommand(factory.getName());
			btn.addActionListener(ctrl);
			btn.setFocusable(false);
			toolbar.add(btn);
			bg.add(btn);
		}
	}

	/**
	 * Selects the active <code>Shape</code> in the <code>JList</code>,
	 * or deselects everything if no shape is active.
	 */
	public void shapeSelected (GraphicsEvent e)
	{
		Shape shape = e.getShape();
		if (shape == null)
			list.clearSelection();
		else
			list.setSelectedValue(shape, true);
	}

	/**
	 * (Does nothing)
	 */
	public void shapeAdded (GraphicsEvent e) { }

	/**
	 * (Does nothing)
	 */
	public void shapeChanged (GraphicsEvent e) { }

	/**
	 * Deselects everything in the <code>JList</code>.
	 */
	public void shapeDeleted (GraphicsEvent e)
	{
		list.clearSelection();
	}

	/**
	 * Makes the select tool the active one.
	 */
	protected void activateSelectTool ()
	{
		if (!select.isSelected()) select.doClick();
	}
}
