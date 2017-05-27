package org.androidtown.image;


import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Jongwon on 2017-05-27.
 */

public class ScreenString {
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

    public String before = "";
    public String after = "";
    private String divedText = "";
    private int defIndex;
    public ArrayList<Integer> divIndexList  = new ArrayList<>();
    public ScreenString(){}

    public String setText()
    {
        divedText = "";
        after = MainActivity.et.getText().toString();
        if(before.length() == after.length()) {

        }
        else if(before.length() < after.length()){
            for(defIndex = 0; defIndex < before.length(); defIndex++)
                if(before.charAt(defIndex) != after.charAt(defIndex))
                    break;
            for(int i =  0; i < divIndexList.size(); i++)
                if(divIndexList.get(i) >= defIndex)
                    divIndexList.set(i, divIndexList.get(i) + 1);
        }
        else if(before.length() > after.length()){
            for(defIndex = 0; defIndex < after.length(); defIndex++)
                if(before.charAt(defIndex) != after.charAt(defIndex))
                    break;
            for(int i =  0; i < divIndexList.size(); i++){
                if(divIndexList.get(i) == defIndex)
                    divIndexList.remove(i);
                else if(divIndexList.get(i) > defIndex)
                    divIndexList.set(i, divIndexList.get(i) - 1);
            }
        }
        Collections.sort(divIndexList);
        int a,b,c;
        char ch;
        for(int i = 0; i < after.length(); i++) {
            ch = after.charAt(i);
            if (ch >= 0xAC00 && ch <= 0xD7A3 && check(i)) //한글이고 분해목록에 있으면
            {
                c = ch - 0xAC00;
                a = c / (21 * 28);
                c = c % (21 * 28);
                b = c / 28;
                c = c % 28;
                divedText = divedText + CHO[a] + JUN[b];
                if (c != 0)
                    divedText += JON[c];
            } else //한글이 아니면
                divedText += ch;
        }
        before = after;
        return divedText;
    }

    public void remove(int num)
    {
        for(int i = 0; i < divIndexList.size(); i++){
            if(num == divIndexList.get(i)) {
                divIndexList.remove(i);
                break;
            }
        }
    }

    public void add(int i){divIndexList.add(i);}

    public boolean check(int num){
        for(int i = 0; i < divIndexList.size(); i++){
            if(num == divIndexList.get(i))
                return true;
        }
        return false;
    }
}
