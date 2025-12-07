// --- 州缩写 -> 全名映射 ---
const STATE_NAME_MAP = {
    AL: "Alabama", AK: "Alaska", AZ: "Arizona", AR: "Arkansas", CA: "California",
    CO: "Colorado", CT: "Connecticut", DE: "Delaware", FL: "Florida", GA: "Georgia",
    HI: "Hawaii", ID: "Idaho", IL: "Illinois", IN: "Indiana", IA: "Iowa",
    KS: "Kansas", KY: "Kentucky", LA: "Louisiana", ME: "Maine", MD: "Maryland",
    MA: "Massachusetts", MI: "Michigan", MN: "Minnesota", MS: "Mississippi",
    MO: "Missouri", MT: "Montana", NE: "Nebraska", NV: "Nevada", NH: "New Hampshire",
    NJ: "New Jersey", NM: "New Mexico", NY: "New York", NC: "North Carolina",
    ND: "North Dakota", OH: "Ohio", OK: "Oklahoma", OR: "Oregon", PA: "Pennsylvania",
    RI: "Rhode Island", SC: "South Carolina", SD: "South Dakota", TN: "Tennessee",
    TX: "Texas", UT: "Utah", VT: "Vermont", VA: "Virginia", WA: "Washington",
    WV: "West Virginia", WI: "Wisconsin", WY: "Wyoming", DC: "District of Columbia"
};

function getStateFullName(input) {
    if (!input) return input;
    const up = input.toUpperCase();
    if (STATE_NAME_MAP[up]) return STATE_NAME_MAP[up];

    const low = input.toLowerCase();
    for (const name of Object.values(STATE_NAME_MAP)) {
        if (name.toLowerCase() === low) return name;
    }
    return input;
}

document.getElementById("searchBtn").addEventListener("click", fetchExtreme);

function fetchExtreme() {
    const type = document.getElementById("regionType").value;
    const regionInput = document.getElementById("regionName").value.trim();

    fetch(`/api/extreme?region=${regionInput}&type=${type}`)
        .then(res => res.json())
        .then(json => {
            const data = json.data;

            document.getElementById("resultCard").style.display = "block";
            document.getElementById("tableCard").style.display = "none";

            let displayName = regionInput;
            if (type === "state") displayName = getStateFullName(regionInput);
            if (type === "national") displayName = "United States";

            // 点击地区名跳转（年份不跳转）
            document.getElementById("resultTitle").innerHTML =
                `<span class="year-link" onclick="loadCompareData('${type}', '${regionInput}', ${data.max.year}, ${data.min.year})">${displayName}</span> - Extreme Temperatures`;

            document.getElementById("maxTemp").innerText = `${data.max.temp}°F (${data.max.year})`;
            document.getElementById("minTemp").innerText = `${data.min.temp}°F (${data.min.year})`;
        });
}


/*
=================================================
点击国家名 / 州名 → 加载两个年份的对比表
=================================================
*/

async function loadCompareData(type, region, yearHigh, yearLow) {
    let fullName = region;

    if (type === "state") fullName = getStateFullName(region);
    if (type === "national") fullName = "United States";

    const urlHigh =
        type === "national"
            ? `/api/extreme/national/year?year=${yearHigh}`
            : `/api/extreme/state/year?state=${region}&year=${yearHigh}`;

    const urlLow =
        type === "national"
            ? `/api/extreme/national/year?year=${yearLow}`
            : `/api/extreme/state/year?state=${region}&year=${yearLow}`;

    const [highRes, lowRes] = await Promise.all([
        fetch(urlHigh).then(r => r.json()),
        fetch(urlLow).then(r => r.json())
    ]);

    const highList = highRes.data;
    const lowList = lowRes.data;

    // 合并两年数据（按 state_name 或 county_name）
    const map = new Map();

    highList.forEach(item => {
        const key = type === "national" ? item.state_name : item.county_name;
        map.set(key, {
            name: key,
            code: type === "national" ? item.stusab : item.county_fips,
            high: item.max_temp ?? item.min_temp,
            low: null
        });
    });

    lowList.forEach(item => {
        const key = type === "national" ? item.state_name : item.county_name;
        if (map.has(key)) {
            map.get(key).low = item.max_temp ?? item.min_temp;
        }
    });

    const combined = Array.from(map.values());

    // 渲染表格
    document.getElementById("tableCard").style.display = "block";

    const title =
        type === "national"
            ? `All States Temperature Comparison (${yearHigh} vs ${yearLow})`
            : `All Counties Temperature Comparison in ${fullName} (${yearHigh} vs ${yearLow})`;

    document.getElementById("tableTitle").innerText = title;

    const thead = document.querySelector("#extremeTable thead");
    const tbody = document.querySelector("#extremeTable tbody");

    thead.innerHTML = `
        <tr>
            <th>${type === "national" ? "State" : "County"}</th>
            <th>${type === "national" ? "Abbreviation" : "FIPS"}</th>
            <th>Temp (${yearHigh})</th>
            <th>Temp (${yearLow})</th>
        </tr>
    `;

    tbody.innerHTML = combined.map(item => `
        <tr>
            <td>${item.name}</td>
            <td>${item.code}</td>
            <td>${item.high ?? "-"}</td>
            <td>${item.low ?? "-"}</td>
        </tr>
    `).join("");
}
