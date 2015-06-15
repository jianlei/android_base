package com.daren.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.daren.base.ui.CameraActivity;

/**
 * Description: 打开调用照相机功能
 * Created by wangjianlei on 15/6/5.
 */
public class CaptureUtil {

    /**
     * Start the camera by dispatching a camera intent.
     */
    public static void dispatchTakePictureIntent(Context context, Uri photoFileUri, int takePictureRequestCode) {

        // Check if there is a camera.
//        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(context, "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity) context;
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.setCapturedImageURI(photoFileUri);
            activity.setCurrentPhotoPath(photoFileUri.getPath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    activity.getCapturedImageURI());
            activity.startActivityForResult(takePictureIntent, takePictureRequestCode);
        }
    }
}
