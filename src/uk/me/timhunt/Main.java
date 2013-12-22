// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package uk.me.timhunt;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Main class to render the IFS.
 */
public class Main {
    Random r = new Random();

    /**
     * Standard entry point.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        (new Main()).run();
    }

    void run() throws IOException {
        BufferedImage image = createImage(2100, 2700);
        Graphics2D g = initialiseImage(image);
        BufferedImage image2 = createImage(2100, 2700);
        Graphics2D g2 = initialiseImage(image2);
        //drawIfs(g);
        g.fill(new Rectangle.Double(0, 0, 1, 1));
        AffineTransform[] transforms = getTransforms();
        for (int i = 0; i < transforms.length; i++) {
            transforms[i].scale(1.0 / 2100, 1.0 / 2700);
        }

        for (int i = 0; i < 60; ++i) {
            iterateIfs(transforms, g2, image);
            image.setData(image2.getRaster());
            if (i % 5 == 4) {
                saveImage(image, "test" + i + ".png");
                System.out.print(".");
            }
        }
    }

    AffineTransform[] getTransforms() {
        double yScale = 0.8;
        double xScale = 0.3;
        double overlap = 0;
        return new AffineTransform[] {
                new AffineTransform(
                        0,                 xScale,
                        0.5 + overlap / 2, -xScale / 2,
                        0,                 1 - xScale / 2),
                new AffineTransform(
                        0,                    xScale,
                        -(0.5 + overlap / 2), -xScale / 2,
                        1,                    1 - xScale / 2),
                new AffineTransform(
                        yScale,           0,
                        0,                yScale,
                        (1 - yScale) / 2, 0),
                new AffineTransform(
                        0.01,   0,
                        0,   1,
                        0.495, 0),
        };
    }

    void iterateIfs(AffineTransform[] transforms, Graphics2D target, BufferedImage source) {
        target.setBackground(new Color(0, 0, 0, 0));
        target.clearRect(0, 0, source.getWidth(), source.getHeight());
        for (int i = 0; i < transforms.length; i++) {
            target.drawRenderedImage(source, transforms[i]);
        }
    }

    void drawIfs(Graphics2D g) {
        Point2D.Double p = new Point2D.Double(0.5, 0.5);
        final AffineTransform[] transforms = getTransforms();
        for (int i = 0; i < 100000; i++) {
            transforms[r.nextInt(transforms.length)].transform(p, p);
            plotPoint(g, p.x, p.y);
            if (i % 10000 == 0) {
                System.out.println();
            }
            if (i % 100 == 0) {
                System.out.print(".");
            }
        }
    }

    /**
     * @param width of the image in pixels.
     * @param height of the image in pixels.
     */
    private BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Fill the image with white, set the plot transform so the coordinates are
     * (0, 0) to (1, 1) and return the Graphics2D.
     * @param image image to initialise.
     * @return ready for more plotting.
     */
    private Graphics2D initialiseImage(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.scale(image.getWidth(), image.getHeight());
        g.setColor(Color.BLACK);
        return g;
    }

    /**
     * @param i
     * @param j
     */
    private void plotPoint(Graphics2D g, double x, double y) {
        g.fill(new Rectangle.Double(x, y, 0.001, 0.001));
    }

    /**
     * @param string
     * @throws IOException
     */
    private void saveImage(BufferedImage image, String filename) throws IOException {
        ImageIO.write(image, "png", new File(filename));
    }
}
