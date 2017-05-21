package org.androidtown.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {
    public static int userfont = 0;
    public static float userTextSize = 80;
    public static int userTextColor = Color.BLACK;
    public static int userStrokeWidth = 10;
    public static int userStrokeColor = Color.RED;
    //
    private EditText et;
    private ImageView dragIv;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        dragIv = new ImageView(this);
        rl.addView(dragIv);
        dragIv.setVisibility(View.INVISIBLE);
        rl.setOnTouchListener(new View.OnTouchListener() {
            float x;
            float y;
            float width;
            float height;
            Bitmap bmp;
            public boolean onTouch(View v, MotionEvent ev){
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        x = ev.getX();
                        y = ev.getY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if(x < ev.getX() && y < ev.getY()){
                            dragIv.setVisibility(View.VISIBLE);
                            dragIv.setX(x); dragIv.setY(y);
                            width = ev.getX() - x;
                            height = ev.getY() - y;
                            bmp = createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bmp);
                            Paint paint = new Paint(ANTI_ALIAS_FLAG);
                            paint.setColor(Color.BLUE);
                            paint.setAlpha(30);
                            canvas.drawRect(0,0,width,height, paint);
                            dragIv.setImageBitmap(bmp);
                        }
                        else if(x < ev.getX() && y > ev.getY()){
                            dragIv.setVisibility(View.VISIBLE);
                            dragIv.setX(x); dragIv.setY(ev.getY());
                            width = ev.getX() - x;
                            height = y - ev.getY();
                            bmp = createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bmp);
                            Paint paint = new Paint(ANTI_ALIAS_FLAG);
                            paint.setColor(Color.BLUE);
                            paint.setAlpha(30);
                            canvas.drawRect(0,0,width,height, paint);
                            dragIv.setImageBitmap(bmp);
                        }
                        else if(x > ev.getX() && y > ev.getY()){
                            dragIv.setVisibility(View.VISIBLE);
                            dragIv.setX(ev.getX()); dragIv.setY(ev.getY());
                            width = x - ev.getX();
                            height = y - ev.getY();
                            bmp = createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bmp);
                            Paint paint = new Paint(ANTI_ALIAS_FLAG);
                            paint.setColor(Color.BLUE);
                            paint.setAlpha(30);
                            canvas.drawRect(0,0,width,height, paint);
                            dragIv.setImageBitmap(bmp);
                        }
                        else if(x > ev.getX() && y < ev.getY()){
                            dragIv.setVisibility(View.VISIBLE);
                            dragIv.setX(ev.getX()); dragIv.setY(y);
                            width = x - ev.getX();
                            height = ev.getY() - y;
                            bmp = createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bmp);
                            Paint paint = new Paint(ANTI_ALIAS_FLAG);
                            paint.setColor(Color.BLUE);
                            paint.setAlpha(30);
                            canvas.drawRect(0,0,width,height, paint);
                            dragIv.setImageBitmap(bmp);
                        }
                    }
                    break;
                    case MotionEvent.ACTION_CANCEL:
                        dragIv.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        dragIv.setVisibility(View.INVISIBLE);
                        selectList.clear();
                        for(int i = 0; i < 100; i++){
                            if(Iv[i].getVisibility() == View.VISIBLE && Iv[i].getX() > dragIv.getX() && Iv[i].getY() > dragIv.getY() && Iv[i].getX() + Iv[i].getWidth() < dragIv.getX() + width && Iv[i].getY() + Iv[i].getHeight() < dragIv.getY() + height){
                                selectList.add(i);
                            }
                        }
                        //for(int i = 0; i < selectList.size(); i++){
                            //Iv[selectList.get(i)].setVisibility(View.INVISIBLE);
                        //}
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
                    initIv(jamo());
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

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);

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
                    initIv(jamo());
                }
                break;
            case 1:
                userfont = 1;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(jamo());
                }
                break;
            case 2:
                userfont = 2;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(jamo());
                }
                break;
            case 3:
                userfont = 3;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(jamo());
                }
                break;
            case 4:
                userfont = 4;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(jamo());
                }
                break;
            case 5:
                userfont = 5;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(jamo());
                }
                break;
            case 6:
                userfont = 6;
                if(et.getText().toString().length() > 0)
                {
                    for(int i = 0; i < selectList.size(); i++)
                        iinfo[selectList.get(i)].setTfNum(userfont);
                    initIv(jamo());
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
                iinfo[selectList.get(i)].setTextSize(userTextSize);
            }
            initIv(jamo());
        }
    }

    public void fontsizedownClicked(View v)
    {
        if(userTextSize > 4)
            userTextSize-=4;
        if(et.getText().toString().length() > 0)
        {
            for(int i = 0; i < selectList.size(); i++){
                iinfo[selectList.get(i)].setTextSize(userTextSize);
            }
            initIv(jamo());
        }
    }

    public void saveButtonClicked(View v)
    {
        File filepath = Environment.getExternalStorageDirectory();
        rl.setDrawingCacheEnabled(true);
        Bitmap bmp = rl.getDrawingCache();
        SaveBitmapToFileCache(bmp, filepath.getAbsolutePath(), "abcd.jpg");
    }

    public  void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename)
    {
        File file = new File(strFilePath);
        // If no folders
        if (!file.exists()) {
        if(file.mkdirs() == false)
            Toast.makeText(this, "failed mkdirs()", Toast.LENGTH_SHORT).show();
        }
        /*File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
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
}



