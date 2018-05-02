/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.IOException;
import java.io.Serializable;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author hansel
 */
public class ImageObject extends PartsImage{
    
    public ImageObject(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
    } // constructor

    
    
     @Override
    public void draw(GraphicsContext gc)throws IOException{
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(super.bytesToImage(), null));
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), (posix * pixelSize) + (1 + posix) * 10, (posiy * pixelSize) + (1 + posiy) * 10, pixelSize, pixelSize);
    } // draw

    public boolean chunkClicked(int xMouse, int yMouse) {
        if ((xMouse >= (posix * pixelSize) + (1 + posix) * 10 && xMouse <= (posix * pixelSize) + (1 + posix) * 10 + this.pixelSize)
                && (yMouse >= (posiy * pixelSize) + (1 + posiy) * 10 && yMouse <= (posiy * pixelSize) + (1 + posiy) * 10 + this.pixelSize)) {
            return true;
        }
        return false;
    } // chunkClicked

//    //atributos
//    private Image image;
//    private int pixelSize;
//    private int posX, posY;
//    private int partsImageWidth, partsImageHeight;
//
//    public ObjectImage(Image image, int pixelSize, int x, int y, int partsImageWidth, int partsImageHeight) {
//        this.image = image;
//        this.pixelSize = pixelSize;
//        this.posX = x;
//        this.posY = y;
//        this.partsImageWidth = partsImageWidth;
//        this.partsImageHeight = partsImageHeight;
//    }
//    
//    public ObjectImage(Image image, int x, int y, int pixelSize) {
//        this.image = image;
//        this.pixelSize = pixelSize;
//        this.posX = x;
//        this.posY = y;
//    }
//
//    public Image getImage() {
//        return image;
//    }
//
//    public void setImage(Image image) {
//        this.image = image;
//    }
//    
//    public int getPixelSize() {
//        return pixelSize;
//    }
//
//    public void setPixelSize(int pixelSize) {
//        this.pixelSize = pixelSize;
//    }
//
//    public int getX() {
//        return posX;
//    }
//
//    public void setX(int x) {
//        this.posX = x;
//    }
//
//    public int getY() {
//        return posY;
//    }
//
//    public void setY(int y) {
//        this.posY = y;
//    }
//
//    public int getPartsImageWidth() {
//        return partsImageWidth;
//    }
//
//    public void setPartsImageWidth(int partsImageWidth) {
//        this.partsImageWidth = partsImageWidth;
//    }
//
//    public int getPartsImageHeight() {
//        return partsImageHeight;
//    }
//
//    public void setPartsImageHeight(int partsImageHeight) {
//        this.partsImageHeight = partsImageHeight;
//    }
//
//    public void paintImage(GraphicsContext gc) {
//        gc.drawImage(image, (posX * pixelSize) + (1 + posX) * 10, (posY * pixelSize) + (1 + posY) * 10);
//    }
//    
////    public boolean pressMouse(int xMouse, int yMouse) {
////        if ((xMouse >= this.posX * pixelSize && xMouse <= this.posX * pixelSize + pixelSize)
////                && (yMouse >= this.posY * pixelSize && yMouse <= this.posY * pixelSize + pixelSize)) {
////            return true;
////        }
////        return false;
////    }
//
////    public boolean pressMouse(int xMouse, int yMouse) {
////        if ((xMouse >= this.posX * partsImageWidth && xMouse <= this.posX * partsImageWidth + this.partsImageWidth)
////                && (yMouse >= this.posY * partsImageHeight && yMouse <= this.posY * partsImageHeight + this.partsImageHeight)) {
////            return true;
////        }
////        return false;
////    } // pressMouse
//    
//    public boolean pressMouse(int xMouse, int yMouse) {
//        if ((xMouse >= (posX * pixelSize) + (1 + posX) * 10 && xMouse <= (posX * pixelSize) + (1 + posX) * 10 + pixelSize)
//                && (yMouse >= (posY * pixelSize) + (1 + posY) * 10 && yMouse <= (posY * pixelSize) + (1 + posY) * 10 + pixelSize)) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public String toString() {
//        return "ObjectImage{" + "image=" + image + ", posX=" + posX + ", posY=" + posY + ", partsImageWidth=" + partsImageWidth + ", partsImageHeight=" + partsImageHeight + '}';
//    }

}
