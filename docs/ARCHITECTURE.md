# SofaPlay 架构设计

## 目标

SofaPlay 是面向 Android TV 的多内核、多来源、可扩展播放器：

- 三内核统一接入：ExoPlayer/Media3、mpv、libVLC。
- Emby 媒体库作为一等数据源。
- 完整兼容 ForwardWidget JS 模块协议，并扩展 Android TV 的焦点与遥控器交互。
- 首页、详情页、资源匹配、字幕和弹幕都以 Provider + Repository 方式组合。

## 分层

```text
app / feature-ui
  Compose TV screens, focus controllers, remote key handling

domain
  HomeComposer, DetailComposer, SourceMatcher, PlaybackCoordinator

data
  EmbyProvider, ForwardPluginProvider, StremioProvider, LocalProvider

plugin
  QuickJS runtime, ForwardWidget bridge, sandbox, cache, script registry

player
  PlayerEngine abstraction + Media3Engine, MpvEngine, VlcEngine

render
  DanmakuOverlay, SubtitleOverlay, PreviewCard, FocusScaleBox
```

## 播放内核策略

`PlayerEngine` 暴露统一状态、进度、错误、字幕、音轨和速度控制。播放前由 `PlayerRouter` 根据媒体能力选择内核：

| 场景 | 默认内核 | 原因 |
| --- | --- | --- |
| HLS/DASH、AV1、离线缓存 | ExoPlayer | Android 生态与 Media3 最贴合 |
| HEVC/10bit/DTS/TrueHD/复杂 ASS 或特殊封装 | mpv | 解码与字幕渲染能力最强 |
| RTSP/RTMP/老封装/兼容性兜底 | libVLC | 网络协议和容错较好 |

用户可以固定默认内核，详情页和播放页也允许手动切换；切换内核时保留进度、字幕与弹幕配置。

## 媒体身份与匹配

所有来源都归一为 `MediaIdentity`：

```text
tmdb:tv:12345
emby:item:abcdef
douban:movie:12345
url:https://...
forward:resource-id
```

匹配器优先级：

1. 显式 TMDB/IMDb/Emby ID。
2. 标题 + 年份 + 季集。
3. 文件名解析后的标题 + 季集。
4. 模糊标题相似度。

资源模块返回的线路展示为“来源 + 清晰度 + 编码 + 延迟探测结果”。失败线路自动降级。

## Forward 插件运行时

Forward 模块是独立 JS 文件，入口为全局 `WidgetMetadata`。SofaPlay 使用 QuickJS 运行脚本，并向脚本注入兼容的 `Widget` 对象：

- `Widget.http.get/post/request`
- `Widget.html.load`
- `Widget.storage`
- `Widget.sharedCache`
- `Widget.tmdb`
- 需要时才注入 `Widget.dom`

安全策略：

1. 每个 Widget 一个独立存储 namespace。
2. `sharedCache` 有独立 namespace，并可被“清理模块缓存”整体清除。
3. 网络请求记录域名白名单；首次新域名向用户确认或使用全局开关。
4. 脚本没有 Android Context、文件系统、任意 Java 反射和原生能力。
5. 执行时间、内存、请求次数、返回大小有硬限制。
6. WebView 是可选能力，TV 端默认禁用，只有用户显式开启后才进入受限 WebView。

## 首页与详情页

首页不是固定 UI，而是 `HomeSection[]`。每个 Section 绑定一个 Forward 模块或内置 Provider，可配置模板、参数、排序和缓存。TV 首页保持横向行 + 纵向行的二维焦点模型。

详情页由多个 Block 组成：

- 背景、海报、简介、评分。
- 季集列表或资源列表。
- Emby 媒体库匹配结果。
- Forward 资源模块线路。
- 字幕、弹幕、播放记录、 Trakt/Emby 进度。
- 演员与分类入口，点击后回到列表模块。

## 播放页

遥控器焦点顺序：

1. 画面区域：上/下 快进快退，左/右 上一下一集。
2. 顶部：标题、线路、内核切换。
3. 左侧：季集列表。
4. 右侧：字幕、音轨、弹幕、倍速、画面比例。
5. 底部：进度条与快捷按钮。

弹幕与播放进度解耦为独立渲染层，保证切换内核时弹幕不需要重启。
