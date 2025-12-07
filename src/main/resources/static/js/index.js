document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/summary")
        .then(res => res.json())
        .then(r => {
            if (!r.success) return;
            const data = r.data;

            // 填入 Summary 信息
            document.getElementById("yearRange").innerText =
                `Data Years: ${data.yearStart} - ${data.yearEnd}`;

            document.getElementById("stateCount").innerText =
                `Total States: ${data.totalStates}`;

            document.getElementById("countyCount").innerText =
                `Total Counties: ${data.totalCounties}`;

            // 填入表信息
            let ul = document.getElementById("tableList");
            data.tables.forEach(t => {
                let li = document.createElement("li");
                li.innerText = `${t.table} — Rows: ${t.rows}`;
                ul.appendChild(li);
            });
        })
        .catch(err => {
            console.error("Summary API failed:", err);
        });
});
