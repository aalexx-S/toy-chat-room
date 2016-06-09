package Server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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

    String encodeImage(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //TODO determine image file type
            ImageIO.write(image, "png", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encodedImage = Base64.getEncoder().encodeToString(baos.toByteArray());
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedImage;
    }
}
