document.addEventListener("DOMContentLoaded", function () {

    const typeSelect = document.getElementById("typeSelect");
    const regionInput = document.getElementById("regionInput");
    const intervalSelect = document.getElementById("intervalSelect");
    const queryBtn = document.getElementById("queryBtn");
    const errorMsg = document.getElementById("errorMsg");

    const btnF = document.getElementById("btnF");
    const btnC = document.getElementById("btnC");

    const chartDom = document.getElementById("chart");
    const chart = echarts.init(chartDom);

    // ---- 默认温度单位：华氏度 ----
    let currentUnit = "F";

    // ---- 缓存数据（避免重复查询） ----
    let lastYears = null;
    let lastTempsF = null;
    let lastTempsC = null;
    let lastRegion = null;
    let lastType = null;

    // ---- 默认年份间隔：5 ----
    intervalSelect.value = "5";

    // ---- 解析 URL 参数 ----
    const params = new URLSearchParams(window.location.search);
    const urlType = params.get("type");
    const urlRegion = params.get("region");

    if (urlType) typeSelect.value = urlType;
    if (urlRegion) regionInput.value = urlRegion;

    // ---- 国家不需要输入 region ----
    regionInput.disabled = (typeSelect.value === "national");
    typeSelect.addEventListener("change", () => {
        regionInput.disabled = (typeSelect.value === "national");
        if (regionInput.disabled) regionInput.value = "";
    });

    // ---- 年份间隔 map ----
    const intervalMap = {
        "1": 1,
        "5": 5,
        "10": 10,
        "20": 20,
        "30": 30,
        "50": 50
    };

    // ========== 图表绘制函数（F / C 切换核心） ==========
    function renderChart(years, tempsF, tempsC, region, type) {

        const temps = (currentUnit === "F") ? tempsF : tempsC;
        const step = intervalMap[intervalSelect.value];

        let sampledYears = [];
        let sampledTemps = [];

        for (let i = 0; i < years.length; i += step) {
            sampledYears.push(years[i]);
            sampledTemps.push(temps[i]);
        }

        chart.setOption({
            title: { text: `Trend (${type.toUpperCase()} - ${region})`, left: "center" },
            tooltip: { trigger: "axis" },
            xAxis: {
                type: "category",
                data: sampledYears,
                boundaryGap: false,
                axisTick: { alignWithLabel: true },
                axisLabel: { align: "center" }
            },
            yAxis: {
                type: "value",
                name: currentUnit === "F" ? "Temp (°F)" : "Temp (°C)"
            },
            series: [{
                type: "line",
                smooth: true,
                data: sampledTemps,
                lineStyle: { color: "#3d6dcc" },
                areaStyle: { color: "#aac4ff55" }
            }]
        });
    }

    // ========== 查询按钮（核心） ==========
    queryBtn.addEventListener("click", () => {
        errorMsg.innerText = "";

        const type = typeSelect.value;
        let region = regionInput.value.trim();

        if (type !== "national" && region === "") {
            errorMsg.innerText = "Please enter a region.";
            return;
        }

        const apiRegion = (type === "national") ? "usa" : region;

        // 更新 URL
        history.pushState({}, "", `/trend?region=${apiRegion}&type=${type}`);

        const url = `/api/trend?region=${apiRegion}&type=${type}`;

        fetch(url)
            .then(res => res.json())
            .then(r => {

                if (!r.success) {
                    errorMsg.innerText = r.message;
                    return;
                }

                const data = r.data;

                const years = data.map(d => d.year);
                const tempsF = data.map(d => d.temp);
                const tempsC = data.map(d => d.tempc);

                // 缓存
                lastYears = years;
                lastTempsF = tempsF;
                lastTempsC = tempsC;
                lastRegion = apiRegion;
                lastType = type;

                renderChart(years, tempsF, tempsC, apiRegion, type);
            })
            .catch(() => {
                errorMsg.innerText = "Request failed.";
            });
    });

    // ========== 单位切换：华氏度 ==========
    btnF.addEventListener("click", () => {
        currentUnit = "F";
        btnF.classList.add("active");
        btnC.classList.remove("active");

        if (lastYears) {
            renderChart(lastYears, lastTempsF, lastTempsC, lastRegion, lastType);
        }
    });

    // ========== 单位切换：摄氏度 ==========
    btnC.addEventListener("click", () => {
        currentUnit = "C";
        btnC.classList.add("active");
        btnF.classList.remove("active");

        if (lastYears) {
            renderChart(lastYears, lastTempsF, lastTempsC, lastRegion, lastType);
        }
    });

    // ========== 页面加载时自动查询 URL 参数 ==========
    if (urlType && urlRegion) {
        queryBtn.click();
    }
});
