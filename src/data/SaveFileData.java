/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import domain.ImageObject;
import domain.MosaicObject;
import domain.PartsImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hansel
 */
public class SaveFileData {

    public SaveFileData() {
    }

    public void saveProject(PartsImage[][] matrizImage, PartsImage[][] matrizMosaic, File file) throws FileNotFoundException, IOException {
        List<PartsImage[][]> all = new ArrayList<>();
        all.add(matrizImage);
        all.add(matrizMosaic);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(all);
        objectOutputStream.close();
    } // save

    public List<PartsImage[][]> recover(File file) throws IOException, ClassNotFoundException {
        List<PartsImage[][]> previous = new ArrayList<>();
        if (file.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            Object aux = objectInputStream.readObject();
            previous = (List<PartsImage[][]>) aux;
            objectInputStream.close();
        } // if(myFile.exists())
        return previous;
    } // recover
}
