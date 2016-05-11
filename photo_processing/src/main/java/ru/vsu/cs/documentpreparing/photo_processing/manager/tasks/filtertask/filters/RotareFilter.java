/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters;

import org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 *
 * @author aleksandr
 */
public class RotareFilter extends ImageFilter {

    /**
     * Rotation angle
     */
    int angle;
    
    @Override
    public IplImage filterImage(IplImage image) {        
            IplImage result = IplImage.create(image.height(), image.width(),
                    image.depth(), image.nChannels());
            CvMat transform = CvMat.create(2, 3);
            float shift = Math.abs(image.height()-image.width());
            cv2DRotationMatrix(new float[]{image.width()/2f, image.height()/2f}, angle, 1, transform);
            transform = transform.put(0, 2, transform.get(0,2)-shift/2);
            transform = transform.put(1, 2, transform.get(1,2)+shift/2);
            cvWarpAffine(image, result, transform);
            return result;
    }
    
    /**
     * Construct filter for image rotation
     * @param angle rotation angle
     */
    public RotareFilter(int angle){
        this.angle = angle;
    }
    
}
