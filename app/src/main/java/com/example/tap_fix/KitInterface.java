package com.example.tap_fix;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.List;

public interface KitInterface {
    void onBarcodeAnalyzed(List<Barcode> visionBarcodes);
    public void sendScannedCode(String code);
}
