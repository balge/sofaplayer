# Roadmap

## Phase 0：项目基线

- Android TV 工程骨架。
- 数据、播放器、插件核心接口。
- 架构与 Forward 兼容文档。

## Phase 1：可播放

- Media3/ExoPlayer 接入。
- Emby 登录、资料库、电影/剧集、播放进度。
- TV 首页和详情页基础 UI。
- 本地/直链播放、进度条、选集。

## Phase 2：三内核

- `PlayerEngine` 能力矩阵。
- libVLC 和 mpv 的 AAR/so 接入。
- 音轨、字幕、倍速、缓冲、HDR、Dolby 相关检测。
- 起播前探测与手动切换内核。

## Phase 3：Forward 插件

- QuickJS 运行时与 `Widget` 桥。
- `.js/.fwd` 导入、更新、缓存、日志和测试。
- 首页模块、搜索、详情、动态资源。
- 模块沙箱与域名白名单。

## Phase 4：增强体验

- 弹幕服务器与 Forward 弹幕模块。
- 字幕压缩包、在线字幕、ASS 高级样式。
- Emby + 插件资源自动匹配。
- 继续观看、播放记录、收藏、家庭 Profile。

## Phase 5：稳定性

- 性能、崩溃、插件超时监控。
- TV 帧率、内存、网络重试策略。
- 快捷安装包、CI、签名与发布通道。
