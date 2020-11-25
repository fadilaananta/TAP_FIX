package com.example.tap_fix;

import androidx.annotation.NonNull;
import androidx.annotation.experimental.UseExperimental;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

public class ImageAnalyze implements ImageAnalysis.Analyzer {
    private final String TAG = getClass().getSimpleName();
    private AnalyzeImage analyzeImage;
    public boolean isAnalysing = true;

    public ImageAnalyze(AnalyzeImage analyzeImage) {
        this.analyzeImage = analyzeImage;
    }


    @UseExperimental(markerClass = androidx.camera.core.ExperimentalGetImage.class)
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        if (!isAnalysing) {
            imageProxy.close();
            return;
        }
        analyzeImage.onImageAnalyzed(imageProxy);
    }


}
