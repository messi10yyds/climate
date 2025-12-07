// ------------------ 初始化年份 ------------------
const yearSelect = document.getElementById("yearSelect");
for (let y = 1895; y <= 2020; y++) {
    const opt = document.createElement("option");
    opt.value = y;
    opt.textContent = y;
    yearSelect.appendChild(opt);
}
yearSelect.value = 1954;

// ------------------ 初始化图表 ------------------
let chart = echarts.init(document.getElementById('chart'));

// 图表更新
function updateChart(counties, order, state) {
    const names = counties.map(c => c.county_name);
    const temps = counties.map(c => c.temp);

    chart.setOption({
        title: {
            text: `${state} - ${order === "top" ? "Top 5" : "Bottom 5"} Counties`,
            left: "center"
        },
        tooltip: {},
        xAxis: {
            type: 'category',
            data: names,
            axisLabel: { fontSize: 12 }
        },
        yAxis: { type: 'value' },
        series: [{
            type: 'bar',
            data: temps,
            barWidth: "45%",
            itemStyle: {
                color: order === "top" ? "#FF7F50" : "#4A90E2"
            }
        }]
    });
}

// 表格更新
function updateTable(rows) {
    const tbody = document.querySelector("#resultTable tbody");
    tbody.innerHTML = rows.map(r => `
        <tr>
            <td>${r.county_name}</td>
            <td>${r.temp}</td>
        </tr>
    `).join("");
}

// 清空图表与表格
function clearChartAndTable() {
    if (chart) {
        chart.clear();
    }
    document.querySelector("#resultTable tbody").innerHTML = "";
}

// ------------------ Search 查询 ------------------
async function searchData() {
    const state = document.getElementById("stateInput").value.trim();
    const year = yearSelect.value;
    const order = document.getElementById("orderSelect").value;

    if (!state) {
        alert("Please enter a state.");
        return;
    }

    const url = `/api/rank/counties?state=${encodeURIComponent(state)}&year=${year}&order=${order}`;

    try {
        const res = await fetch(url);
        if (!res.ok) {
            alert("Request failed: " + res.status);
            clearChartAndTable();
            return;
        }

        const json = await res.json();
        console.log("F7 response:", json);

        // ➤ 新增：检查 Result<T> 的 success 字段
        if (!json.success) {
            alert(json.message || "Query failed.");
            clearChartAndTable();
            return;
        }

        // ➤ 获取真正的数据数组
        const list = json.data;

        if (!Array.isArray(list) || list.length === 0) {
            alert("No data found.");
            clearChartAndTable();
            return;
        }

        const obj = list[0]; // 后端返回的是 `[ {state,...,data:[...] } ]`

        if (!obj || !Array.isArray(obj.data) || obj.data.length === 0) {
            alert("No county data.");
            clearChartAndTable();
            return;
        }

        updateChart(obj.data, order, obj.state || state);
        updateTable(obj.data);

    } catch (e) {
        console.error(e);
        alert("Network error, check console.");
        clearChartAndTable();
    }
}

// 按钮触发搜索
document.getElementById("searchBtn").addEventListener("click", searchData);

// 输入框按 Enter 触发搜索
document.getElementById("stateInput").addEventListener("keyup", function (e) {
    if (e.key === "Enter") searchData();
});
