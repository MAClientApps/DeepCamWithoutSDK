package com.lakshitasuman.deepcam;

import static android.hardware.Camera.Parameters.FLASH_MODE_AUTO;
import static android.hardware.Camera.Parameters.FLASH_MODE_ON;
import static android.hardware.Camera.Parameters.FLASH_MODE_RED_EYE;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wysaid.camera.CameraInstance;
import org.wysaid.common.Common;
import org.wysaid.myUtils.ImageUtil;
import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.CameraRecordGLSurfaceView;

import java.io.IOException;
import java.io.InputStream;

import vocsy.ads.GoogleAds;

public class AugsMainActivity extends AppCompatActivity {
    private CameraRecordGLSurfaceView mCameraView;
    SeekBar seekBar;
    AugsRecy_adapter option_adapter;
    private String mCurrentConfig;
    Button switchBtn, flashBtn, gallary;
    Button filter1, filter2, filter3, filter4, filter5;
    RecyclerView holist;
    LinearLayoutManager layoutManager;
    int scrollDy = 0;
    private static final String DEBUG_TAG = "Velocity";
    int effectPos = 0;
    private VelocityTracker mVelocityTracker = null;
    View rr;
    int flashIndex = 0;


    String[] flashModes1 = {
            "AUTO",
            "ON",
            "OFF",
            "TORCH",
            "RED_EYE",
    };


    String[] flashModes = {
            FLASH_MODE_AUTO,
            FLASH_MODE_ON,
            Camera.Parameters.FLASH_MODE_OFF,
            Camera.Parameters.FLASH_MODE_TORCH,
            FLASH_MODE_RED_EYE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
        rr = (View) findViewById(R.id.rr);

        rr.setOnTouchListener(new View.OnTouchListener() {

            int x, y;
            int chageEffectX = 0, chageEffectY = 0;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.e("onTouch", "onTouch");

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        chageEffectX = (int) event.getX();
                        chageEffectY = (int) event.getY();

                        Log.e("chageEffectX", "" + chageEffectX);
                        Log.e("chageEffectY", "" + chageEffectY);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:

                        x = (int) event.getX();
                        y = (int) event.getY();
                        Log.e("x", "" + x);
                        Log.e("y", "" + y);

                        if (x < chageEffectX && chageEffectX - x > 200) {

                            if (effectPos != 104) {
                                effectPos += 1;

                                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                                holist.setAdapter(option_adapter);

                                if (effectPos > 3) {
                                    layoutManager.scrollToPosition(effectPos - 2);
                                    scrollDy = effectPos * 100;
                                }

                                if (effectPos < 20) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));

                                } else if (effectPos < 40) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else if (effectPos < 60) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else if (effectPos < 80) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorAccent));
                                }

                                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                                mCurrentConfig = EFFECT_CONFIGS[effectPos];
                            }
                        } else if (x > chageEffectX && x - chageEffectX > 200) {

                            Log.e("possss", "" + effectPos);
                            if (effectPos != 0) {
                                effectPos -= 1;

                                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                                holist.setAdapter(option_adapter);

                                if (effectPos > 3) {
                                    layoutManager.scrollToPosition(effectPos - 2);
                                    scrollDy = effectPos * 100;
                                }

                                Log.e("dy", "" + effectPos);
                                if (effectPos < 20) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));

                                } else if (effectPos < 40) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else if (effectPos < 60) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else if (effectPos < 80) {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorAccent));
                                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                                } else {
                                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    filter5.setTextColor(getResources().getColor(R.color.colorAccent));
                                }


                                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                                mCurrentConfig = EFFECT_CONFIGS[effectPos];
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // Return a VelocityTracker object back to be re-used by others.


                        break;
                }
                return true;
            }
        });

        mCameraView = (CameraRecordGLSurfaceView) findViewById(R.id.myGLSurfaceView);
        mCameraView.presetCameraForward(false);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        layout = (LinearLayout) findViewById(R.id.menuLinearLayout);
        filter1 = (Button) findViewById(R.id.filter1);
        filter2 = (Button) findViewById(R.id.filter2);
        filter4 = (Button) findViewById(R.id.filter4);
        filter3 = (Button) findViewById(R.id.filter3);
        filter5 = (Button) findViewById(R.id.filter5);
        holist = (RecyclerView) findViewById(R.id.holist);

        Log.e("size", "" + EFFECT_CONFIGS.length);


        option_adapter = new AugsRecy_adapter(this, EFFECT_CONFIGS, effectPos);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        holist.setHasFixedSize(true);
        holist.setLayoutManager(layoutManager);
        holist.setItemAnimator(new DefaultItemAnimator());
        holist.setAdapter(option_adapter);

        holist.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                effectPos = position;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = position * 100;
                }


                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];
            }


        }));

//        layoutManager.scrollToPosition(80);
        holist.setOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                scrollDy += dx;

                int pos = (int) scrollDy / 100;
                Log.e("scroll", "aavu" + pos);

                Log.e("dy", "" + pos);
                if (pos < 20) {

                    filter1.setTextColor(getResources().getColor(R.color.colorAccent));
                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else if (pos < 40) {
                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter2.setTextColor(getResources().getColor(R.color.colorAccent));
                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (pos < 60) {
                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter3.setTextColor(getResources().getColor(R.color.colorAccent));
                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (pos < 80) {
                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter4.setTextColor(getResources().getColor(R.color.colorAccent));
                    filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filter5.setTextColor(getResources().getColor(R.color.colorAccent));
                }


            }

        });

        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                scrollDy = 0;
                effectPos = 0;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }

                if (effectPos == 0) {
                    scrollDy = effectPos * 100;
                }

                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];

                filter1.setTextColor(getResources().getColor(R.color.colorAccent));
                filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                scrollDy = 2100;


                effectPos = 21;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }


                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];


                filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter2.setTextColor(getResources().getColor(R.color.colorAccent));
                filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                scrollDy = 4100;


                effectPos = 41;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }


                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];


                filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter3.setTextColor(getResources().getColor(R.color.colorAccent));
                filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        filter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                scrollDy = 6100;

                effectPos = 61;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }


                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];


                filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter4.setTextColor(getResources().getColor(R.color.colorAccent));
                filter5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        filter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                scrollDy = 8100;


                effectPos = 81;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsMainActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }
                mCameraView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];


                filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter5.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        switchBtn = (Button) findViewById(R.id.switchCameraBtn);
        switchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                mCameraView.switchCamera();
            }
        });

        gallary = (Button) findViewById(R.id.gallary);

        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AugsMainActivity.this, AugsSecondActivity.class);
                in.putExtra("pathint", "1");
                in.putExtra("path", "nothig");
                startActivity(in);

            }
        });
        flashBtn = (Button) findViewById(R.id.flashBtn);


        flashBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if (flashIndex == 0) {
                    flashBtn.setBackground(getResources().getDrawable(R.drawable.flashauto));
                } else if (flashIndex == 1) {
                    flashBtn.setBackground(getResources().getDrawable(R.drawable.flashon));
                } else if (flashIndex == 2) {
                    flashBtn.setBackground(getResources().getDrawable(R.drawable.flashoff));
                } else if (flashIndex == 3) {
                    flashBtn.setBackground(getResources().getDrawable(R.drawable.touch));
                } else if (flashIndex == 4) {
                    flashBtn.setBackground(getResources().getDrawable(R.drawable.redeye));
                }


                mCameraView.setFlashLightMode(flashModes[flashIndex]);
//                flashBtn.setText(flashModes1[flashIndex]);
                ++flashIndex;
                flashIndex %= flashModes.length;
            }
        });

        mCameraView.setFlashLightMode(flashModes[flashIndex]);
        ++flashIndex;
        flashIndex %= flashModes.length;


        final Button takePicBtn = (Button) findViewById(R.id.takePicBtn);
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showText("Taking Picture...");
                takePicBtn.setBackground(getResources().getDrawable(R.drawable.swiatch));
                mCameraView.takePicture(new CameraRecordGLSurfaceView.TakePictureCallback() {
                    @Override
                    public void takePictureOK(Bitmap bmp) {
                        if (bmp != null) {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    takePicBtn.setBackground(getResources().getDrawable(R.drawable.switchtack));

                                }
                            }, 700);


                            String s = ImageUtil.saveBitmap(bmp);
                            Log.e("pathtack", "" + s);
                            bmp.recycle();
//                            showText("Take picture success!");
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + s)));

                            Intent in = new Intent(AugsMainActivity.this, AugsSecondActivity.class);
                            in.putExtra("pathint", "0");
                            in.putExtra("path", s);
                            startActivity(in);

                            GoogleAds.getInstance().showCounterInterstitialAd(AugsMainActivity.this, () -> {

                            });


                        } else {
                            Toast.makeText(AugsMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            showText("Take picture failed!");
                        }
                    }
                }, null, mCurrentConfig, 1.0f, true);
            }
        });


//        for (int i = 0; i != AugsMainActivity.EFFECT_CONFIGS.length; ++i) {
//            MyButtons button = new MyButtons(this, AugsMainActivity.EFFECT_CONFIGS[i]);
//            button.setAllCaps(false);
//            if (i == 0)
//                button.setText("None");
//            else
//                button.setText("F" + i);
//            try {
//                Drawable d = Drawable.createFromStream(getAssets().open("filter/filter" + i + ".jpg"), null);
//                button.setBackground(d);
//            } catch (Exception e) {
//                Log.e("Exception", "" + e);
//            }
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    100, 100);
//            params.setMargins(2, 2, 2, 2);
//            button.setLayoutParams(params);
//
//            button.setOnClickListener(mFilterSwitchListener);
//            layout.addView(button);
//        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = progress / 100.0f;
                mCameraView.setFilterIntensity(intensity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar.setProgress(90);
        mCameraView.setFilterIntensity(90 / 100.0f);

        mCameraView.presetRecordingSize(1440, 1920);
//        mCameraView.presetRecordingSize(720, 1280);
        //Taking picture size.
//        mCameraView.setPictureSize(2048, 2048, true); // > 4MP
        mCameraView.setZOrderOnTop(false);
        mCameraView.setZOrderMediaOverlay(true);

        mCameraView.setOnCreateCallback(new CameraRecordGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver() {

            }


        });

        mCameraView.setPictureSize(1440, 1920, true);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        CameraInstance.getInstance().stopCamera();
        Log.i("AugsMainActivity", "activity onPause...");
        mCameraView.release(null);
        mCameraView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mCameraView.onResume();
    }


    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: 1.jpg
        //注意， 这里回传的name不包含任何路径名， 仅为具体的图片文件名如 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            Log.i(Common.LOG_TAG, "Loading file: " + name);
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open(name);
            } catch (IOException e) {
                Log.e(Common.LOG_TAG, "Can not open file " + name);
                return null;
            }

            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            Log.i(Common.LOG_TAG, "Loading bitmap over, you can choose to recycle or cache");

            //The bitmap is which you returned at 'loadImage'.
            //You can call recycle when this function is called, or just keep it for further usage.
            //唯一不需要马上recycle的应用场景为 多个不同的滤镜都使用到相同的bitmap
            //那么可以选择缓存起来。
            bmp.recycle();
        }
    };
    public static final String EFFECT_CONFIGS[] = {
            "",
            "@curve RGB(0,255)(255,0) @style mapping0.jpg 80 80 8 3", // ASCII art (字符画效果)
            "@beautify face 1 480 640", //Beautify
            "@adjust lut edgy_amber.png",
            "@adjust lut filmstock.png",
            "@adjust lut foggy_night.png",
            "@adjust lut late_sunset.png",
            "@adjust lut soft_warming.png",
            "@adjust lut wildbird.png",
            "#unpack @blur lerp 0.75", //can adjust blur intensity
            "@blur lerp 1", //can adjust blur mix
            "#unpack @dynamic wave 1", //can adjust speed
            "@dynamic wave 0.5",       //can adjust wave mix
            "#unpack @style sketch 0.9",
            "#unpack @krblend sr hehe.jpg 100 ",
            "#unpack @krblend ol hehe.jpg 100",
            "#unpack @krblend add hehe.jpg 100",
            "#unpack @krblend darken hehe.jpg 100",
            "@beautify bilateral 100 3.5 2 ",
            "@style crosshatch 0.01 0.003 ",
            "@style edge 1 2 ",
            "@style edge 1 2 @curve RGB(0, 255)(255, 0) ",
            "@style edge 1 2 @curve RGB(0, 255)(255, 0) @adjust saturation 0 @adjust level 0.33 0.71 0.93 ",
            "@adjust level 0.31 0.54 0.13 ",
            "#unpack @style emboss 1 2 2 ",
            "@style halftone 1.2 ",
            "@vigblend overlay 255 0 0 255 100 0.12 0.54 0.5 0.5 3 ",
            "@curve R(0, 0)(63, 101)(200, 84)(255, 255)G(0, 0)(86, 49)(180, 183)(255, 255)B(0, 0)(19, 17)(66, 41)(97, 92)(137, 156)(194, 211)(255, 255)RGB(0, 0)(82, 36)(160, 183)(255, 255) ",
            "@adjust exposure 0.98 ",
            "@adjust shadowhighlight -200 200 ",
            "@adjust sharpen 10 1.5 ",
            "@adjust colorbalance 0.99 0.52 -0.31 ",
            "@adjust level 0.66 0.23 0.44 ",
            "@style min",
            "@style max",
            "@style haze 0.5 -0.14 1 0.8 1 ",
            "@curve R(0, 0)(71, 74)(164, 165)(255, 255) @pixblend screen 0.94118 0.29 0.29 1 20",//415
            "@curve G(0, 0)(144, 166)(255, 255) @pixblend screen 0.94118 0.29 0.29 1 20",//416
            "@curve B(0, 0)(68, 72)(149, 184)(255, 255) @pixblend screen 0.94118 0.29 0.29 1 20",//417
            "@curve R(0, 0)(71, 74)(164, 165)(255, 255) @pixblend overlay 0.357 0.863 0.882 1 40",//418
            "@curve R(0, 0)(96, 61)(154, 177)(255, 255) @pixblend overlay 0.357 0.863 0.882 1 40",//419
            "@curve R(0, 0)(152, 183)(255, 255)G(0, 0)(161, 133)(255, 255) @pixblend overlay 0.357 0.863 0.882 1 40",//420
            "@curve R(0, 0)(149, 145)(255, 255)G(0, 0)(149, 145)(255, 255)B(0, 0)(149, 145)(255, 255) @pixblend colordodge 0.937 0.482 0.835 1 20",//421
            "@curve G(0, 0)(101, 127)(255, 255) @pixblend colordodge 0.937 0.482 0.835 1 20",//422
            "@curve B(0, 0)(70, 87)(140, 191)(255, 255) @pixblend pinlight 0.247 0.49 0.894 1 20",//423
            "@adjust saturation 0.7 @pixblend screen 0.8112 0.243 1 1 40",//425
            "@adjust saturation 0.7 @pixblend screen 1 0.243 0.69 1 30",//426

            "@curve R(0, 0)(71, 74)(164, 165)(255, 255) @pixblend screen 0.94118 0.29 0.29 1 20",//415
            "@curve G(0, 0)(144, 166)(255, 255) @pixblend screen 0.94118 0.29 0.29 1 20",//416
            "@curve B(0, 0)(68, 72)(149, 184)(255, 255) @pixblend screen 0.94118 0.29 0.29 1 20",//417
            "@curve R(0, 0)(71, 74)(164, 165)(255, 255) @pixblend overlay 0.357 0.863 0.882 1 40",//418
            "@curve R(0, 0)(96, 61)(154, 177)(255, 255) @pixblend overlay 0.357 0.863 0.882 1 40",//419
            "@curve R(0, 0)(152, 183)(255, 255)G(0, 0)(161, 133)(255, 255) @pixblend overlay 0.357 0.863 0.882 1 40",//420
            "@curve R(0, 0)(149, 145)(255, 255)G(0, 0)(149, 145)(255, 255)B(0, 0)(149, 145)(255, 255) @pixblend colordodge 0.937 0.482 0.835 1 20",//421
            "@curve G(0, 0)(101, 127)(255, 255) @pixblend colordodge 0.937 0.482 0.835 1 20",//422
            "@curve B(0, 0)(70, 87)(140, 191)(255, 255) @pixblend pinlight 0.247 0.49 0.894 1 20",//423
            "@adjust saturation 0.7 @pixblend screen 0.8112 0.243 1 1 40",//425
            "@adjust saturation 0.7 @pixblend screen 1 0.243 0.69 1 30",//426

            "@curve R(0, 0)(117, 95)(155, 171)(179, 225)(255, 255)G(0, 0)(94, 66)(155, 176)(255, 255)B(0, 0)(48, 59)(141, 130)(255, 224)",//5
            "@curve R(0, 0)(69, 63)(105, 138)(151, 222)(255, 255)G(0, 0)(67, 51)(135, 191)(255, 255)B(0, 0)(86, 76)(150, 212)(255, 255)",//6
            "@curve R(0, 0)(43, 77)(56, 104)(100, 166)(255, 255)G(0, 0)(35, 53)(255, 255)B(0, 0)(110, 123)(255, 212)",//7
            "@curve R(0, 0)(35, 71)(153, 197)(255, 255)G(0, 15)(16, 36)(109, 132)(255, 255)B(0, 23)(181, 194)(255, 230)",//8
            "@curve R(15, 0)(92, 133)(255, 234)G(0, 20)(105, 128)(255, 255)B(0, 0)(120, 132)(255, 214)",//9
            "@curve R(0, 4)(255, 244)G(0, 0)(255, 255)B(0, 84)(255, 194)",//10
            "@curve R(48, 56)(82, 129)(130, 206)(214, 255)G(7, 37)(64, 111)(140, 190)(232, 220)B(2, 97)(114, 153)(229, 172)",//11
            "@curve R(39, 0)(93, 61)(130, 136)(162, 193)(208, 255)G(41, 0)(92, 61)(128, 133)(164, 197)(200, 250)B(0, 23)(125, 127)(255, 230)",//12
            "@curve R(40, 162)(108, 186)(142, 208)(193, 227)(239, 249)G(13, 7)(72, 87)(124, 150)(197, 206)(255, 255)B(8, 22)(57, 97)(112, 147)(184, 204)(255, 222)",//13
            "@curve R(18, 0)(67, 63)(104, 152)(128, 255)G(23, 4)(87, 106)(132, 251)B(17, 0)(67, 63)(108, 174)(128, 251)",//14
            "@curve R(5, 49)(85, 173)(184, 249)G(23, 35)(65, 76)(129, 145)(255, 199)B(74, 69)(158, 107)(255, 126)",//15
            "@adjust hsv -0.7 -0.7 0.5 -0.7 -0.7 0.5 @pixblend ol 0.243 0.07059 0.59215 1 25",//17
            "@adjust hsv -0.7 0.5 -0.7 -0.7 -0.7 0.5 @pixblend ol 0.07059 0.60391 0.57254 1 25",//18
            "@adjust hsv -0.7 0.5 -0.7 -0.7 0 0 @pixblend ol 0.2941 0.55292 0.06665 1 25",//19
            "@adjust hsv -0.8 0 -0.8 -0.8 0.5 -0.8 @pixblend ol 0.78036 0.70978 0.09018 1 28",//20

            "@adjust hsv -0.4 -0.64 -1.0 -0.4 -0.88 -0.88 @curve R(0, 0)(119, 160)(255, 255)G(0, 0)(83, 65)(163, 170)(255, 255)B(0, 0)(147, 131)(255, 255)",//22
            "@adjust hsv -0.5 -0.5 -0.5 -0.5 -0.5 -0.5 @curve R(0, 0)(129, 148)(255, 255)G(0, 0)(92, 77)(175, 189)(255, 255)B(0, 0)(163, 144)(255, 255)",//23
            "@adjust hsv 0.3 -0.5 -0.3 0 0.35 -0.2 @curve R(0, 0)(111, 163)(255, 255)G(0, 0)(72, 56)(155, 190)(255, 255)B(0, 0)(103, 70)(212, 244)(255, 255)",//24
            "@curve R(40, 40)(86, 148)(255, 255)G(0, 28)(67, 140)(142, 214)(255, 255)B(0, 100)(103, 176)(195, 174)(255, 255) @adjust hsv 0.32 0 -0.5 -0.2 0 -0.4",//25
            "@curve R(4, 35)(65, 82)(117, 148)(153, 208)(206, 255)G(13, 5)(74, 78)(109, 144)(156, 201)(250, 250)B(6, 37)(93, 104)(163, 184)(238, 222)(255, 237) @adjust hsv -0.2 -0.2 -0.44 -0.2 -0.2 -0.2",//26
            "@adjust hsv -1 -1 -1 -1 -1 -1",//27
            "@colormul mat 0.34 0.48 0.22 0.34 0.48 0.22 0.34 0.48 0.22 @curve R(0, 29)(20, 48)(83, 103)(164, 166)(255, 239)G(0, 30)(30, 61)(66, 94)(151, 160)(255, 241)B(2, 48)(82, 93)(166, 143)(255, 199)",//119
            "@colormul mat 0.34 0.48 0.22 0.34 0.48 0.22 0.34 0.48 0.22 @curve R(0, 0)(9, 10)(47, 38)(87, 69)(114, 92)(134, 116)(175, 167)(218, 218)(255, 255)G(40, 0)(45, 14)(58, 34)(74, 55)(125, 118)(192, 205)(255, 255)B(0, 0)(15, 16)(37, 31)(71, 55)(108, 88)(159, 151)(204, 201)(255, 255)",//120
            "@curve R(3, 0)(23, 29)(83, 116)(167, 206)(255, 255)G(5, 0)(56, 64)(160, 189)(255, 255)B(3, 0)(48, 49)(142, 167)(248, 255)",//160
            "@curve R(15, 0)(45, 37)(92, 103)(230, 255)G(19, 0)(34, 22)(138, 158)(228, 252)B(19, 0)(74, 63)(159, 166)(230, 255)",//161
            "@curve R(0, 4)(39, 103)(134, 223)(242, 255)G(0, 3)(31, 85)(68, 155)(131, 255)(219, 255)B(0, 3)(42, 110)(114, 207)(255, 255)",//162
            "@curve R(17, 0)(37, 18)(75, 52)(238, 255)G(16, 0)(53, 32)(113, 92)(236, 255)B(16, 0)(80, 57)(171, 164)(235, 255)",//163
            "@curve R(33, 0)(70, 32)(146, 143)(185, 204)(255, 255)G(22, 0)(103, 71)(189, 219)(255, 252)B(10, 0)(54, 29)(93, 66)(205, 220)(255, 255)",//164
            "@curve R(4, 4)(38, 38)(146, 146)(201, 202)(255, 255)G(0, 0)(80, 74)(192, 187)(255, 255)B(0, 0)(58, 58)(183, 184)(255, 255)",//165
            "@curve R(5, 8)(36, 51)(115, 145)(201, 220)(255, 255)G(6, 9)(67, 83)(169, 190)(255, 255)B(3, 3)(55, 60)(177, 190)(255, 255)",//166
            "@curve R(14, 0)(51, 42)(135, 138)(191, 202)(234, 255)G(11, 6)(78, 77)(178, 185)(242, 250)B(11, 0)(22, 10)(72, 60)(171, 162)(217, 209)(255, 255)",//167
            "@curve R(9, 0)(26, 7)(155, 108)(194, 159)(255, 253)G(9, 0)(50, 19)(218, 194)(255, 255)B(0, 0)(29, 9)(162, 116)(218, 194)(255, 255)",//168
            "@curve R(0, 0)(69, 93)(126, 160)(210, 232)(255, 255)G(0, 0)(36, 47)(135, 169)(250, 254)B(0, 0)(28, 30)(107, 137)(147, 206)(255, 255)",//169
            "@curve R(2, 2)(16, 30)(72, 112)(135, 185)(252, 255)G(2, 1)(30, 42)(55, 84)(157, 207)(238, 249)B(1, 0)(26, 17)(67, 106)(114, 165)(231, 250)",//170
            "@curve R(16, 0)(60, 45)(124, 124)(214, 255)G(18, 2)(91, 81)(156, 169)(213, 255)B(16, 0)(85, 74)(158, 171)(211, 255) @curve R(17, 0)(144, 150)(214, 255)G(16, 0)(61, 47)(160, 172)(215, 255)B(21, 2)(131, 135)(213, 255)",//171
            "@curve R(0, 0)(120, 96)(165, 255)G(90, 0)(131, 145)(172, 255)B(77, 0)(165, 167)(255, 255)",//172
            "@curve R(9, 0)(49, 62)(124, 155)(218, 255)G(10, 0)(30, 33)(137, 169)(223, 255)B(10, 0)(37, 45)(96, 122)(150, 182)(221, 255)",//173
            "@curve R(81, 3)(161, 129)(232, 253)G(91, 0)(164, 136)(255, 225)B(76, 0)(196, 162)(255, 225)",//174
            "@curve R(0, 0)(135, 147)(255, 255)G(0, 0)(135, 147)(255, 255)B(0, 0)(135, 147)(255, 255)  @adjust saturation 0.71 @adjust brightness -0.05 @curve R(19, 0)(45, 36)(88, 90)(130, 125)(200, 170)(255, 255)G(18, 0)(39, 26)(71, 74)(147, 160)(255, 255)B(0, 0)(77, 58)(136, 132)(255, 204)",//300
            "@adjust saturation 0 @curve R(9, 13)(37, 13)(63, 23)(81, 43)(91, 58)(103, 103)(159, 239)(252, 242)G(3, 20)(29, 20)(56, 19)(77, 37)(107, 108)(126, 184)(137, 217)(150, 248)(182, 284)(255, 255)B(45, 17)(78, 51)(96, 103)(131, 202)(255, 255)",//301
            "@curve R(42, 2)(53, 52)(80, 102)(100, 123)(189, 196)(255, 255)G(55, 74)(75, 98)(95, 114)(177, 197)(203, 212)(221, 220)(229, 234)(240, 249)B(0, 132)(81, 188)(180, 251)",//303
            "@adjust saturation 0 @curve R(0, 68)(10, 72)(42, 135)(72, 177)(98, 201)(220, 255)G(0, 29)(12, 30)(57, 127)(119, 203)(212, 255)(254, 239)B(0, 36)(54, 118)(66, 141)(119, 197)(155, 215)(255, 254)",//304
            "@curve R(0, 64)(16, 13)(58, 128)(108, 109)(162, 223)(255, 255)G(0, 30)(22, 35)(42, 58)(56, 86)(70, 119)(130, 184)(189, 212)B(6, 36)(76, 157)(107, 192)(173, 229)(255, 255)", //306
            "@vigblend mix 10 10 30 255 91 0 1.0 0.5 0.5 3 @curve R(0, 31)(35, 75)(81, 139)(109, 174)(148, 207)(255, 255)G(0, 24)(59, 88)(105, 146)(130, 171)(145, 187)(180, 214)(255, 255)B(0, 96)(63, 130)(103, 157)(169, 194)(255, 255)",
            "@adjust saturation 0 @curve R(0, 49)(16, 44)(34, 56)(74, 120)(120, 185)(151, 223)(255, 255)G(0, 46)(34, 73)(85, 129)(111, 164)(138, 192)(170, 215)(255, 255)B(0, 77)(51, 101)(105, 143)(165, 182)(210, 213)(250, 229)",
            "@adjust saturation 0 @adjust level 0 0.83921 0.8772",
            "@adjust hsl 0.02 -0.31 -0.17 @curve R(0, 28)(23, 45)(117, 148)(135, 162)G(0, 8)(131, 152)(255, 255)B(0, 17)(58, 80)(132, 131)(127, 131)(255, 225)"
    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            mCameraView.takePicture(new CameraRecordGLSurfaceView.TakePictureCallback() {
                @Override
                public void takePictureOK(Bitmap bmp) {
                    if (bmp != null) {
                        String s = ImageUtil.saveBitmap(bmp);
                        Log.e("pathtack", "" + s);
                        bmp.recycle();
//                            showText("Take picture success!");
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + s)));

                        Intent in = new Intent(AugsMainActivity.this, AugsSecondActivity.class);
                        in.putExtra("pathint", "0");
                        in.putExtra("path", s);
                        startActivity(in);


                    } else {
//                            showText("Take picture failed!");
                    }
                }
            }, null, mCurrentConfig, 1.0f, true);

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
