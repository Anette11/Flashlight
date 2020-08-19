package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout constraintLayout;
    private Button buttonOnOff;
    private final String LOG_TAG = "myLog";
    private final String LOG_MSG = "CameraAccessException: ";
    private final String FLASH_NOT_AVAILABLE = "Flash is not available on the device!";
    private boolean isFlashOn = false;
    private boolean cameraHasFlash;
    private boolean enable = false;
    private CameraManager cameraManager;
    private String cameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    public void onClick(View v) {
        if (cameraHasFlash) {
            if (isFlashOn) {
                setButtonOff();
                flashOff();
            } else {
                setButtonOn();
                flashOn();
            }
        } else {
            Toast.makeText(this, FLASH_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize() {
        constraintLayout = findViewById(R.id.constraint_layout);
        buttonOnOff = findViewById(R.id.buttonOnOff);
        buttonOnOff.setOnClickListener(this);
        cameraHasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (cameraManager != null) {
                cameraID = cameraManager.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            Log.d(LOG_TAG, LOG_MSG + e.getMessage());
        }
        setButtonOff();
    }

    private void setButtonOn() {
        constraintLayout.setBackgroundResource(R.drawable.shape);
        buttonOnOff.setText(R.string.button_on);
    }

    private void setButtonOff() {
        constraintLayout.setBackgroundResource(R.drawable.shape2);
        buttonOnOff.setText(R.string.button_off);
    }

    private void flashOn() {
        enable = true;
        flashSwitch();
    }

    private void flashOff() {
        enable = false;
        flashSwitch();
    }

    private void flashSwitch() {
        try {
            if (cameraManager != null) {
                cameraManager.setTorchMode(cameraID, enable);
                isFlashOn = enable;
            }
        } catch (CameraAccessException e) {
            Log.d(LOG_TAG, LOG_MSG + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFlashOn) {
            flashOff();
        }
    }
}
