# Emby 接入设计

## 服务地址与鉴权

用户输入的服务地址统一去掉末尾 `/`，可保存多个 Emby 服务器。

登录流程：

```http
POST /Users/AuthenticateByName
X-Emby-Authorization: MediaBrowser Client="SofaPlay", Device="Android TV", DeviceId="{deviceId}", Version="0.1.0"
Content-Type: application/json

{
  "Username": "user",
  "Pw": "password"
}
```

保存 `AccessToken` 与 `User.Id`。Token 属于敏感数据，使用 Android Keystore 加密后写入 DataStore，不进入普通日志。

## 基础接口

| 用途 | 接口 |
| --- | --- |
| 资料库根 | `/Users/{userId}/Views` |
| 电影/剧集列表 | `/Users/{userId}/Items?ParentId={id}&Recursive=true` |
| 详情 | `/Users/{userId}/Items/{itemId}` |
| 季列表 | `/Shows/{seriesId}/Seasons?userId={userId}` |
| 集列表 | `/Shows/{seriesId}/Episodes?seasonId={seasonId}&userId={userId}` |
| PlaybackInfo | `/Items/{itemId}/PlaybackInfo` |
| 图片 | `/Items/{itemId}/Images/Primary`、`/Items/{itemId}/Images/Backdrop` |
| 起播 | `/Sessions/Playing` |
| 进度 | `/Sessions/Playing/Progress` |
| 停止 | `/Sessions/Playing/Stopped` |

列表请求常用字段：

```text
Fields=PrimaryImageAspectRatio,Overview,ProductionYear,Path,MediaStreams
IncludeItemTypes=Movie,Series,Episode
SortBy=SortName
SortOrder=Ascending
```

## 图片策略

TV 端列表分辨率建议：

- 海报墙：`Primary?maxWidth=342&quality=85`
- 横向卡片：`Backdrop?maxWidth=640&quality=85`
- 详情背景：`Backdrop?maxWidth=1280&quality=90`
- 焦点放大时使用两级缓存，避免反复请求大图。

必须支持 Emby 的 tag 缓存参数，服务端 tag 变化后再刷新磁盘缓存。

## 播放选择

先请求 `PlaybackInfo`，拿到 `MediaSources` 后判断：

1. 容器、视频编码、音频编码、HDR、码率。
2. 当前设备硬解能力、HDR 显示能力、音频直通能力。
3. 是否触发服务端转码。
4. 网络类型与用户设置的码率上限。

优先请求 `static=true` 原盘，不能直解时再使用服务端转码。播放 URL 示例：

```http
/Videos/{itemId}/stream?static=true&mediaSourceId={mediaSourceId}&api_key={token}
```

转码地址使用服务端返回的 `TranscodingUrl`，不要自行猜测。

## 进度同步

- 每 10 秒上报一次 `Playing/Progress`。
- `Seek`、暂停/恢复、选集、切换线路立即上报。
- 退出播放页或 Home 键进入后台时上报 `Playing/Stopped`。
- 下次进入详情页用 `UserData.PlaybackPositionTicks` 初始化进度。

Emby ticks 换算：`1ms = 10000 ticks`。

## 与插件资源融合

详情页同时展示：

1. Emby 当前条目。
2. Emby 同名/同年匹配的其他版本。
3. ForwardWidget `loadResource` 返回的线路。
4. 用户手动添加的直链。

默认排序依据：上次使用来源 > 直解成功率 > 清晰度 > 延迟 > Emby 服务端推荐。
