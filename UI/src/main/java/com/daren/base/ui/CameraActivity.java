
package com.daren.base.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daren.base.util.CaptureUtil;
import com.daren.base.util.ImageUtil;
import com.daren.base.util.ToastUtil;
import com.daren.base.widget.WeightGridLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 拍照界面继承此界面
 * 1. 调用setWeightGridLayout(WeightGridLayout weightGridLayout) ，传入weightGridLayout，确定需要显示图片的view之后，
 * 完成初始化。
 * 2.  通过调用doTakePicture(filePath)，传入文件路径，完成拍照
 * <p/>
 * 3.  调用getPictureHolders()，获取所有的图片对象
 *
 * @author wjl
 */
public abstract class CameraActivity extends BaseActionbarActivity {

    public static final int TAKE_PICTURE_REQUEST_CODE = 999;

    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;

    private ImageLayoutAdapter imageLayoutAdpater;
    private WeightGridLayout weightGridLayout;

    public void setWeightGridLayout(WeightGridLayout weightGridLayout) {
        this.weightGridLayout = weightGridLayout;
        imageLayoutAdpater = new ImageLayoutAdapter(this, weightGridLayout);
        this.weightGridLayout.setGridAdapter(imageLayoutAdpater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * 客户端可以通过此方法获取所有的拍照数据
     *
     * @return
     */
    public List<PictureHolder> getPictureHolders() {
        return imageLayoutAdpater.getDatas();
    }

    /**
     * Getters and setters.
     */
    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }

    public int getMaxPictures(){
        return 10;
    }

    protected abstract void takePicture();

    protected void doTakePicture(String filePath) {
        if (getPictureHolders().size() == getMaxPictures()){
            ToastUtil.show(this,getString(R.string.toast_max_picture,getMaxPictures()));
            return;
        }
        mCurrentPhotoPath = filePath;
        CaptureUtil.dispatchTakePictureIntent(this, filePath, TAKE_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {


            Bitmap oldBitmap = ImageUtil.getBitmapFromSize(mCurrentPhotoPath, 600, 600);
            //添加水印
            ImageUtil.waterMarkBitmap(mCurrentPhotoPath, oldBitmap, "");
            //获取缩略图
            Bitmap thumbnailBitmap = ImageUtil.getThumbnailBitmap(mCurrentPhotoPath, 200);

            PictureHolder pictureHolder = new PictureHolder();
            pictureHolder.setBitmap(thumbnailBitmap);
            pictureHolder.setFilePath(mCurrentPhotoPath);
            pictureHolder.setUri("file://" + mCurrentPhotoPath);

            imageLayoutAdpater.addPictureHolder(pictureHolder);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ImageLayoutAdapter implements WeightGridLayout.GridAdapter, View.OnClickListener {

        private CameraActivity context;
        private WeightGridLayout layoutView;

        private boolean isDeleteMode;

        public void setIsDeleteMode(boolean isDeleteMode) {
            this.isDeleteMode = isDeleteMode;
        }

        private List<PictureHolder> datas = new ArrayList<>();

        public ImageLayoutAdapter(CameraActivity context, WeightGridLayout view) {
            this.context = context;
            this.layoutView = view;
        }

        public List<PictureHolder> getDatas() {
            return datas;
        }

        public void addPictureHolder(PictureHolder pictureHolder) {
            datas.add(pictureHolder);
            layoutView.notifyDataSetChanged();
        }

        public void deletePictureHOlder(PictureHolder pictureHolder) {
            datas.remove(pictureHolder);
            layoutView.notifyDataSetChanged();
        }

        @Override
        public View getView(int index) {
            View view = null;
            LayoutInflater inflater = LayoutInflater.from(context);
            if (index == datas.size()) {
                //添加视图
                view = inflater.inflate(R.layout.add_picture_layout, null);
                view.findViewById(R.id.add_image_btn).setOnClickListener(this);
            } else if (index == datas.size() + 1) {
                //删除视图
                view = inflater.inflate(R.layout.delete_picture_layout, null);
                view.findViewById(R.id.delete_image_btn).setOnClickListener(this);
            } else {
                PictureHolder pictureHolder = datas.get(index);
                view = inflater.inflate(R.layout.takepic_layout, null, false);
                ImageView deleteFlagView = (ImageView) view.findViewById(R.id.delete_image);
                deleteFlagView.setOnClickListener(this);
                deleteFlagView.setTag(pictureHolder);
                if (isDeleteMode) {
                    deleteFlagView.setVisibility(View.VISIBLE);
                } else {
                    deleteFlagView.setVisibility(View.GONE);
                }
                final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                imageView.setTag(index + 1);
                imageView.setOnClickListener(this);
                imageView.setImageBitmap(pictureHolder.getBitmap());
                imageView.setBackgroundColor(getResources().getColor(R.color.gray));

            }
            return view;
        }

        @Override
        public int getCount() {
            return datas.size() + 2;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.add_image_btn) {
                this.isDeleteMode = false;
                context.takePicture();
                //添加
            } else if (v.getId() == R.id.delete_image_btn) {
                //显示删除按钮
                this.isDeleteMode = !this.isDeleteMode;
                layoutView.notifyDataSetChanged();
            } else if (v.getId() == R.id.delete_image) {

                deletePictureHOlder((PictureHolder) v.getTag());
                if (datas.size() == 0) {
                    this.isDeleteMode = false;
                }
                layoutView.notifyDataSetChanged();
            }else if (v.getId() == R.id.image){
                Intent intent = new Intent();
                intent.setClass(CameraActivity.this, ImageShowActivity.class);

                String[] uris = new String[datas.size()];
                for (int i = 0; i < datas.size(); i++) {
                    uris[i] = datas.get(i).getFilePath();
                }

                intent.putExtra(ImageShowActivity.Extra.IMAGES, uris);
                intent.putExtra(ImageShowActivity.Extra.IMAGE_POSITION, (Integer) v.getTag());

                startActivity(intent);
            }
        }
    }

    public class PictureHolder {
        String uri;
        String fileName;
        String filePath;

        Bitmap bitmap;
        String mimeType;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
    }
}