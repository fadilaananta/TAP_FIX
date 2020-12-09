package com.example.tap_fix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.example.tap_fix.KitInterface;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements KitInterface, AnalyzeImage{

    private static final String TAG = "BarcodeScannerActivity";
    @Nullable
    private SendRequest sendRequest;
    private ImageAnalysis analysisUseCase;
    private PreviewView previewView;
    private Button scan_btn;
    private Button cam_btn;
    private TextView kata;
    private CameraSelector cameraSelector;
    private Camera camera;
    private final int REQUEST_CODE = 1;
    private AnalyzeBarcode analyzeBarcode;
    private ImageAnalyze imageAnalyze;
//    private Button testlogin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest = new SendRequest();
        cam_btn = findViewById(R.id.button_scan);
        cam_btn.setOnClickListener(v -> {
            setupCamera();
        });
        //testlogin = findViewById(R.id.testlogin);
//        testlogin.setOnClickListener(v -> {
//            sendMessage();
//        });
        scan_btn = findViewById(R.id.scan_btn);
        analyzeBarcode = new AnalyzeBarcode(this);
    }

    private void setupCamera() {
        imageAnalyze = new ImageAnalyze(this);
        setContentView(R.layout.content_main);
        previewView = findViewById(R.id.preview_view);
        scan_btn = findViewById(R.id.scan_btn);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            startCamera();
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        imageAnalyze.isAnalysing = true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCamera();
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e){

                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
        final ImageCapture imageCapture = builder
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        analysisUseCase = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1080, 1920))
                .build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        imageAnalyze = new ImageAnalyze(this);
        imageAnalysis.setAnalyzer(executorService, imageAnalyze);

        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);
    }



/*    private void bindAnalysis(){
        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        analysisUseCase = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1080, 1920))
            .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        imageAnalyze = new ImageAnalyze(this);
        imageAnalysis.setAnalyzer(executorService, imageAnalyze);
    }*/

    @Override
    public void onBarcodeAnalyzed(List<Barcode> visionBarcodes) {
        for(Barcode barcode : visionBarcodes){
            if (barcode.getBoundingBox().height() <= 35)
                continue;
            Toast.makeText(this, "Ada barcode gan", Toast.LENGTH_SHORT).show();
            return;
        }
    }




    @Override
    public void onImageAnalyzed(ImageProxy imageProxy) {
        scan_btn.setOnClickListener(v -> {
            analyzeBarcode.analyzeBarcode(imageProxy);
        });
    }

    public void sendScannedCode(String code){
        kata = findViewById(R.id.barcodeRawValue);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(code != null && !code.isEmpty()){
                    sendRequest.code = code;
                    kata.setText(code);
                }
                sendRequest.postData();
            }
        });
    }
}