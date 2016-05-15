/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.support;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;
import static org.opencv.imgproc.Imgproc.*;



/**
 *
 * @author aleksandr
 */
public class RotareFilter extends ImageFilter {

    /**
     * Rotation angle
     */
    double angle;
    
    @Override
    public Mat filterImage(Mat image) {        
            Mat result = new Mat();
            Point center = new Point(image.width()/2f, image.height()/2f);
            Mat transform = getRotationMatrix2D( center, angle, 1.);
            //Count shift and rect
            Rect bbox = new RotatedRect(center,image.size(), angle).boundingRect();
            transform.put(0, 2,
                    transform.get(0, 2)[0]+ bbox.width/2. - center.x);
            transform.put(1, 2,
                    transform.get(1, 2)[0] + bbox.height/2 - center.y);
            warpAffine(image, result, transform, bbox.size());
            return result;
    }
    
    /**
     * Construct filter for image rotation
     * @param angle rotation angle
     */
    public RotareFilter(double angle){
        this.angle = angle;
    }
    
}
