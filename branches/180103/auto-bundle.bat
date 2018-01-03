echo "begin..."
echo "react native update..."
cd ../toutiao-rn/ReactNative
svn update
cd ../../toutiao/ReactNative
del /s/q *.*
xcopy  /s ..\..\toutiao-rn\ReactNative .\
cd ../../toutiao
echo "delete old bundle and android0.zip"
rmdir /s/q  bundle
del  app\src\main\assets\android0.zip
mkdir bundle
echo "bundle..."
call react-native-bundle.bat
wzzip -rP app/src/main/assets/android0.zip ./bundle
echo "bundle success"
