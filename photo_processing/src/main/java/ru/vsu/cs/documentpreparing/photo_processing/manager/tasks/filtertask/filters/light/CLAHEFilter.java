/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light;

import java.util.LinkedList;
import java.util.List;
import static org.opencv.core.Core.merge;
import static org.opencv.core.Core.split;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2YCrCb;
import static org.opencv.imgproc.Imgproc.COLOR_YCrCb2BGR;
import static org.opencv.imgproc.Imgproc.cvtColor;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 *
 * @author aleksandr
 */
public class CLAHEFilter extends ImageFilter {
    
    final Size cellSize;
    final long clipSize;

    @Override
    public Mat filterImage(Mat image) {
        if(image.channels() >= 3)
        {
            Mat ycrcb = new Mat();

            cvtColor(image,ycrcb, COLOR_BGR2YCrCb);

            List<Mat> channels = new LinkedList<>();
            split(ycrcb,channels);

            CLAHE clahe = Imgproc.createCLAHE(clipSize, cellSize);
            
            clahe.apply(channels.get(0), channels.get(0));

            Mat result = new Mat();
            merge(channels,ycrcb);

            cvtColor(ycrcb,result,COLOR_YCrCb2BGR);

            return result;
        }
        return image;
    }
    
    public CLAHEFilter(long clipSize, long cellSize){
        this.clipSize = clipSize;
        this.cellSize = new Size(cellSize, cellSize);
    }
    
}
