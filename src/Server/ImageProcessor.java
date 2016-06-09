package Server;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Iterator;

/**
 * Created by Tony on 2016/6/8.
 */
public class ImageProcessor {
    public BufferedImage createResizeCopy(Image originalImage, int scaledWidth, int scaledHeight) {
        int imageType = BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public String encodeImage(BufferedImage image, String type) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, baos);
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

    public String getFileType(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ImageInputStream stream = ImageIO.createImageInputStream(fis);
            Iterator iter = ImageIO.getImageReaders(stream);
            if (!iter.hasNext()) {
                return null;
            }
            ImageReader reader = (ImageReader) iter.next();
            ImageReadParam param = reader.getDefaultReadParam();
            reader.setInput(stream, true, true);
            BufferedImage bi;
            try {
                bi = reader.read(0, param);
                return reader.getFormatName();
            } finally {
                reader.dispose();
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
