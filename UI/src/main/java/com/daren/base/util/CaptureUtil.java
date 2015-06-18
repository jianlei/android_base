package com.daren.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.daren.base.ui.CameraActivity;

import java.io.File;

/**
 * Description: 打开调用照相机功能
 * Created by wangjianlei on 15/6/5.
 */
public class CaptureUtil {

    public static final String JPG_SUFFIX = "jpg";

    public static void dispatchTakePictureIntent(Context context, String photoFilePath, int takePictureRequestCode) {
        dispatchTakePictureIntent(context, new File(photoFilePath), takePictureRequestCode);
    }

    /**
     * 打开相机进行拍照
     * @param context
     * @param photoFile
     * @param takePictureRequestCode
     */
    public static void dispatchTakePictureIntent(Context context, File photoFile, int takePictureRequestCode) {
        dispatchTakePictureIntent(context, Uri.fromFile(photoFile), takePictureRequestCode);
    }

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




    /**
     * 获取sd卡路径
     * @return
     */
    public static String getSdcardRoot() {
        return Environment.getExternalStorageDirectory() + File.separator;
    }
}
