package com.chinaso.toutiao.util.charactorUtil;

import java.util.Locale;

/**
 * Created by chinaso on 2016/6/27.
 * 汉字、拼音转换
 */
public class GetLetter {
    private CharacterParser mCharacter;
    private String mName;

    public GetLetter(CharacterParser characterParser, String name) {
        mCharacter = characterParser;
        mName = name;

    }


    public String getSortLetter() {

        String letter = "#";
        if (mName == null) {
            return letter;
        }
        //汉字转换成拼音
        String pinyin = mCharacter.getSelling(mName);

        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }

        return letter;
    }


    /**
     * SortKey单字转换成拼音，针对数据库 COLLATE LOCALIZED ASC失效
     *
     * @return arr
     */
    public String[] getSortKeyLetter() {

        if (mName == null) {
            return null;
        }
        //汉字转换成拼音
        String[] arr = new String[mName.length()];
        for (int i = 0; i < mName.length(); i++) {
            arr[i] = mName.substring(i, i + 1);

            String pinyin = mCharacter.getSelling(arr[i]);
            arr[i] = pinyin;

        }

        return arr;
    }


}
