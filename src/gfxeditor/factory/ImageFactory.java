package gfxeditor.factory;

import gfxeditor.Shape;
import gfxeditor.shapes.ImageAdapter;
import gfxeditor.shapes.Square;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

// Extension 1
public class ImageFactory extends ShapeFactory
{
	private final BufferedImage image;

	public ImageFactory (URL path) throws IOException
	{
		image = ImageIO.read(path);
	}
	
	public Shape createShape (int x, int y, Color c)
	{
		return new ImageAdapter(new Square(x, y, c), image);
	}

	public String getName ()
	{
		return "Image";
	}
}
