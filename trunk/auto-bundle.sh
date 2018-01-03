#!/bin/bash
# 2016/12/27	liuyao	First release

echo "开始"
echo "react native版本更新"
cd ../toutiao-rn/ReactNative 
svn update
cd ../../toutiao/ReactNative
rm -rf ./*
cp  -r ../../toutiao-rn/ReactNative/* ./ 
cd ../../toutiao
echo "准备资源"
rm -rf bundle
rm app/src/main/assets/android0.zip
mkdir bundle
echo "打包中..."
react-native bundle --platform android  --dev false --entry-file index.android.js --bundle-output ./bundle/index.android.bundle --assets-dest ./bundle/
zip -r app/src/main/assets/android0.zip bundle
echo "打包完成"


