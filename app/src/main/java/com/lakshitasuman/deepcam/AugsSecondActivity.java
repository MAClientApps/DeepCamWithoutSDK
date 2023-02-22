package com.lakshitasuman.deepcam;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import org.wysaid.common.Common;
import org.wysaid.myUtils.ImageUtil;
import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGEImageHandler;
import org.wysaid.view.ImageGLSurfaceView;

import java.util.Random;

import static com.lakshitasuman.deepcam.AugsMainActivity.EFFECT_CONFIGS;

import vocsy.ads.GoogleAds;

/**
 * Created by Admin on 9/5/2017.
 */

public class AugsSecondActivity extends AppCompatActivity {
    protected static final String BASIC_FILTER_CONFIG = "@adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0";
    protected static final String BASIC_FILTER_NAMES[] = {
            "brightness",
            "contrast",
            "saturation",
            "sharpen"
    };

    Animation slideUp;
    Animation slideDown;
    private String mCurrentConfig;
    TextView view1, view2, view3, view4, view5;
    Bitmap mBitmap;
    private ImageGLSurfaceView mImageView;
    private SeekBar mSeekBar;
    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    Button colse, yes, gallry;
    RelativeLayout effectll;
    Button filter1, filter2, filter3, filter4, filter5;
    RecyclerView holist;
    LinearLayoutManager layoutManager;
    int scrollDy = 0;
    int effectPos = 0;
    AugsRecy_adapter option_adapter;
    Button britness, contrest, saltration, sharpen;


    int randamcount;
    int number = 0;

    class AdjustConfig {
        public int index;
        public float intensity, slierIntensity = 0.5f;
        public float minValue, originValue, maxValue;

        public AdjustConfig(int _index, float _minValue, float _originValue, float _maxValue) {
            index = _index;
            minValue = _minValue;
            originValue = _originValue;
            maxValue = _maxValue;
            intensity = _originValue;
        }

        protected float calcIntensity(float _intensity) {
            float result;
            if (_intensity <= 0.0f) {
                result = minValue;
            } else if (_intensity >= 1.0f) {
                result = maxValue;
            } else if (_intensity <= 0.5f) {
                result = minValue + (originValue - minValue) * _intensity * 2.0f;
            } else {
                result = maxValue + (originValue - maxValue) * (1.0f - _intensity) * 2.0f;
            }
            return result;
        }

        //_intensity range: [0.0, 1.0], 0.5 for the origin.
        public void setIntensity(float _intensity, boolean shouldProcess) {
            if (mImageView != null) {
                slierIntensity = _intensity;
                intensity = calcIntensity(_intensity);
                mImageView.setFilterIntensityForIndex(intensity, index, shouldProcess);
            }
        }
    }

    AdjustConfig mActiveConfig = null;

    public void setActiveConfig(AdjustConfig config) {
        mActiveConfig = config;
        mSeekBar.setProgress((int) (config.slierIntensity * mSeekBar.getMax()));
    }

    AdjustConfig mAdjustConfigs[] = {
            new AdjustConfig(0, -1.0f, 0.0f, 1.0f), //brightness
            new AdjustConfig(1, 0.1f, 1.0f, 3.0f), //contrast
            new AdjustConfig(2, 0.0f, 1.0f, 3.0f), //saturation
            new AdjustConfig(3, -1.0f, 0.0f, 10.0f) //sharpen
    };


//    public static class MyButton extends AppCompatButton implements View.OnClickListener {
//
//        AdjustConfig mConfig;
//        AugsSecondActivity mImageDemoActivity;
//
//        public MyButton(AugsSecondActivity context, AdjustConfig config) {
//            super(context);
//            mImageDemoActivity = context;
//            mConfig = config;
//            setOnClickListener(this);
//            setAllCaps(false);
//        }
//
//        @Override
//        public void onClick(View v) {
//
//            mImageDemoActivity.setActiveConfig(mConfig);
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        GoogleAds.getInstance().admobBanner(this,findViewById(R.id.banner));

        randamcount = randInt(6, 8);

        filter1 = (Button) findViewById(R.id.filter1);
        filter2 = (Button) findViewById(R.id.filter2);
        filter4 = (Button) findViewById(R.id.filter4);
        filter3 = (Button) findViewById(R.id.filter3);
        filter5 = (Button) findViewById(R.id.filter5);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
//        view1,view2,view3,view4,view5

        view1 = (TextView) findViewById(R.id.view1);
        view2 = (TextView) findViewById(R.id.view2);
        view3 = (TextView) findViewById(R.id.view3);
        view4 = (TextView) findViewById(R.id.view4);
        view5 = (TextView) findViewById(R.id.view5);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

//        view1.setVisibility(View.GONE);
//        view2.setVisibility(View.GONE);
//        view3.setVisibility(View.GONE);
//        view4.setVisibility(View.GONE);
//        view5.setVisibility(View.GONE);

//        britness,contrest,saltration,sharpen
        britness = (Button) findViewById(R.id.britness);
        contrest = (Button) findViewById(R.id.contrest);
        saltration = (Button) findViewById(R.id.saltration);
        sharpen = (Button) findViewById(R.id.sharpen);
        gallry = (Button) findViewById(R.id.gallry);

        gallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_IMAGE);
            }
        });

        britness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myadfunction();
                AdjustConfig mConfig1 = mAdjustConfigs[0];
                mActiveConfig = mConfig1;
                mSeekBar.setProgress((int) (mConfig1.slierIntensity * mSeekBar.getMax()));
                view1.setTextColor(getResources().getColor(R.color.colorPrimary));
                view2.setTextColor(getResources().getColor(R.color.colorAccent));
                view3.setTextColor(getResources().getColor(R.color.colorPrimary));
                view4.setTextColor(getResources().getColor(R.color.colorPrimary));
                view5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        contrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myadfunction();
                AdjustConfig mConfig1 = mAdjustConfigs[1];
                mActiveConfig = mConfig1;
                mSeekBar.setProgress((int) (mConfig1.slierIntensity * mSeekBar.getMax()));
                view1.setTextColor(getResources().getColor(R.color.colorPrimary));
                view2.setTextColor(getResources().getColor(R.color.colorPrimary));
                view3.setTextColor(getResources().getColor(R.color.colorAccent));
                view4.setTextColor(getResources().getColor(R.color.colorPrimary));
                view5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        saltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myadfunction();
                AdjustConfig mConfig1 = mAdjustConfigs[2];
                mActiveConfig = mConfig1;
                mSeekBar.setProgress((int) (mConfig1.slierIntensity * mSeekBar.getMax()));
                view1.setTextColor(getResources().getColor(R.color.colorPrimary));
                view2.setTextColor(getResources().getColor(R.color.colorPrimary));
                view3.setTextColor(getResources().getColor(R.color.colorPrimary));
                view4.setTextColor(getResources().getColor(R.color.colorAccent));
                view5.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        sharpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myadfunction();
                AdjustConfig mConfig1 = mAdjustConfigs[3];
                mActiveConfig = mConfig1;
                mSeekBar.setProgress((int) (mConfig1.slierIntensity * mSeekBar.getMax()));
                view1.setTextColor(getResources().getColor(R.color.colorPrimary));
                view2.setTextColor(getResources().getColor(R.color.colorPrimary));
                view3.setTextColor(getResources().getColor(R.color.colorPrimary));
                view4.setTextColor(getResources().getColor(R.color.colorPrimary));
                view5.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });


        colse = (Button) findViewById(R.id.close);
        yes = (Button) findViewById(R.id.yes);

        effectll = (RelativeLayout) findViewById(R.id.effectll);
        colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);
                effectll.startAnimation(slideDown);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        effectll.setVisibility(View.GONE);
                    }
                }, 500);


            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mImageView.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
                    @Override
                    public void get(Bitmap bmp) {
                        mImageView.setImageBitmap(bmp);
                    }
                });

                mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);

                effectll.startAnimation(slideDown);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        effectll.setVisibility(View.GONE);
                    }
                }, 500);


            }
        });
        mImageView = (ImageGLSurfaceView) findViewById(R.id.mainImageView);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.appicon);

//        mImageView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
//            @Override
//            public void surfaceCreated() {
//               mImageView.setImageBitmap(mBitmap);
//                mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);
//            }
//        });

        mImageView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);

        mImageView.setZOrderOnTop(true);
        Intent in = getIntent();
        if (in.getStringExtra("pathint").equals("0")) {

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inPreferredConfig = Bitmap.Config.ARGB_8888;

            mBitmap = BitmapFactory.decodeFile(in.getStringExtra("path"));

            mImageView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
                @Override
                public void surfaceCreated() {
                    mImageView.setImageBitmap(mBitmap);
                    mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);
                }
            });

        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_IMAGE);

        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mActiveConfig != null) {
                    float intensity = progress / (float) seekBar.getMax();
                    mActiveConfig.setIntensity(intensity, true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = progress / 100.0f;
                mImageView.setFilterIntensity(intensity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
//
//        if (BASIC_FILTER_NAMES.length == mAdjustConfigs.length) {
//
//            for (int i = 0; i != BASIC_FILTER_NAMES.length; ++i) {
//                MyButton btn = new MyButton(this, mAdjustConfigs[i]);
////                btn.setText(BASIC_FILTER_NAMES[i]);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        75, 75);
//                params.setMargins(5, 5, 5, 5);
//                btn.setLayoutParams(params);
//                if (i == 0) {
//                    btn.setBackground(AugsSecondActivity.this.getResources().getDrawable(R.drawable.brit));
//                } else if (i == 1) {
//                    btn.setBackground(AugsSecondActivity.this.getResources().getDrawable(R.drawable.contrast));
//                } else if (i == 2) {
//                    btn.setBackground(AugsSecondActivity.this.getResources().getDrawable(R.drawable.salu));
//                } else if (i == 3) {
//                    btn.setBackground(AugsSecondActivity.this.getResources().getDrawable(R.drawable.sharpen));
//                }
//
//
//                menuLayout.addView(btn);
//            }
//        } else {
//            Log.e(Common.LOG_TAG, "Invalid config num!");
//        }


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

//                mImageView.setFilterWithConfig(EFFECT_CONFIGS[position]);
//
//                Log.e("filter click", AugsMainActivity.EFFECT_CONFIGS[position]);
//                mCurrentConfig = AugsMainActivity.EFFECT_CONFIGS[position];

                myadfunction();
                effectPos = position;


                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsSecondActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = position * 100;
                }
                mCurrentConfig = EFFECT_CONFIGS[effectPos];
                mImageView.setFilterWithConfig(mCurrentConfig);

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
                myadfunction();
                layoutManager.scrollToPosition(0);
                scrollDy = 0;


                effectPos = 0;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsSecondActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }

                if (effectPos == 0) {
                    scrollDy = effectPos * 100;
                }

                mImageView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
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
                myadfunction();
                effectPos = 21;
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsSecondActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }


                mImageView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
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
                effectPos = 41;
                myadfunction();
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsSecondActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }


                mImageView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
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
                effectPos = 61;
                myadfunction();
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsSecondActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }


                mImageView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
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
                effectPos = 81;
                myadfunction();
                Log.e("posy", "" + effectPos);
                option_adapter = new AugsRecy_adapter(AugsSecondActivity.this, EFFECT_CONFIGS, effectPos);
                holist.setAdapter(option_adapter);

                if (effectPos > 3) {
                    layoutManager.scrollToPosition(effectPos - 2);
                    scrollDy = effectPos * 100;
                }
                mImageView.setFilterWithConfig(EFFECT_CONFIGS[effectPos]);
                mCurrentConfig = EFFECT_CONFIGS[effectPos];
                filter1.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter2.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter3.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter4.setTextColor(getResources().getColor(R.color.colorPrimary));
                filter5.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });


    }


    public void effectClick(View view) {

        effectll.setVisibility(View.VISIBLE);
        effectll.startAnimation(slideUp);

    }

    String s;

    public void saveImageBtnClicked(View view) {
        mImageView.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
            @Override
            public void get(Bitmap bmp) {
                s = ImageUtil.saveBitmap(bmp);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + s)));
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        GoogleAds.getInstance().showCounterInterstitialAd(AugsSecondActivity.this, () -> {
                            Intent aa = new Intent(AugsSecondActivity.this, AugsSaveActivity.class);
                            aa.putExtra("pathh", s);
                            startActivity(aa);
                        });

                    }
                });
            }
        });

    }

    static int displayIndex;

    public void switchDisplayMode(View view) {

        ImageGLSurfaceView.DisplayMode[] modes = {
                ImageGLSurfaceView.DisplayMode.DISPLAY_SCALE_TO_FILL,
                ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FILL,
                ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT,

        };

        mImageView.setDisplayMode(modes[++displayIndex % modes.length]);
    }

    public void resetBtnClicked(View view) {

        GoogleAds.getInstance().showCounterInterstitialAd(AugsSecondActivity.this, () -> {

        });

        mImageView.queueEvent(new Runnable() {
            @Override
            public void run() {

                CGEImageHandler handler = mImageView.getImageHandler();
                for (AdjustConfig config : mAdjustConfigs) {

                    handler.setFilterIntensityAtIndex(config.originValue, config.index, false);
                }

                handler.revertImage();
                handler.processFilters();
                mImageView.requestRender();
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.i(Common.LOG_TAG, "Filter Demo2 onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.i(Common.LOG_TAG, "Filter Demo2 onPause...");
        super.onPause();
        mImageView.release();
        mImageView.onPause();
    }

    @Override
    public void onResume() {
        Log.i(Common.LOG_TAG, "Filter Demo2 onResume...");
        super.onResume();
        mImageView.onResume();
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        Log.e("data", "" + data);

                        Uri uri = data.getData();
                        Log.e("uri", "" + uri);

                        String a = getRealPathFromURI(this, uri);
//                        Uri uri1 = Uri.parse("file://" + a);
                        Log.e("uri", "" + a);
//                        Bitmap bmp = BitmapFactory.decodeFile(a);

                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bmp = BitmapFactory.decodeFile(a);

                        Log.e("bmp", "" + bmp);
//
                        int w = bmp.getWidth();
                        int h = bmp.getHeight();
                        float s = Math.max(w / 2048.0f, h / 2048.0f);

                        if (s > 1.0f) {
                            w /= s;
                            h /= s;
                            mBitmap = Bitmap.createScaledBitmap(bmp, w, h, false);
                        } else {
                            mBitmap = bmp;
                        }


                        mImageView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
                            @Override
                            public void surfaceCreated() {
                                mImageView.setImageBitmap(mBitmap);
                                mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);
                            }
                        });

                        GoogleAds.getInstance().showCounterInterstitialAd(AugsSecondActivity.this, () -> {

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("uri", "" + e);
                        MsgUtil.toastMsg(this, "Error: Can not open image");
                    }

                }
                break;
            default:
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void myadfunction() {

        number++;


        if (number == randamcount) {

            randamcount = randInt(10, 12);
            number = 0;
            GoogleAds.getInstance().showCounterInterstitialAd(this, () -> {
            });
        }
    }

    public  int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        //
        // In particular, do NOT do 'Random rand = new Random()' here or you
        // will get not very good / not very random results.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @Override
    public void onBackPressed() {
        GoogleAds.getInstance().showCounterInterstitialAd(this,()->{
            finish();
        });
    }
}



