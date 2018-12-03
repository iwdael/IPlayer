# IPlayer
[![](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/language-java-yellow.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/jcenter-1.2.2--beta4-brightgreen.svg)](http://jcenter.bintray.com/com/hacknife/iplayer) [![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/api-19+-green.svg)](https://github.com/hacknife)<br/><br/>
爱播支持列表播放、全屏播放、重力自动旋转、清晰度切换、播放引擎切换、自定义主题颜色等。
![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203194535.gif)![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203194658.gif)![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203194919.gif)
![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203195154.gif)![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203195313.gif)![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203195513.gif)
![](https://github.com/hacknife/IPlayer/blob/master/ScreenCapture/G_1203195848.gif)
## 特点
* 全屏重力感应自动旋转
* 清晰度切换(高清/超清/蓝光)
* 自定义播放引擎(ijkPlayer/ExoPlayer/MediaPlayer)
* 视频列表播放(ListView/RecyclerView)
* 自定义主题颜色
* 可拖动窗口播放
* 拖动调节音量、亮度
* 支持Https视频源
## 属性说明
|属性|功能|值|默认状态|
|:------:|:------:|:------:|:------:|
|enableTitleBar|是否启用标题栏|boolean|是|
|enableBottomBar|是否启用控制栏|boolean|是|
|enableBottomProgressBar|是否启用底部进度条|boolean|是|
|enableEnlarge|是否启用全屏按钮|boolean|是| 
|enableClarity|非全屏模式下是否启用播放源切换|boolean|是|
|enableShowWifiDialog|非wifi条件下播放视频是否提示用户|boolean|是|
|enableCache|是否启用缓存|boolean|否|
|enableTinyWindow|视频列表中是否启用小窗口播放|boolean|否|
|tinyWindowWidth|小窗口宽度|dimension|默认为普通窗口的2/5|
|tinyWindowHeight|小窗口高度|dimension|默认为普通窗口的2/5|
|screenType|普通窗口中，视频内容显示的方式|adapter/fillCrop/fillParent/original|adapter|
|screenTypeFull|全屏窗口中，视频内容显示的方式|adapter/fillCrop/fillParent/original|adapter|
|screenTypeTiny|小窗口中，视频内容显示的方式|adapter/fillCrop/fillParent/original|adapter|
|orientationFullScreen|全屏模式中，Activity的方向|vertical/horizontal|重力感应自动旋转|
## 快速引入项目
需要使用的module中添加引用
```
dependencies {
    implementation 'com.hacknife:iplayer:1.2.2-bate3'
    //可选
    implementation 'com.hacknife.ijkplayer:ijkplayer-java:0.8.8'
    implementation 'com.hacknife.ijkplayer:ijkplayer-exo:0.8.8'
    implementation 'com.hacknife.ijkplayer:ijkplayer-armv7a:0.8.8'
    implementation 'com.hacknife.ijkplayer:ijkplayer-armv5:0.8.8'
    implementation 'com.hacknife.ijkplayer:ijkplayer-arm64:0.8.8'
    implementation 'com.hacknife.ijkplayer:ijkplayer-86:0.8.8'
    implementation 'com.hacknife.ijkplayer:ijkplayer-86_64:0.8.8'
}
```
## 使用说明
### Step1.配置播放器主题色
在APP主题中添加如下属性
```
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="iplayer_primary_color">#00aeff</item>
    </style>
```
### Step2.设置封面加载器
实现ImageLoader中的方法
```
public class CoverLoader implements ImageLoader {
    @Override
    public void onLoadCover(ImageView cover, String coverUrl) {
        Glide.with(cover).load(coverUrl).into(cover);
    }
}
```
### Step3
设置图片加载器和添加爱播生命周期管理器，播放引擎可选，如果不设置默认使用系统自带的MediaPlayer
```
    Player.setPlayerEngine(new IjkEngine());
    Player.setImageLoader(new CoverLoader());
    registerActivityLifecycleCallbacks(new PlayerLifecycleCallbacks());
```
