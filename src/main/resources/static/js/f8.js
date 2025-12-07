let fullData = [];
let currentSortKey = null;
let sortAsc = true;

// 加载数据
async function loadData() {
    try {
        const res = await fetch("/api/amplitude/counties");
        const json = await res.json();
        if (!json.success || !Array.isArray(json.data)) {
            alert("Query failed.");
            return;
        }
        fullData = json.data;

        populateStates();
        renderTable(fullData);
    } catch (e) {
        console.error(e);
        alert("Network error.");
    }
}

// 填充州筛选下拉框
function populateStates() {
    const stateSelect = document.getElementById("stateSelect");
    const states = [...new Set(fullData.map(d => d.state_name))].sort();

    states.forEach(state => {
        const opt = document.createElement("option");
        opt.value = state;
        opt.textContent = state;
        stateSelect.appendChild(opt);
    });
}

// 渲染表格
function renderTable(list) {
    const tbody = document.querySelector("#resultTable tbody");
    tbody.innerHTML = list.map(item => `
        <tr>
            <td>${item.county_name}</td>
            <td>${item.state_name}</td>
            <td>${item.max_temp}</td>
            <td>${item.max_year}</td>
            <td>${item.min_temp}</td>
            <td>${item.min_year}</td>
            <td>${Number(item.amplitude).toFixed(3)}</td>
        </tr>
    `).join("");
}

// 执行搜索（按州过滤）
function doSearch() {
    const state = document.getElementById("stateSelect").value;

    let filtered = fullData;
    if (state !== "ALL") {
        filtered = fullData.filter(d => d.state_name === state);
    }

    renderTable(filtered);
}

// 表头排序
function handleSort(event) {
    const key = event.target.dataset.key;
    if (!key) return;

    sortAsc = (currentSortKey === key) ? !sortAsc : false;
    currentSortKey = key;

    const sorted = [...fullData].sort((a, b) => {
        if (typeof a[key] === "string") {
            return sortAsc
                ? a[key].localeCompare(b[key])
                : b[key].localeCompare(a[key]);
        }
        return sortAsc ? a[key] - b[key] : b[key] - a[key];
    });

    renderTable(sorted);
}

// 事件绑定
document.getElementById("searchBtn").addEventListener("click", doSearch);
document.querySelectorAll("#resultTable th")
    .forEach(th => th.addEventListener("click", handleSort));

// 页面加载自动获取数据
loadData();
