# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\project\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

########<--通用配置-->#########
-printmapping mapping.txt
-useuniqueclassmembernames
-allowaccessmodification

-dontnote com.google.vending.licensing.ILicensingService
-dontnote **ILicensingService
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn java.nio.file.*
####attributes
-keepattributes SourceFile,SourceDir,LineNumberTable
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepattributes EnclosingMethod
-keepattributes Exceptions, InnerClasses

####不混淆Serializable的子类，由于retrofit反射机制，这里配置和默认不同
-keep class * implements java.io.Serializable {*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

#### support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#### support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

#### enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#### jni
-keepclasseswithmembernames class * {
    native <methods>;
}
#### 自定义view
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keep public class * extends android.widget.* {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keep public class * extends android.webkit.* {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#### 自定义adapter
-keep public class * extends android.widget.BaseAdapter {*;}
-keep public class * extends android.support.v4.view.PagerAdapter {*;}

########<--头条-->#########
#### react native
-keep class com.facebook.**{*;}
-keep class com.chinaso.toutiao.rn.**{*;}
####data parse
-keep class org.codehaus.jackson.**{*;}
-dontwarn org.codehaus.jackson.**
-keep class com.google.gson.**{*;}
-dontwarn com.google.gson.**
-keep class org.json.**{*;}
-dontwarn org.json.**
####event-bus callback
-keepclassmembers class * {
    void onEventMainThread(***);
}
####retrofit
-keep class retrofit2.**{*;}
-dontwarn retrofit2.**
-dontnote retrofit2.**
####okthhp
-keep class org.apache.http.**{*;}
-dontwarn org.apache.http.**
-keep class okhttp3.**{*;}
-dontwarn okhttp3.**
-dontnote okhttp3.**
####picasso
-keep class com.squareup.picasso.**{*;}
-dontwarn com.squareup.picasso.**
#### 自定义entity
-keep public class com.chinaso.toutiao.app.entity.**{*;}
-keep public class com.chinaso.toutiao.mvp.entity.**{*;}
-keep public class com.chinaso.toutiao.mvp.data.**{*;}
#### web接口
-keepclassmembers class com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity$*{*;}
-keepclassmembers class com.chinaso.toutiao.mvp.ui.activity.QueryCommentActivity*{*;}
-keepclassmembers class com.chinaso.toutiao.mvp.ui.activity.CommonSearchResultActivity$*{*;}
-keepclassmembers class com.chinaso.toutiao.view.BaseWebView$*{*;}

########<--三方依赖-->#########
#### butterknife

#-keep class **$$ViewInjector { *; }
#
#-keepnames class * { @butterknife.InjectView *;}
#
#-dontwarn butterknife.Views$InjectViewProcessor
#
#-dontwarn com.gc.materialdesign.views.**
#
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#### tencentWB
-keep class com.tencent.weibo.sdk.android.**{*;}
-dontwarn com.tencent.weibo.sdk.android.**
#### sina
-keep class com.sina.weibo.sdk.net.**{*;}
-dontwarn com.sina.weibo.sdk.net.**
-keep class com.umeng.analytics.** {*;}
-dontwarn com.umeng.analytics.**

#### umeng analytics
-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.common.** { *; }
-keep class com.umeng.newxp.** { *; }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class com.umeng.**

-keep public class com.idea.fifaalarmclock.app.R$*{
    public static final int *;
}
-dontwarn com.umeng.**
-keep class org.apache.commons.**{*;}
-dontwarn org.apache.commons.**

-keep public class * extends com.umeng.**

-keep class com.umeng.** {*; }
#### Umeng Share
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**

-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.socialize.view.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep class javax.*
-keep public class android.webkit.* {*;}

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.view.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#### xiaomi Push
-keep class com.xiaomi.mipush.sdk.* {*;}
-keep class com.xiaomi.xmpush.thrift.* {*;}
-keep class com.xiaomi.push.service.* {*;}
-dontnote com.xiaomi.xmpush.thrift.*
-dontwarn com.xiaomi.push.service.XMPushService
-dontnote com.android.volley.*
#### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**

####  Rx
# If you do not use RxJava:
-dontwarn rx.**
#解决RxAndroid在6.0系统出现java.lang.InternalError
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
