package com.daren.base.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rex St. John (on behalf of AirPair.com) on 3/6/14.
 */
public class ImageUtil {

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }


    private static String getCurrentTime() {
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder(formater.format(new Date()));
        return sb.toString();
    }


    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws java.io.IOException
     */
    public static Boolean saveFile(Bitmap bm, String filePath)
            throws IOException {
        Boolean success = false;
        BufferedOutputStream bos = null;
        try {
            File myCaptureFile = new File(filePath);
            myCaptureFile.createNewFile();
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            bos = null;
            success = true;
        } catch (Exception e) {
            success = false;
        } finally {
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        }
        return success;
    }

    public static void waterMarkBitmap(String filePath, Bitmap bitmap,String  content){
        Bitmap resultBM = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvasTemp = new Canvas(resultBM);
//            canvasTemp.drawColor(Color.WHITE);
        Paint p = new Paint();
//            String familyName = “宋体”;
//            Typeface font = Typeface.create(familyName,Typeface.BOLD);
        p.setColor(Color.WHITE);
//            p.setTypeface(font);
        p.setTextSize(22);
        int textWidth = getTextWidth(p, content);
        canvasTemp.drawText(content, resultBM.getWidth() - textWidth - 10, resultBM.getHeight() - 35, p);

        String  time = getCurrentTime();
        int timeWidth = getTextWidth(p, time);

        canvasTemp.drawText(time, resultBM.getWidth() - timeWidth - 10, resultBM.getHeight() - 10, p);

        try {
            saveFile(resultBM, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scale the photo down and fit it to our image views.
     * <p/>
     * "Drastically increases performance" to set images using this technique.
     * Read more:http://developer.android.com/training/camera/photobasics.html
     */
    public static Bitmap getBitmapFromSize(String imagePath, int width, int height) {
        // Get the dimensions of the View
        int targetW = width;//imageView.getWidth();
        int targetH = height;//imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        return bitmap;
    }

    /**
     * Get thumbnail as a bitmap from a path given a resolution
     *
     * @param path
     * @param thumbnailSize
     * @return
     */
    public static Bitmap getThumbnailBitmap(String path, int thumbnailSize) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / thumbnailSize;
        return BitmapFactory.decodeFile(path, opts);
    }

    /**
     * Use for decoding camera response data.
     * <p/>
     * Example call:
     * Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
     * startActivityForResult(i, R.integer.PHOTO_SELECT_ACTION);
     *
     * @param data
     * @param context
     * @return
     */
    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }
}
