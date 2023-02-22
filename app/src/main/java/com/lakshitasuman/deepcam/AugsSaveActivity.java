package com.lakshitasuman.deepcam;

import static android.R.attr.x;
import static android.R.attr.y;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

import vocsy.ads.GoogleAds;

/**
 * Created by Admin on 9/8/2017.
 */

public class AugsSaveActivity extends AppCompatActivity {

    private ImageView mImageView;
    Bitmap mBitmap;
    Uri myUri;
    Button fb, insta, what, share, save, back;
    RelativeLayout fbll, install, whatll, sharell, savell, backll;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.save_activity);

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.banner));

        fb = (Button) findViewById(R.id.facebook);
        insta = (Button) findViewById(R.id.insta);
        what = (Button) findViewById(R.id.whatsup);
        share = (Button) findViewById(R.id.share);
        save = (Button) findViewById(R.id.save);
        back = (Button) findViewById(R.id.back);

        savell = (RelativeLayout) findViewById(R.id.savell);
        backll = (RelativeLayout) findViewById(R.id.backll);
        fbll = (RelativeLayout) findViewById(R.id.facebookll);
        install = (RelativeLayout) findViewById(R.id.install);
        whatll = (RelativeLayout) findViewById(R.id.whatsupll);
        sharell = (RelativeLayout) findViewById(R.id.sharell);
        mImageView = (ImageView) findViewById(R.id.mainImageView);
        Intent in = getIntent();
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inPreferredConfig = Bitmap.Config.ARGB_8888;
        path = in.getStringExtra("pathh");
        Log.e("path", path);
        mBitmap = BitmapFactory.decodeFile(path);
        Log.e("bitmap", "" + mBitmap);
        mImageView.setImageBitmap(mBitmap);
        myUri = Uri.parse(path);


        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touched down");
//                        savell.setBackgroundResource(R.drawable.allsahdoinside);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("TAG", "touched up");

                        saveImageBtnClicked();
//                        savell.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }
                return false;
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touched down");
//                        backll.setBackgroundResource(R.drawable.allsahdoinside);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("TAG", "touched up");

                        backk();
//                        backll.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }
                return false;
            }
        });


        fb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touched down");
                        fb.setBackgroundResource(R.drawable.fb1);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("TAG", "touched up");

                        facebook();
                        fb.setBackgroundResource(R.drawable.fb);
                        break;
                }
                return false;
            }
        });
//        fbll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i("TAG", "touched down");
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.i("TAG", "touched up");
//
//                        break;
//                }
//                return false;
//            }
//        });
        what.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touched down");
                        what.setBackgroundResource(R.drawable.what1);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("TAG", "touched up");
                        whatsup();
                        what.setBackgroundResource(R.drawable.what);
                        break;
                }
                return false;
            }
        });
//        whatll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i("TAG", "touched down");
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.i("TAG", "touched up");
//
//
//
//                        break;
//                }
//                return false;
//            }
//        });
        insta.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touched down");
                        insta.setBackgroundResource(R.drawable.insta1
                        );

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("TAG", "touched up");
                        insta.setBackgroundResource(R.drawable.insta);
                        instagram();
                        break;
                }
                return false;
            }
        });
//        install.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i("TAG", "touched down");
//                        install.setBackgroundResource(R.drawable.allsahdoinside);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        install.setBackgroundColor(getResources().getColor(R.color.white));
//                        break;
//                }
//                return false;
//            }
//        });
        share.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG", "touched down");
                        share.setBackgroundResource(R.drawable.share1);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("TAG", "touched up");
                        share();
                        share.setBackgroundResource(R.drawable.shre);
                        break;
                }
                return false;
            }
        });
//        sharell.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i("TAG", "touched down");
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.i("TAG", "touched up");
//
//                        break;
//                }
//                return false;
//            }
//        });
    }


    public void saveImageBtnClicked() {

//        String s;
//        s = ImageUtil.saveBitmap(mBitmap);
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + s)));


        GoogleAds.getInstance().showCounterInterstitialAd(this, () -> {
            Toast.makeText(this, "save image", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(AugsSaveActivity.this, AugsMainActivity.class));
        });
    }


    public void backk() {
        GoogleAds.getInstance().showCounterInterstitialAd(this, () -> {
            File fdelete = new File(myUri.getPath());
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + myUri.getPath());
                } else {
                    System.out.println("file not Deleted :" + myUri.getPath());
                }
            }
            finish();
            startActivity(new Intent(AugsSaveActivity.this, AugsMainActivity.class));
        });
    }

    public void facebook() {
        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(AugsSaveActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage("com.facebook.katana");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share image File"));

    }

    public void instagram() {

        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(AugsSaveActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage("com.instagram.android");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share image File"));

    }

    public void whatsup() {

        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(AugsSaveActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage("com.whatsapp");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share image File"));


    }

    public void share() {

        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(AugsSaveActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share image File"));
    }


    @Override
    public void onBackPressed() {
        File fdelete = new File(myUri.getPath());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + myUri.getPath());
            } else {
                System.out.println("file not Deleted :" + myUri.getPath());
            }
        }
        finish();
        startActivity(new Intent(AugsSaveActivity.this, AugsMainActivity.class));
        GoogleAds.getInstance().showCounterInterstitialAd(this, () -> {

        });
    }


}
