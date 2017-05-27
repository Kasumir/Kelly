package org.androidtown.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;
import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ColorPickerDialog.OnColorChangedListener{
    private static final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private static final String TAG = "AppPermission";
    public static int userfont = 0;
    public static float userTextSize = 80;
    public static int userTextColor = Color.BLACK;
    public static int userStrokeWidth = 10;
    public static int userStrokeColor = Color.RED;
    private String text = "";
    //

    Button btn_colorPicker;
    int color;
    Button btn_capture;
    LinearLayout screen = null;
    public static EditText et;
    private Typeface[] tf = new Typeface[7];
    private Button btn;
    private ListPopupWindow list;
    private RelativeLayout rl;
    private String[] font = {"나눔", "나눔바른고딕", "나눔바른고딕볼드", "나눔바른펜", "나눔핸드브러시","나눔명조-옛한글", "나눔펜"};
    private int ivFocus;
    private int i;
    private ArrayList<Integer> selectList = new ArrayList<Integer>();
    private IvInfo[] iinfo = new IvInfo[100];
    private ImageView Iv[] = new ImageView[100];
    private DragView Dv;
    private ScreenString str = new ScreenString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (LinearLayout)findViewById(R.id.Screen);
        btn_capture = (Button)findViewById(R.id.btn_Capture);
        btn_colorPicker = (Button)findViewById(R.id.btn_colorPicker);
        btn_colorPicker.setOnClickListener(this);
        Dv = new DragView(this);
        //define font
            tf[0] = Typeface.createFromAsset(getAssets(), "NanumB.otf");
            tf[1] = Typeface.createFromAsset(getAssets(), "NanumBarunGothic.ttf");
            tf[2] = Typeface.createFromAsset(getAssets(), "NanumBarunGothicBold.ttf");
            tf[3] = Typeface.createFromAsset(getAssets(), "NanumBarunpen.otf");
            tf[4] = Typeface.createFromAsset(getAssets(), "Nanumhandbrush.ttf");
            tf[5] = Typeface.createFromAsset(getAssets(), "NanumMyeongjo-YetHangul.otf");
            tf[6] = Typeface.createFromAsset(getAssets(), "NanumPen.ttf");
        //init user input value
        //init UI
        rl = (RelativeLayout) findViewById(R.id.RL);
        rl.addView(Dv);
        Dv.setVisibility(View.INVISIBLE);
        for(i = 0; i < 100; i++){
            iinfo[i] = new IvInfo();
            Iv[i] = new ImageView(this);
            rl.addView(Iv[i]);
            Iv[i].setId(i+1);
            Iv[i].setVisibility(View.INVISIBLE);
            Iv[i].setOnTouchListener(new View.OnTouchListener() {
                float x;
                float y;
                public boolean onTouch(View v, MotionEvent ev){
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            selectList.clear();
                            selectList.add(v.getId() - 1);
                            for(int i = 0; i < 100; i++){
                                Iv[i].setBackgroundColor(Color.TRANSPARENT);
                                Iv[i].setAlpha((float)1);
                            }
                            for(int i = 0; i < selectList.size();i++){
                                Iv[selectList.get(i)].setBackgroundColor(Color.CYAN);
                                Iv[selectList.get(i)].setAlpha((float)0.5);
                            }
                            x = v.getX() - ev.getRawX(); // 손으로누른좌표랑 이미지왼쪽위좌표값의 차이값
                            y = v.getY() - ev.getRawY();
                            ivFocus = v.getId() - 1;
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            v.animate().x(ev.getRawX() + x).y(ev.getRawY() + y).setDuration(0).start(); // 이동시켜주는함수.
                            iinfo[ivFocus].setX(ev.getRawX() + x);
                            iinfo[ivFocus].setY(ev.getRawY() + y);
                        }
                        break;
                        case MotionEvent.ACTION_CANCEL: //터치모션 캔슬되었을 때. 아무것도안함.
                        case MotionEvent.ACTION_UP: // 손가락뗏을때. 아무것도안함.
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                checkPermission();
                rl.setDrawingCacheEnabled(true);
                Bitmap bm = rl.getDrawingCache();
                saveScreenImage(bm);
            }
        });
        rl.setOnTouchListener(new View.OnTouchListener(){
            float x, y;
            float width, height;
            public boolean onTouch(View v, MotionEvent ev){
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = ev.getX();
                        y = ev.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(x < ev.getX() && y < ev.getY()){
                            Dv.setVisibility(View.INVISIBLE);
                            Dv.setX(x); Dv.setY(y);
                            width = ev.getX() - x;
                            height = ev.getY() - y;
                            Dv.setWidth((int)width); Dv.setHeight((int)height);
                            Dv.setVisibility(View.VISIBLE);
                        }
                        else if(x < ev.getX() && y > ev.getY()){
                            Dv.setVisibility(View.INVISIBLE);
                            Dv.setX(x); Dv.setY(ev.getY());
                            width = ev.getX() - x;
                            height = y - ev.getY();
                            Dv.setWidth((int)width); Dv.setHeight((int)height);
                            Dv.setVisibility(View.VISIBLE);
                        }
                        else if(x > ev.getX() && y > ev.getY()){
                            Dv.setVisibility(View.INVISIBLE);
                            Dv.setX(ev.getX()); Dv.setY(ev.getY());
                            width = x - ev.getX();
                            height = y - ev.getY();
                            Dv.setWidth((int)width); Dv.setHeight((int)height);
                            Dv.setVisibility(View.VISIBLE);
                        }
                        else if(x > ev.getX() && y < ev.getY()){
                            Dv.setVisibility(View.INVISIBLE);
                            Dv.setX(ev.getX()); Dv.setY(y);
                            width = x - ev.getX();
                            height = ev.getY() - y;
                            Dv.setWidth((int)width); Dv.setHeight((int)height);
                            Dv.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Dv.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        Dv.setVisibility(View.INVISIBLE);
                        selectList.clear();
                        for(int i = 0; i < 100; i++){
                            if(Iv[i].getVisibility() == View.VISIBLE && Iv[i].getX() > Dv.getX() && Iv[i].getY() > Dv.getY() && Iv[i].getX() + Iv[i].getWidth() < Dv.getX() + width && Iv[i].getY() + Iv[i].getHeight() < Dv.getY() + height){
                                selectList.add(i);
                            }
                        }
                        for(int i = 0; i < 100; i++){
                            Iv[i].setBackgroundColor(Color.TRANSPARENT);
                            Iv[i].setAlpha((float)1);
                        }
                        for(int i = 0; i < selectList.size();i++){
                            Iv[selectList.get(i)].setBackgroundColor(Color.CYAN);
                            Iv[selectList.get(i)].setAlpha((float)0.5);
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        btn = (Button) findViewById(R.id.button);
        list = new ListPopupWindow(this);
        list.setWidth(300);
        list.setHeight(300);
        list.setAnchorView(btn);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, font));
        list.setModal(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                fontswitch(position);
            }
        });
        et = (EditText) findViewById(R.id.edittext);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et.getText().toString().length()>0)
                    initIv(str.setText());
                else
                    for(int i = 0; i < 100; i++)
                        Iv[i].setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public Bitmap TexttoBitmap(String text, int index) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(iinfo[index].getTextSize());
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(iinfo[index].getTextColor());
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(tf[iinfo[index].getTfNum()]);

        Paint strokePaint = new Paint(ANTI_ALIAS_FLAG);
        strokePaint.setTextSize(iinfo[index].getTextSize());
        strokePaint.setColor(iinfo[index].getStrokeColor());
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setTextAlign(Paint.Align.LEFT);
        strokePaint.setTypeface(tf[iinfo[index].getTfNum()]);
        strokePaint.setStrokeWidth(iinfo[index].getStrokeWidth());

        float baseline = -strokePaint.ascent(); // ascent() is negative
        int width = (int) (strokePaint.measureText(text) + 0.5f) + userStrokeWidth; // round
        int height = (int) (baseline + strokePaint.descent() + 0.5f) + userStrokeWidth;

        Bitmap image = createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        //canvas.translate(baseline,0);
        //canvas.rotate(userRotate,width / 2, height / 2);
        canvas.drawText(text,0,baseline, strokePaint);
        canvas.drawText(text, 0, baseline, paint);

        return image;
    }

    public void fontswitch(int position)
    {
        switch(position){
            case 0:
                userfont = 0;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
            case 1:
                userfont = 1;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
            case 2:
                userfont = 2;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
            case 3:
                userfont = 3;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
            case 4:
                userfont = 4;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
            case 5:
                userfont = 5;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
            case 6:
                userfont = 6;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(str.setText());
                }
                break;
        }
    }

    public void onbtnClicked(View v)
    {
        if(list.isShowing())
            list.dismiss();
        else
            list.show();
    }

    public void fontsizeupClicked(View v)
    {
        userTextSize+=4;
        if(et.getText().toString().length() > 0)
        {
            for(int i = 0; i < selectList.size(); i++){
                iinfo[selectList.get(i)].setTextSize(iinfo[selectList.get(i)].getTextSize() + 4);
            }
            initIv(str.setText());
        }
    }

    public void fontsizedownClicked(View v)
    {
        if(userTextSize > 4)
            userTextSize-=4;
        if(et.getText().toString().length() > 0)
        {
            for(int i = 0; i < selectList.size(); i++){
                iinfo[selectList.get(i)].setTextSize(iinfo[selectList.get(i)].getTextSize() - 4);
            }
            initIv(str.setText());
        }
    }

    public void onSeperateBtnClicked(View v){
        if(et.getText().toString().length() > 0) {
            for (int i = 0; i < selectList.size(); i++) {
                int j = selectList.get(i);
                for (int k = 0; k < str.divIndexList.size(); k++) {
                    if (str.divIndexList.get(k) < j) {
                        char ch = str.before.charAt(str.divIndexList.get(k));
                        if (ch >= 0xAC00 && ch <= 0xD7A3) //한글이고 분해목록에 있으면
                        {
                            int a, b, c;
                            c = ch - 0xAC00;
                            a = c / (21 * 28);
                            c = c % (21 * 28);
                            b = c / 28;
                            c = c % 28;
                            if (c == 0)
                                j -= 1;
                            else {
                                if (str.divIndexList.get(k) + 1 == j)
                                    j -= 1;
                                else
                                    j -= 2;
                            }
                        }
                    }
                }
                if (str.check(j))
                    str.remove(j);
                else
                    str.add(j);
            }
            initIv(str.setText());
        }
    }

    private static final char[] CHO =
		/*ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ */
            {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145,
                    0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};
    private static final char[] JUN =
		/*ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ*/
            {0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158,
                    0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160,	0x3161,	0x3162,
                    0x3163};
    /*X ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ*/
    private static final char[] JON =
            {0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a,
                    0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
                    0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

    public String jamo()  //editText의 내용을 자모분리한 문자열 리턴
    {
        String str = et.getText().toString();
        int a,b,c;
        String result = "";
        char ch;
        for(int i = 0; i < str.length(); i++)
        {
            ch = str.charAt(i);
            if(ch >= 0xAC00 && ch <= 0xD7A3) //한글이면 분해
            {
                c = ch - 0xAC00;
                a = c / (21 * 28);
                c = c % (21 * 28);
                b = c / 28;
                c = c % 28;

                result = result + CHO[a] + JUN[b];
                if(c != 0)
                    result += JON[c];
            }
            else //한글이 아니면
                result += ch;
        }
        return result;
    }

    public void initIv(String str)
    {
        for(int i = 0; i < 100; i++) {
            rl.removeView(Iv[i]);
            Iv[i].setVisibility(View.INVISIBLE);
        }
        for(int i = 0; i < str.length(); i++)         //모든 뷰에 id할당
        {
            Iv[i].setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams lpfirst = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpfirst.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.RL);
        lpfirst.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.RL);

        Iv[0].setImageBitmap(TexttoBitmap(str.substring(0, 1), 0));
        rl.addView(Iv[0], lpfirst);
        for(int i = 1; i < str.length(); i++)
        {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            Iv[i].setImageBitmap(TexttoBitmap(str.substring(i,i+1), i));
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.RL);
            lp.addRule(RelativeLayout.RIGHT_OF, Iv[i - 1].getId());
            rl.addView(Iv[i], lp);
        }
        for(int i = str.length(); i < 100; i++)
            rl.addView(Iv[i]);
    }

    @Override
    public void onClick(View v) {
        color = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.WHITE);
        new ColorPickerDialog(this, this, color).show();
    }

    @Override
    public void colorChanged(int color) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();
        userTextColor = color;
        if(et.getText().toString().length() > 0) {
            for (int i = 0; i < selectList.size(); i++) {
                iinfo[selectList.get(i)].setTextColor(userTextColor);
            }
            initIv(jamo());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            writeFile();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    writeFile();

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    Log.d(TAG, "Permission always deny");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }
    private void writeFile() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "temp.txt");
        try {
            Log.d(TAG, "create new File : " + file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveScreenImage(Bitmap bm){
        FileOutputStream stream;
        String path = "/mnt/sdcard/DCIM/" + "a.png";
        try{
            stream = new FileOutputStream(path);
            bm.compress(Bitmap.CompressFormat.PNG, 90,stream);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}



