package org.androidtown.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class MainActivity extends AppCompatActivity {
    private int userfont;
    private float userTextSize;

    private EditText et;
    private Bitmap bmp;
    private ImageView iv;
    private ImageView Iv[];
    private Typeface[] tf = new Typeface[7];
    private Button btn;
    private ListPopupWindow list;
    private TextView jamo;
    private String[] font = {"나눔", "나눔바른고딕", "나눔바른고딕볼드", "나눔바른펜", "나눔핸드브러시","나눔명조-옛한글", "나눔펜"};
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
        userfont = 0;
        userTextSize = 48;
        //init UI
        jamo = (TextView)findViewById(R.id.jamo);
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


        iv = (ImageView) findViewById(R.id.imageView);
        iv.setVisibility(View.INVISIBLE);
        et = (EditText) findViewById(R.id.edittext);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);

                    //iv.setImageBitmap(TexttoBitmap(et.getText().toString().substring(et.getText().toString().length() - 1,et.getText().toString().length()), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                    initIv();
                    jamo();
                }
                else {
                    iv.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public Bitmap TexttoBitmap(String text, float textSize, int textColor, Typeface tf) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(tf);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
    public void fontswitch(int position)
    {
        switch(position){
            case 0:
                userfont = 0;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
                break;
            case 1:
                userfont = 1;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
                break;
            case 2:
                userfont = 2;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
                break;
            case 3:
                userfont = 3;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
                break;
            case 4:
                userfont = 4;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
                break;
            case 5:
                userfont = 5;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
                break;
            case 6:
                userfont = 6;
                if(et.getText().toString().length()>0) {
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
                }else
                    iv.setVisibility(View.INVISIBLE);
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
        iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
    }

    public void fontsizedownClicked(View v)
    {
        if(userTextSize > 4)
            userTextSize-=4;
        iv.setImageBitmap(TexttoBitmap(et.getText().toString(), userTextSize, et.getCurrentTextColor(), tf[userfont]));
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

    public void jamo()
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
        jamo.setText(result);
    }

    public void initIv()
    {
        Iv = new ImageView[et.getText().toString().length()];
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < et.getText().toString().length(); i++)
        {
            Iv[i] = new ImageView(this);
            Iv[i].setImageBitmap(TexttoBitmap(et.getText().toString().substring(i,i+1), userTextSize, et.getCurrentTextColor(), tf[userfont]));
            rl.addView(Iv[i], lp);
        }
    }
}