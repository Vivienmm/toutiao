package com.chinaso.toutiao.util.charactorUtil;



/**
 * Created by chinaso on 2016/6/27.
 * 将拼音分开存储，获得Key，并以SortToken的形式存储，即获得简拼、全拼
 */
public class ParseSortKey {
    private String sortKey;
    private CharacterParser mCharacter;

    public ParseSortKey(CharacterParser characterParser, String str) {
        sortKey = str;
        mCharacter = characterParser;
    }

    /**
     * 解析sort_key,封装简拼,全拼
     *针对通讯录名称
     * @return
     */
    public SortToken parseSortKey() {
        String chReg = "[\\u4E00-\\u9FA5]+";//中文字符串匹配

        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);

            //针对sortKey只有汉字的状况
            if (enStrs.length == 0) {
                enStrs = new GetLetter(mCharacter, sortKey).getSortKeyLetter();

            }

            for (String enStr : enStrs) {
                if (enStr.length() > 0) {
                    //拼接简拼
                    token.simpleSpell += enStr.charAt(0);
                    token.wholeSpell += enStr;
                }
            }
        }
        return token;
    }

    /**
     * 针对APP名字的转换
     *
     * @param name
     * @return
     */
    public SortToken parseApptoSortKey(String name) {

        SortToken token = new SortToken();
        for (int i = 0; i < name.length(); i++) {
            token.wholeSpell += mCharacter.getSelling(name.substring(i, i + 1));
            token.simpleSpell += mCharacter.getSelling(name.substring(i, i + 1)).charAt(0);
        }

        return token;
    }
}
