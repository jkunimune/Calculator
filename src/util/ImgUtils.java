/**
 * MIT License
 *
 * Copyright (c) 2016 Justin Kunimune
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package util;

import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

/**
 * A class of static methods to manipulate JavaFX's Canvases and Images.
 *
 * @author jkunimune
 */
public class ImgUtils {

	private static final double DEF_FONT_SIZE = 24;
	
	public static final Image NULL = new WritableImage(1,1);
	
	private static final Font PLAIN = Font.font("System", DEF_FONT_SIZE);
	private static final Font ITALIC = Font.font("System", FontPosture.ITALIC,
			DEF_FONT_SIZE);
	
	private static final double LINE_WIDTH = 2;
	private static final double SPACING = 4;
	
	
	public static Image drawString(String s) {	// cast a String to an Image
		return drawString(s, false);
	}
	
	
	public static Image drawString(String s, boolean italics) {
		return drawString(s, italics, DEF_FONT_SIZE);
	}
	
	
	public static Image drawString(String s, boolean italics, double size) {
		final Text txt = new Text(s);
		if (size == DEF_FONT_SIZE) {
			if (italics)	txt.setFont(ITALIC);
			else			txt.setFont(PLAIN);
		}
		else {
			if (italics)	txt.setFont(Font.font("System", FontPosture.ITALIC, size));
			else			txt.setFont(Font.font("System", FontPosture.REGULAR, size));
		}
		
		final Canvas canvas = new Canvas(			// make a canvas of the
				txt.getLayoutBounds().getWidth(),	// appropriate size
				txt.getLayoutBounds().getHeight());
		final GraphicsContext g = getGraphics(canvas);
		g.setFont(txt.getFont());
		g.fillText(s, 0, txt.getBaselineOffset());	// and draw the string
		return canvas.snapshot(null, null);
	}
	
	
	public static Image stretchX(Image img, double s) {	// resizes widht
		final Canvas canvas = new Canvas(s, img.getHeight());
		final GraphicsContext g = getGraphics(canvas);
		g.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
		return canvas.snapshot(null, null);
	}
	
	
	public static Image stretchY(Image img, double s) {	// resizes height
		final Canvas canvas = new Canvas(img.getWidth(), s);
		final GraphicsContext g = getGraphics(canvas);
		g.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
		return canvas.snapshot(null, null);
	}
	
	
	public static Image cropTop(Image img, double s) {	// more of a canvas size
		final Canvas canvas = new Canvas(img.getWidth(), s);
		final GraphicsContext g = getGraphics(canvas);
		g.drawImage(img, 0, canvas.getHeight()-img.getHeight());
		return canvas.snapshot(null, null);
	}
	
	
	public static Image horzCat(Image... images) {	// concatenate horizontally
		double totW = 0;
		double maxH = 0;
		for (Image img: images) {	// first, figure out how big
			totW += img.getWidth();	// the output will need to be
			if (img.getHeight() > maxH)
				maxH = img.getHeight();
		}
		
		final Canvas canvas = new Canvas(totW, maxH);	// make the canvas
		final GraphicsContext g = getGraphics(canvas);
		double curX = 0;
		for (Image img: images) {
			g.drawImage(img, curX, (maxH-img.getHeight())/2);
			curX += img.getWidth();						// and add each image
		}
		
		return canvas.snapshot(null, null);
	}
	
	
	public static Image vertCat(Image... images) {	// concatenate vertically
		double maxW = 0;
		double totH = 0;
		for (Image img: images) {	// first, figure out how big
			totH += img.getHeight();	// the output will need to be
			if (img.getWidth() > maxW)
				maxW = img.getWidth();
		}
		
		final Canvas canvas = new Canvas(maxW, totH);	// make a canvas
		final GraphicsContext g = getGraphics(canvas);
		double curY = 0;
		for (Image img: images) {
			g.drawImage(img, (maxW-img.getWidth())/2, curY);
			curY += img.getHeight();					// and add each image
		}
		
		return canvas.snapshot(null, null);
	}
	
	
	public static Image link(List<Image> images, String s) {	// concatenate images with Strings between them
		if (images.isEmpty())
			return null;
		Image output = images.get(0);
		for (int i = 1; i < images.size(); i ++)
			output = horzCat(output, drawString(s), images.get(i));
		return output;
	}
	
	
	public static Image split(Image i1, Image i2) {	// draws a fraction
		final Canvas canvas = new Canvas(
				Math.max(i1.getWidth(), i2.getWidth()) + 2*SPACING,
				i1.getHeight()+i2.getHeight() + LINE_WIDTH);
		final GraphicsContext g = getGraphics(canvas);
		g.drawImage(i1, (canvas.getWidth()-i1.getWidth())/2, 0);
		g.fillRect(0, i1.getHeight(), canvas.getWidth(), LINE_WIDTH);
		g.drawImage(i2,  (canvas.getWidth()-i2.getWidth())/2,
				i1.getHeight()+LINE_WIDTH);
		return canvas.snapshot(null, null);
	}
	
	
	public static Image wrap(String s1, Image img, String s2) {
		double size = Math.max(img.getHeight(), DEF_FONT_SIZE);
		
		return horzCat(cropTop(stretchY(drawString(s1), 1.25*size), 1.05*size),
				img, cropTop(stretchY(drawString(s2), 1.25*size), 1.05*size));
	}
	
	
	public static Image superS(Image img) {	// raise the baseline
		final Canvas canvas = new Canvas(img.getWidth()*0.6,
				img.getHeight()*1.0);
		final GraphicsContext g = canvas.getGraphicsContext2D();
		g.drawImage(img, 0, 0,
				img.getWidth()*0.6, img.getHeight()*0.6);
		return canvas.snapshot(null, null);
	}
	
	
	public static Image subS(Image img) {	// lower the baseline
		final Canvas canvas = new Canvas(img.getWidth()*0.6,
				img.getHeight()*1.0);
		final GraphicsContext g = canvas.getGraphicsContext2D();
		g.drawImage(img, 0, img.getHeight()*0.4,
				img.getWidth()*0.6, img.getHeight()*0.6);
		return canvas.snapshot(null, null);
	}


	public static Image overline(Image img) {	// put a line over it
		final Canvas canvas = new Canvas(
				img.getWidth(), img.getHeight() + 2*(SPACING+LINE_WIDTH));
		final GraphicsContext g = getGraphics(canvas);
		g.drawImage(img, 0, SPACING+LINE_WIDTH);
		g.fillRect(0, SPACING, canvas.getWidth(), LINE_WIDTH);
		return canvas.snapshot(null, null);
	}


	public static Image call(String f, Image arg) {	// draw a function call
		return horzCat(drawString(f), wrap("(", arg, ")"));
	}
	
	
	public static Image callInv(String f, Image arg) {	// darw an inverse func
		return horzCat(drawString(f), superS(drawString("-1")),
				wrap("(", arg, ")"));
	}
	
	
	
	private static GraphicsContext getGraphics(Canvas c) {
		final GraphicsContext g = c.getGraphicsContext2D();
		//g.clearRect(0, 0, c.getWidth(), c.getHeight());XXX
		return g;
	}

}
