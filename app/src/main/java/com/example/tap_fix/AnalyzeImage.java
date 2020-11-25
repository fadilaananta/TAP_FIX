package com.example.tap_fix;

import androidx.camera.core.ImageProxy;

public interface AnalyzeImage {
    void onImageAnalyzed(ImageProxy inputImage);
}
