/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import domain.MosaicObject;
import data.SaveFileData;
import domain.PartsImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author hansel
 */
public class SaveFileBusiness {
    SaveFileData fileData;

    public SaveFileBusiness() {
        this.fileData=new SaveFileData();
    }
    
    public void saveProject(PartsImage[][] matrizImage, PartsImage[][] matrizMosaic, File file) throws IOException{
        this.fileData.saveProject(matrizImage, matrizMosaic, file);
    }
    
    public List<PartsImage[][]> recover(File file) throws IOException, ClassNotFoundException{
        return this.fileData.recover(file);
    }
//    public void saveMosaic(MosaicObjectImage matrizMosaicImages, File file) throws IOException, ClassNotFoundException {
//        this.fileData.saveMosaic(matrizMosaicImages);
//    }
//        
//    public List<MosaicObjectImage> recover() throws IOException, ClassNotFoundException {
//        return this.fileData.recover();
//    }
//    
//    public void newProject(){
//        this.fileData.newProject();
//    }
//    
//    public MosaicObjectImage obtaineNamePRoject(String name) throws  IOException, ClassNotFoundException{
//        return this.fileData.obtaineNameProject(name);
//    }
//    
//    public boolean obtaineNameProjectBoolean(String name) throws IOException, ClassNotFoundException {
//        return this.fileData.obtaineNameProjectBoolean(name);
//    }
}
