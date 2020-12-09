package com.example.tap_fix;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class AnalyzeBarcode {
    private static final String TAG = "BarcodeAnalyzer";
    private BarcodeScanner scanner;
    private KitInterface kitInterface;
    private SendRequest sendRequest;

    public AnalyzeBarcode(KitInterface kitInterface){
        this.kitInterface = kitInterface;
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_CODE_39)
                .build();
        scanner = BarcodeScanning.getClient(options);
    }

    public void analyzeBarcode(ImageProxy imageProxy) {
        @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();
        InputImage inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

        scanner.process(inputImage).addOnFailureListener(e -> imageProxy.close());
        scanner.process(inputImage).addOnSuccessListener(barcodes -> {
            for (Barcode barcode: barcodes) {
                Rect bounds = barcode.getBoundingBox();
                Point[] corners = barcode.getCornerPoints();
                String rawValue = barcode.getRawValue();
                int valueType = barcode.getValueType();
                if(valueType == Barcode.TYPE_URL){
                    String url = barcode.getUrl().getUrl();
                    kitInterface.sendScannedCode(url);
                } else kitInterface.sendScannedCode(rawValue);

            }
            imageProxy.close();
        });
    }

}
