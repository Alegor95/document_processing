/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.components.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author aleksandr
 */
public class ImageContainer {
    
    private Image sceneImage;
    
    private Mat opencvImage;
    
    private String fileName;
    
    public Image getSceneImage(){
        return sceneImage;
    }
    
    public void setSceneImage(Image img){
        this.sceneImage = img;
        this.opencvImage = ImageContainer.ImageToMat(img);
    }
    
    public Mat getOpenCVImage(){
        return opencvImage;
    }
    
    public void setOpenCVImage(Mat value){
        this.sceneImage = ImageContainer.MatToImage(value);
        this.opencvImage = value;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    @Override
    public String toString(){
        return this.fileName;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof ImageContainer){
            return this.fileName.equals(((ImageContainer) o).fileName);
        }
        return false;
    }
    
    private static Image MatToImage(Mat frame){
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
    
    private static Mat ImageToMat(Image img){
        BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
        return bufferedImage2Mat( bImage);
    }
    
    public static Mat bufferedImage2Mat(BufferedImage in)
    {
          Mat out;
          byte[] data;
          int r, g, b;
          int height = in.getHeight();
          int width = in.getWidth();
          if(in.getType() == BufferedImage.TYPE_INT_RGB || in.getType() == BufferedImage.TYPE_INT_ARGB)
          {
              out = new Mat(height, width, CvType.CV_8UC3);
              data = new byte[height * width * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
              for(int i = 0; i < dataBuff.length; i++)
              {
                  data[i*3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                  data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                  data[i*3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
              }
          }
          else
          {
              out = new Mat(height, width, CvType.CV_8UC1);
              data = new byte[height * width * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
              for(int i = 0; i < dataBuff.length; i++)
              {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
              }
           }
           out.put(0, 0, data);
           return out;
    }
    
    public ImageContainer(String filename, Image sceneImage){
        this.fileName = filename;
        this.sceneImage = sceneImage;
        this.opencvImage = ImageContainer.ImageToMat(sceneImage);
    }
    
    public ImageContainer(String filename, Mat opencvImage){
        this.fileName = filename;
        this.sceneImage = ImageContainer.MatToImage(opencvImage);
        this.opencvImage = opencvImage;
    }
}
