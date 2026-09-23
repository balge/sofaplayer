# ForwardWidget 兼容设计

## 必须实现的全局入口

每个模块必须暴露 `WidgetMetadata`：

```javascript
var WidgetMetadata = {
  id: "unique_id",
  title: "Module Title",
  version: "1.0.0",
  modules: [
    {
      id: "home",
      title: "Home",
      functionName: "loadHome",
      params: [],
    },
  ],
  search: {
    title: "Search",
    functionName: "search",
    params: [],
  },
};
```

## 模块类型

| `id` | `type` | 作用 |
| --- | --- | --- |
| 任意 | 空值 | 视频/列表模块 |
| `loadResource` | `stream` | 起播前动态生成线路 |
| `loadSubtitle` | `subtitle` | 字幕搜索 |
| `searchDanmu` | `danmu` | 弹幕搜索 |
| `getDetail` | `danmu` | 弹幕详情 |
| `getComments` | `danmu` | 弹幕评论 |
| `getDanmuWithSegmentTime` | `danmu` | 分段弹幕 |

## 数据兼容

`VideoItem` 字段保持 Forward 官方模型：

- `id/type/title/mediaType`
- `posterPath/backdropPath/detailPoster/coverUrl`
- `releaseDate/rating/duration/durationText`
- `genreItems/peoples/genreTitle`
- `videoUrl/link/episode`
- `episodeItems/childItems/relatedItems`

图片回退顺序：

1. 横图：`backdropPath -> coverUrl -> posterPath`
2. 竖图：`posterPath -> backdropPath -> coverUrl`
3. 详情海报：`detailPoster -> 竖图结果`
4. 剧照：`backdropPaths`

`loadDetail(link)` 返回 `VideoItem`、`VideoItem[]` 或 `null`。`loadResource` 返回 `VideoResource[]`，支持 `customHeaders`、`headers` 和 `playerType`。`X-Forward-Skip-Redirect-Probe: "1"` 用于跳过一次性签名 URL 的重定向探测。

## 弹幕

优先支持官方两种返回：

```json
[{"p":"10,1,#ffffff,uid","m":"hello"}]
```

```json
[[10, "1", "#ffffff", "", "hello"]]
```

播放器按时间分桶、预取相邻段、丢弃过期段。`segmentTime` 使用秒传给脚本，SofaPlay 内部使用毫秒。

## 字幕

`loadSubtitle` 返回 `SubtitleItem[]`。支持 SRT、ASS、SSA、VTT、SUB/IDX 与内置字幕。压缩包遵循官方 `resolveSubtitleArchive(params)` 规则，只返回解压目录内的相对路径。

## 与 Forward 的差异

| 项 | Forward iOS | SofaPlay |
| --- | --- | --- |
| WebView | 支持 | 默认关闭，TV 显式开启 |
| 触控拖拽 | 支持 | 遥控器焦点优先 |
| 本地 `.fwd` 导入 | 支持 | 支持，同时映射到 SofaPlay 布局 |
| 模块加密 | 支持 | 第一阶段导入加密包，第二阶段兼容官方解密 |
| 网络代理 | 系统能力 | 只提供 TV 可控的 HTTP 代理配置 |
