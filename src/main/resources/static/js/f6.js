let allStates = [];   // 全部州异常数据（含各年）
let years = [];       // 年份列表

window.onload = function () {
    loadStateAnomalies();
};

/* 三位小数格式 */
function format3(num) {
    return Number(num).toFixed(3);
}

/* ======================
   加载全部异常州
====================== */
function loadStateAnomalies() {
    fetch("/api/anomaly/states")
        .then(resp => resp.json())
        .then(res => {
            if (!res.success || !Array.isArray(res.data)) {
                console.error("Invalid state anomaly data:", res);
                return;
            }

            allStates = res.data;

            years = [...new Set(allStates.map(s => s.year))].sort();
            renderYearSelect();
            updateByYear();
        });
}

/* 渲染年份选择器 */
function renderYearSelect() {
    const select = document.getElementById("yearSelect");
    select.innerHTML = "";

    years.forEach(y => {
        let opt = document.createElement("option");
        opt.value = y;
        opt.textContent = y;
        select.appendChild(opt);
    });
}

/* ======================
   根据年份更新内容
====================== */
function updateByYear() {
    const year = Number(document.getElementById("yearSelect").value);
    const states = allStates.filter(s => s.year === year);

    renderStateChart(states);
    renderStateTable(states);
}

/* ======================
   渲染柱状图
====================== */
function renderStateChart(list) {
    let names = list.map(s => s.stusab);
    let deviations = list.map(s => Number(format3(s.deviation)));

    let chart = echarts.init(document.getElementById("stateChart"));

    // ⭐ 动态柱宽：如果只有 1 个柱子，用固定像素宽度
    const barWidthSetting = list.length === 1 ? 30 : "20%";
    const barGapSetting  = list.length === 1 ? "60%" : "40%";

    chart.setOption({
        title: { text: "State Deviation (°F)", left: "center" },
        tooltip: {
            trigger: "item",
            formatter: p => `${p.name}<br/>Deviation: ${p.value}°F`
        },
        xAxis: {
            type: "category",
            data: names,
            axisLabel: { rotate: 30 }
        },
        yAxis: { type: "value", name: "Deviation (°F)" },
        series: [{
            type: "bar",
            data: deviations,
            barWidth: barWidthSetting,      // ⭐ 动态柱宽
            barCategoryGap: barGapSetting,  // ⭐ 动态间距
            itemStyle: {
                color: p => p.value >= 0 ? "#ff4d4f" : "#2f6bff",
                borderRadius: [4, 4, 0, 0]
            }
        }]
    });
}


/* ======================
   渲染表格（州 + 可展开县级信息）
====================== */
function renderStateTable(list) {
    let html = `
        <tr>
            <th>State</th>
            <th>Year</th>
            <th>Temp</th>
            <th>Mean</th>
            <th>Deviation</th>
            <th>Anomaly</th>
            <th>Counties</th>
        </tr>
    `;

    list.forEach((item, index) => {
        html += `
            <tr>
                <td>${item.state_name} (${item.stusab})</td>
                <td>${item.year}</td>
                <td>${format3(item.temp)}</td>
                <td>${format3(item.mean)}</td>
                <td>${format3(item.deviation)}</td>
                <td>${item.anomaly}</td>
                <td>
                    <span class="expand-btn" onclick="toggleCounties(${index})">▶ Show</span>
                    <div id="county-${index}" style="display:none; margin-top:8px;">
                        ${renderCountyBoxes(item.counties)}
                    </div>
                </td>
            </tr>
        `;
    });

    document.getElementById("stateTable").innerHTML = html;
}

/* 生成县级展开内容 */
function renderCountyBoxes(counties) {
    if (!counties || counties.length === 0) return "<i>No county anomalies</i>";

    return counties.map(c => `
        <div class="county-box">
            <strong>${c.county_name}</strong><br>
            Temp: ${format3(c.temp)} |
            Mean: ${format3(c.mean)} |
            Dev: ${format3(c.deviation)} (${c.anomaly})
        </div>
    `).join("");
}

/* 展开/折叠逻辑 */
function toggleCounties(idx) {
    const div = document.getElementById(`county-${idx}`);
    const btn = div.previousElementSibling;

    if (div.style.display === "none") {
        div.style.display = "block";
        btn.textContent = "▼ Hide";
    } else {
        div.style.display = "none";
        btn.textContent = "▶ Show";
    }
}
