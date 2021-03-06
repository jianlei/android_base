package com.daren.base.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * չʾͼƬ
 * <p/>
 * Created by wjl on 14-9-24.
 */
public class ImageShowActivity extends SystemBarTintActivity {

    private static final String STATE_POSITION = "STATE_POSITION";

    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        initSystemBar();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
        int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }


        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter(imageUrls));
        pager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private String[] images;
        private LayoutInflater inflater;

        ImagePagerAdapter(String[] images) {
            this.images = images;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.image_show_item, view, false);
            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            final TextView index = (TextView) imageLayout.findViewById(R.id.index);
            index.setText(images.length + " - " + (position + 1));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ImageShowActivity.this.finish();
                }
            });

            spinner.setVisibility(View.VISIBLE);

            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    spinner.setVisibility(View.GONE);
                }
            });
            Picasso picasso = builder.build();


            Uri uri = Uri.fromFile(new File(images[position]));
            RequestCreator rc;
            if(images[position].startsWith("http")){
                rc = picasso.with(ImageShowActivity.this).load(images[position]);
            }else {
                rc = picasso.with(ImageShowActivity.this).load(new File(images[position]));
            }

            rc.into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
                    mAttacher.update();
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    public static class Extra {
        public static final String IMAGES = "com.daren.zsyw.IMAGES";
        public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
    }
}
