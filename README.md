# SofaPlay

SofaPlay 是一个 Android TV 播放器，目标是把 Emby 媒体库、ForwardWidget 插件生态和多播放内核组合成一套适合电视操作的观看体验。

## 当前状态

仓库处于 Phase 0：已提供 Android TV 工程骨架、核心模型、播放器抽象、Emby 接口、ForwardWidget 兼容模型和完整架构设计。

## 核心能力

- 支持 mpv、libVLC、ExoPlayer/Media3 三内核。
- 支持 Emby 媒体库挂载与播放进度同步。
- 完整设计 ForwardWidget JS 插件兼容层。
- 首页、详情页、资源匹配均由 Provider 组合。
- 支持外部/内置/在线字幕和弹幕扩展。

## 文档

- 架构：`docs/ARCHITECTURE.md`
- Emby 接入：`docs/EMBY-INTEGRATION.md`
- 竞品分析：`docs/MARKET-ANALYSIS.md`
- Forward 兼容：`docs/FORWARD-COMPATIBILITY.md`
- 路线图：`docs/ROADMAP.md`

## 开发环境

使用 Android Studio Ladybug 或更新版本，JDK 17，AGP 8.10.1。第一次打开后让 Android Studio 生成 Gradle Wrapper，或运行：

```bash
gradle wrapper --gradle-version 8.11.1
./gradlew :app:assembleDebug
```
