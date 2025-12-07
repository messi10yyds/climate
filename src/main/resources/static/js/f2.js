var chart = echarts.init(document.getElementById('mapContainer'));

initYearSelect(); // initialize select dropdown

chart.showLoading();

// 加载 USA.json 完整版
$.get('../data/USA.json', function (usaJson) {

    echarts.registerMap('USA', usaJson);

    chart.hideLoading();

    loadTemperature(2010);
});

// ⭐ 美观下拉框初始化
function initYearSelect() {
    const select = document.getElementById("yearInput");

    for (let y = 1895; y <= 2023; y++) {
        const opt = document.createElement("option");
        opt.value = y;
        opt.textContent = y;

        if (y === 2010) opt.selected = true;

        select.appendChild(opt);
    }

    // 自动触发查询
    select.addEventListener("change", () => loadTemperature(select.value));
}

function loadTemperature(year) {

    fetch(`/api/state/distribution?year=${year}`)
        .then(res => res.json())
        .then(res => {
            const seriesData = res.data.map(item => ({
                name: item.state_name,
                value: item.temp
            }));
            renderMap(seriesData, year);
        });
}

function renderMap(seriesData, year) {

    const option = {
        title: {
            text: `USA Temperature Distribution (${year})`,
            left: 'center'
        },

        tooltip: {
            trigger: 'item',
            formatter: p =>
                p.value ? `${p.name}<br>${p.value} °F` : `${p.name}<br>No Data`
        },

        visualMap: {
            min: 20,
            max: 90,
            show: false,
            inRange: {
                color: [
                    '#313695',
                    '#4575b4',
                    '#74add1',
                    '#abd9e9',
                    '#e0f3f8',
                    '#ffffbf',
                    '#fee090',
                    '#fdae61',
                    '#f46d43',
                    '#d73027',
                    '#a50026'
                ]
            }

        },

        series: [{
            type: 'map',
            map: 'USA',
            roam: true,

            boundingCoords: [
                [-130, 25],
                [-60, 50]
            ],

            layoutCenter: ['50%', '55%'],
            layoutSize: '170%',

            regions: [
                { name: 'Alaska', itemStyle: { areaColor: 'rgba(0,0,0,0)', borderColor: 'rgba(0,0,0,0)' }},
                { name: 'Hawaii', itemStyle: { areaColor: 'rgba(0,0,0,0)', borderColor: 'rgba(0,0,0,0)' }},
                { name: 'Puerto Rico', itemStyle: { areaColor: 'rgba(0,0,0,0)', borderColor: 'rgba(0,0,0,0)' }}
            ],

            data: seriesData
        }]
    };

    chart.setOption(option);
}
