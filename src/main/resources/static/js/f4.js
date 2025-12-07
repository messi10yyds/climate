function searchCompare() {
    const type = document.getElementById("type").value;
    const year = document.getElementById("year").value;
    const regions = document.getElementById("regions").value.trim();

    if (!year || !regions) {
        alert("Please enter year and regions.");
        return;
    }

    const url = `/api/compare?type=${type}&year=${year}&regions=${encodeURIComponent(regions)}`;

    fetch(url)
        .then(resp => resp.json())
        .then(res => {
            if (!res.success) {
                alert(res.message);
                return;
            }
            renderResult(res.data, type, year);
        });
}

function renderResult(data, type, year) {

    const names = data.map(d => type === "state" ? d.stusab : d.county_name);
    const temps = data.map(d => d.temp);

    // --- 绘制柱状图（已经优化为细柱状） ---
    let chart = echarts.init(document.getElementById("chart"));

    chart.setOption({
        title: { text: `Temperature Comparison (${year})`, left: "center" },
        tooltip: { trigger: "axis" },
        xAxis: {
            type: "category",
            data: names,
            axisLabel: { fontSize: 14 }
        },
        yAxis: {
            type: "value",
            name: "Temp (°F)",
            axisLabel: { fontSize: 14 }
        },
        series: [{
            type: "bar",
            data: temps,
            barWidth: "30%",        // ⭐ 更细的柱状图
            barCategoryGap: "25%",  // ⭐ 设置柱状图间距
            itemStyle: {
                color: "#2f6bff",
                borderRadius: [4, 4, 0, 0]
            }
        }]
    });

    // --- 渲染表格 ---
    let html = `
        <tr>
            <th>${type === "state" ? "State" : "County"}</th>
            <th>Year</th>
            <th>Avg Temp (°F)</th>
        </tr>
    `;

    data.forEach(item => {
        html += `
            <tr>
                <td>${type === "state" ? item.state_name : item.county_name}</td>
                <td>${item.year}</td>
                <td>${item.temp}</td>
            </tr>
        `;
    });

    document.getElementById("resultTable").innerHTML = html;
}
