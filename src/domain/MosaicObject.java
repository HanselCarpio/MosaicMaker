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
    public void printImageOnMosaic(GraphicsContext gc) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bytesToImage(), null));
        imageView.setRotate(imageView.getRotate() + rotation);
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), this.posix * this.pixelSize + this.horizontal, this.posiy * 
                this.pixelSize + this.vertical, this.pixelSize * this.negativoHorizontal, this.pixelSize * this.negativoVertical);
    } // draw

    @Override
    public boolean pressMouse(int xMouse, int yMouse) {
        if ((xMouse >= this.posix * this.pixelSize && xMouse <= this.posix * this.pixelSize + this.pixelSize)
                && (yMouse >= this.posiy * this.pixelSize && yMouse <= this.posiy * this.pixelSize + this.pixelSize)) {
            return true;
        }
        return false;
    } 
}
