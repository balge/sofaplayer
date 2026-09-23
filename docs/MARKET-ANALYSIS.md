# TV 播放器与 Forward 竞品分析

## 结论

市面产品通常只解决一个问题：Kodi 强在媒体中心、Jellyfin/Infuse 强在私有媒体库、Just Player/MX Player 强在播放内核、Forward 强在插件与首页聚合。SofaPlay 的机会是把这四件事压进一个 TV-first 架构，而不是把手机 UI 移植到电视。

## 重点产品

| 产品 | 可借鉴 | 需要避免 |
| --- | --- | --- |
| Kodi/Jellyfin | 媒体库扫描、刮削、季集模型、多语言、播放记录 | 配置层级深，TV 首次体验复杂 |
| Infuse/Emby Theater | Emby/Jellyfin 登录、海报墙、继续观看、进度同步 | 闭源、插件扩展有限 |
| Just Player | Media3 能力矩阵、音轨/字幕处理、简洁播放页 | 不适合做首页聚合 |
| MX Player/FX Player | 解码兜底、网络流兼容、快捷手势 | 大量广告/权限较重，TV 焦点体验一般 |
| mpv-android | 复杂字幕、硬解、外部滤镜、脚本控制 | 界面极简，需要包一层 TV UI |
| VLC Android | 多协议、异常兜底、跨平台稳定性 | 高级样式字幕和现代 UI 较弱 |
| Forward | JS 插件协议、首页配置、资源聚合、弹幕/字幕扩展 | iOS 触控优先，没有 Android TV 焦点系统 |

## SofaPlay 的关键差异

1. **TV-first**：所有页面围绕方向键、OK、Back、Menu、长按设计；不支持触控也能完整使用。
2. **三内核不是菜单噱头**：播放前根据 HLS/DASH/RTSP/HEVC/封装/字幕/音轨自动选择，失败自动降级。
3. **Emby 是 Provider，不是唯一后端**：Emby、ForwardWidget、Stremio、本地文件进入同一套 `MediaItem` 模型。
4. **首页可编程**：Forward 模块决定首页内容，内置内容只作为兜底 Section。
5. **详情页可融合**：一个剧集详情页同时展示 Emby 库内匹配、插件资源、在线预告、字幕和弹幕。
6. **插件可审计**：脚本有沙箱、日志、网络白名单、缓存清理和返回结构校验。

## 从 Forward 学到的核心

- 模块就是一个 JS 文件，导入成本极低。
- `WidgetMetadata` 同时声明功能、参数和函数名，宿主不需要理解每个站点。
- 首页模板与数据源分离，同一数据源可以换展示模板。
- `loadResource` 必须在真正起播时调用，避免短期 URL 提前失效。
- 弹幕支持分段加载，长视频不能一次性把全部弹幕塞进内存。
- 字幕压缩包允许脚本挑选真正要用的字幕文件。

## MVP 拒绝范围

第一版不做：

- Torrent/磁力下载器。
- DRM 私有流。
- 任意 WebView 自动登录。
- 全功能媒体整理/刮削编辑。
- 手机端完整 UI。

这些能力留到稳定期，避免拖慢 TV 播放和插件生态主链路。
