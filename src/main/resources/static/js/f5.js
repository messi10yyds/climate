function searchRank() {
    const type = document.getElementById("type").value;
    const year = document.getElementById("year").value;
    const order = document.getElementById("order").value;

    if (!year) {
        alert("Please enter a year.");
        return;
    }

    const url = `/api/rank?type=${type}&year=${year}&order=${order}`;

    fetch(url)
        .then(resp => resp.json())
        .then(res => {
            if (!res.success) {
                alert(res.message);
                return;
            }
            renderRank(res.data, type, year, order);
        });
}

function renderRank(list, type, year, order) {

    const names = list.map(d =>
        type === "state" ? d.stusab : `${d.county_name} (${d.stusab})`
    );
    const temps = list.map(d => d.temp);

    // === 柱状图 ===
    let chart = echarts.init(document.getElementById("chart"));
    chart.setOption({
        title: {
            text: `Top 10 ${type === "state" ? "States" : "Counties"} (${order === "desc" ? "Highest" : "Lowest"}) - ${year}`,
            left: "center"
        },
        tooltip: { trigger: "axis" },
        xAxis: {
            type: "category",
            data: names,
            axisLabel: { fontSize: 12, interval: 0, rotate: 30 }
        },
        yAxis: {
            type: "value",
            name: "Temp (°F)"
        },
        series: [{
            type: "bar",
            data: temps,
            barWidth: "30%",
            barCategoryGap: "25%",
            itemStyle: {
                color: "#2f6bff",
                borderRadius: [4, 4, 0, 0]
            }
        }]
    });

    // === 表格 ===
    let tableHtml = "<tr>";

    if (type === "state") {
        tableHtml += `
            <th>State</th>
            <th>Year</th>
            <th>Avg Temp (°F)</th>
        </tr>
        `;
    } else {
        tableHtml += `
            <th>County</th>
            <th>State</th>
            <th>Year</th>
            <th>Avg Temp (°F)</th>
        </tr>
        `;
    }

    list.forEach(item => {
        if (type === "state") {
            tableHtml += `
                <tr>
                    <td>${item.state_name} (${item.stusab})</td>
                    <td>${item.year}</td>
                    <td>${item.temp}</td>
                </tr>
            `;
        } else {
            tableHtml += `
                <tr>
                    <td>${item.county_name}</td>
                    <td>${item.state_name} (${item.stusab})</td>
                    <td>${item.year}</td>
                    <td>${item.temp}</td>
                </tr>
            `;
        }
    });

    document.getElementById("rankTable").innerHTML = tableHtml;
}
