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
import sun.java2d.pipe.DrawImage;

/**
 *
 * @author hansel
 */
public class MosaicObject extends PartsImage{

    
    private int rotation;
    private int horizontal, vertical, negativoHorizontal, negativoVertical;
    //atributos

    public MosaicObject(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
        this.rotation = 0;
        this.horizontal = 0;
        this.vertical = 0;
        this.negativoHorizontal = 1;
        this.negativoVertical = 1;
    }

    public void rotate(int click) {
        if (click == 0) {
            if (rotation < 360) {
                rotation += 90;
            } else {
                rotation = rotation * 0 + 90;
            }
        } else {
            if (rotation > 0) {
                rotation -= 90;
            } else {
                rotation = 360 - 90;
            }
        }
    } // rotate

    public void setRotation() { // Cambiar nombre al metodo
        this.rotation = 0;
        flipHorizontal(0);
        flipVertical(0);
    } // setRotation

    public void flipHorizontal(int click) {
        if (click == 1) { // derecha
            if (horizontal == pixelSize) {
                horizontal = 0;
                negativoHorizontal = 1;
            } else {
                horizontal = pixelSize;
                negativoHorizontal = -1;
            }
        } else { // izquierda
            horizontal = 0;
            negativoHorizontal = 1;
        }
    } // flip

    public void flipVertical(int click) {
        if (click == 1) { // abajo
            if(vertical == pixelSize){
                vertical = 0;
                negativoVertical = 1;
            }else{
                vertical = pixelSize;
                negativoVertical = -1;
            }
        } else { // arriba
            vertical = 0;
            negativoVertical = 1;
        }
    } // flip

    @Override
    public void draw(GraphicsContext gc) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bytesToImage(), null));
        imageView.setRotate(imageView.getRotate() + rotation);
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), posix * pixelSize + horizontal, posiy * pixelSize + vertical, pixelSize * negativoHorizontal, pixelSize * negativoVertical);
    } // draw

    @Override
    public boolean chunkClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.posix * pixelSize && xMouse <= this.posix * pixelSize + pixelSize)
                && (yMouse >= this.posiy * pixelSize && yMouse <= this.posiy * pixelSize + pixelSize)) {
            return true;
        }
        return false;
    } // chunkClicked
    
//    
//    public int getPosX() {
//        return posX;
//    }
//
//    public void setPosX(int x) {
//        this.posX = x;
//    }
//
//    public int getPosY() {
//        return posY;
//    }
//
//    public void setPosY(int y) {
//        this.posY = y;
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
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
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
//    public void paintImage(GraphicsContext gc) {
//        gc.drawImage(image, posX * pixelSize, posY * pixelSize, pixelSize, pixelSize);
//    }
//
//    public boolean pressMouse(int xMouse, int yMouse) {
//        if ((xMouse >= this.posX * pixelSize && xMouse <= this.posX * pixelSize + pixelSize)
//                && (yMouse >= this.posY * pixelSize && yMouse <= this.posY * pixelSize + pixelSize)) {
//            return true;
//        }
//        return false;
//    } // pressMouse
//    
////    public boolean mosaicClicked(int xMouse, int yMouse, int pixelSize) {
////        if ((xMouse >= this.posX * pixelSize && xMouse <= this.posX * pixelSize + pixelSize)
////                && (yMouse >= this.posY * posY && yMouse <= this.posY * pixelSize + pixelSize)) {
////            return true;
////        }
////        return false;
////    } // chunkClicked
//
//    @Override
//    public String toString() {
//        return "MosaicObjectImage{" + "posX=" + posX + ", posY=" + posY + ", sizeImage=" + pixelSize + ", image=" + image + '}';
//    }
//
}