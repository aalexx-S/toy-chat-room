package Server;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tony on 2016/6/8.
 */
public class ImageProcessor {
    BufferedImage createResizeCopy(Image originalImage, int scaledWidth, int scaledHeight) {
        int imageType = BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }
}
