package org.androidtown.image;

//import android.app.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.androidtown.image.R.id.imageView;

//public class MainActivity extends AppCompatActivity {

    //  @Override
    //protected void onCreate(Bundle savedInstanceState) {
    //  super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_main);
    //}

//}
public class HeeActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private static final String TAG = "AppPermission";
    final int REQ_CODE_SELECT_IMAGE = 100;
    private Bitmap mBitmap;
    public int check;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hejee);
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
    public void onButton1Clicked(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
        check = 1;
    }
    public void onButton2Clicked(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
        check = 2;
    }


@RequiresApi(api = Build.VERSION_CODES.M)
public void onButton3Clicked(View v) {
    checkPermission();
    View b = findViewById(R.id.button1);
    b.setVisibility(View.GONE);
    View b2 = findViewById(R.id.button2);
    b2.setVisibility(View.GONE);
    View b3 = findViewById(R.id.button3);
    b3.setVisibility(View.GONE);
    View rootView = getWindow().getDecorView();
    File screenShot = ScreenShot(rootView);
    if(screenShot!=null){
        //갤러리에 추가
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));

    }
    }

    public File ScreenShot(View view){

        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환
        //ImageView image4 = (ImageView) findViewById(R.id.imageView2);
      //  image4.setImageBitmap(screenBitmap);
        String filename = "screenshot.jpg";
        File file = new File("/mnt/sdcard", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }





protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

    if (requestCode == REQ_CODE_SELECT_IMAGE) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                //Uri에서 이미지 이름을 얻어온다.
               // String name_Str = getImageNameToUri(data.getData());

                //이미지 데이터를 비트맵으로 받아온다.

                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                int sW = image_bitmap.getWidth();
                int sH = image_bitmap.getHeight();
                if(check == 2) {
                    int[] pixels = new int[sW * sH];
                    image_bitmap.getPixels(pixels, 0, sW, 0, 0, sW, sH);
                    for (int i = 0; i < pixels.length; i++) {
                        if (pixels[i] == Color.WHITE)
                            pixels[i] = Color.TRANSPARENT;
                    }
                    Bitmap b = Bitmap.createBitmap(pixels, 0, sW, sW, sH, Bitmap.Config.ARGB_8888);

                    ImageView image = (ImageView) findViewById(R.id.imageView3);
                    image.setOnTouchListener(new View.OnTouchListener() {
                        float x;
                        float y;
                        public boolean onTouch(View v, MotionEvent ev){
                            switch (ev.getAction()) {
                                case MotionEvent.ACTION_DOWN: {
                                    x = v.getX() - ev.getRawX(); // 손으로누른좌표랑 이미지왼쪽위좌표값의 차이값
                                    y = v.getY() - ev.getRawY();
                                    break;
                                }
                                case MotionEvent.ACTION_MOVE: {
                                    v.animate().x(ev.getRawX() + x).y(ev.getRawY() + y).setDuration(0).start(); // 이동시켜주는함수.
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
                    // Toast toast2 = Toast.makeText(this,"되라고 썅",Toast.LENGTH_LONG);
                    //toast2.show();
                    // Toast toast = Toast.makeText(getBaseContext(),String.format("%d", mBitmap.getPixel(100,100)),Toast.LENGTH_LONG);
                    //  toast.show();
                    //배치해놓은 ImageView에 set

                    image.setImageBitmap(b);
                }
                else {
                    ImageView image2 = (ImageView) findViewById(imageView);
                    image2.setImageBitmap(image_bitmap);
                }
              // image.setColorFilter(Color.argb(0, 255, 0, 0));
            //   mBitmap = BitmapFactory.decodeResource(getResources(), );
              //  setContentView(new BitmapView(this));

                //toast.show();
                //Toast.makeText(getBaseContext(), "name_Str : " + name_Str, Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public String getImageNameToUri(Uri data) {
    String[] proj = {MediaStore.Images.Media.DATA};
    Cursor cursor = managedQuery(data, proj, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    String imgPath = cursor.getString(column_index);
    String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
    return imgName;
}
public class BitmapView extends View {
    private Bitmap mViewBitmap;
    private int[] mColors;
    private Paint mPaint;

    private int mWidth = 0;
    private int mHeight = 0;
    private int mST = 0;

    public BitmapView(Context context){
        super(context);
        setFocusable(true);
        mColors = createColors();
        int[] colors = mColors;

        mViewBitmap = Bitmap.createBitmap(colors, 0, mST, mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mPaint = new Paint();
        mPaint.setDither(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int winWidth = getWidth();
        int winHeight = getHeight();
        // 이미지 그리기
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mViewBitmap, winWidth / 2 - mWidth, winHeight / 2 - mHeight, null);
        canvas.translate(0, mViewBitmap.getHeight());

        super.onDraw(canvas);
    }

    private int[] createColors(){
        int[] colors = null;
        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();
        mST = mWidth + 10; // 넓이보다 10을 크게 설정

        colors = new int[mST * mHeight];
        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                colors[y * mST + x] = mBitmap.getPixel(x, y);
            }
        }

        return colors;
    }
}

}

