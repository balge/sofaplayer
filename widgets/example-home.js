var WidgetMetadata = {
    id: "sofaplay.example.home",
    title: "SofaPlay 示例首页",
    description: "验证 ForwardWidget 列表协议的最小模块",
    author: "SofaPlay",
    site: "https://example.com",
    version: "1.0.0",
    requiredVersion: "0.0.1",
    detailCacheDuration: 60,
    modules: [
        {
            id: "hotList",
            title: "今日推荐",
            description: "静态样例列表，用于联调 TV 首页",
            requiresWebView: false,
            functionName: "loadHotList",
            sectionMode: false,
            cacheDuration: 3600,
            params: []
        }
    ],
    search: {
        title: "搜索",
        functionName: "search",
        params: []
    }
};

async function loadHotList() {
    return [
        {
            id: "tv.1399",
            type: "tmdb",
            title: "Game of Thrones",
            mediaType: "tv",
            posterPath: "https://image.tmdb.org/t/p/w500/1XS1oqL89akfn8Sf5EEDInXj4Uh.jpg",
            backdropPath: "https://image.tmdb.org/t/p/w780/suopoADq0k8YZr4dQXcU6pToj6s.jpg",
            releaseDate: "2011-04-17",
            rating: "8.5",
            genreItems: [{ id: "drama", title: "剧情" }],
            link: "tv.1399"
        }
    ];
}

async function search(params) {
    const query = String(params.keyword || "").toLowerCase();
    if (!query || query.includes("game")) {
        return await loadHotList();
    }
    return [];
}
