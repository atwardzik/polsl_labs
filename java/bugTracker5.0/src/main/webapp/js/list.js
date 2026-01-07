function showList() {
    fetch("list-servlet")
        .then(response => response.json())
        .then(issues => {
            const container = document.getElementById("contents");
            container.innerHTML = "";

            issues.forEach(issue => {
                const status = issue.status;
                let statusClass = "";

                switch (status) {
                    case "IN_PROGRESS":
                        statusClass = "badge-in-progress";
                        break;
                    case "OPEN":
                        statusClass = "badge-open";
                        break;
                    case "CLOSED":
                        statusClass = "badge-closed";
                        break;
                    case "REOPENED":
                        statusClass = "badge-reopened";
                        break;
                    default:
                        statusClass = "badge-default";
                }

                const card = document.createElement("a");
                card.className = "issues-card";
                card.href = `tracker.html?view=issue&id=${issue.id.substring(1)}`;
                card.className = "issues-card";
                card.innerHTML = `
                    <div title="${status.replace("_", " ")}" class="status-badge ${statusClass}"></div>
                    <div class="issue-list-id">${issue.id.substring(1)}</div>
                    <div class="issue-list-title">${issue.title}</div>
                    <div class="list-meta-info">
                        <div class="issue-list-author">${issue.author}</div>
                        <div title="Created on" class="issue-list-dateCreated">${issue.dateCreated}</div>
                        <div title="Due on" class="issue-list-dueDate">${issue.dueDate}</div>
                    </div>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function showIssueDetails(issueId) {
    fetch("issue-details?id=" + issueId)
        .then(response => response.json())
        .then(issueData => {
            const container = document.getElementById("contents");
            container.innerHTML = "";

            const meta = document.createElement("div");
            meta.className = "issue-meta";
            meta.innerHTML = `
                <h1>${issueData.title}</h1>
                <p><strong>ID:</strong> ${issueId}</p>
                <p><strong>Author:</strong> ${issueData.author}</p>
                <p><strong>Status:</strong> ${issueData.status.replace("_", " ")}</p>
                <p><strong>Created:</strong> ${issueData.dateCreated}</p>
                <p><strong>Due:</strong> ${issueData.dueDate}</p>
                <hr>
            `;
            container.appendChild(meta);

            const desc = document.createElement("div");
            desc.className = "issue-description";
            desc.innerHTML = issueData.description;
            container.appendChild(desc);


            const editBar = document.createElement("div");
            editBar.className = "issue-edit-bar";

            const editLink = document.createElement("a");
            editLink.href = `edit-issue-servlet?id=${issueId}`;
            editLink.textContent = "Edit";
            const assignLink = document.createElement("a");
            assignLink.href = `edit-issue-servlet?id=${issueId}`;
            assignLink.textContent = "Assign";

            const select = document.createElement("select");
            ["OPEN", "CLOSED", "REOPENED", "IN_PROGRESS"].forEach(status => {
                const option = document.createElement("option");
                option.value = status;
                option.textContent = status.replace("_", " ");
                if (status === issueData.status) option.selected = true;
                select.appendChild(option);
            });

            select.addEventListener("change", () => {
                const newStatus = select.value;
                fetch("edit-issue-servlet", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: `id=${encodeURIComponent(issueId)}&status=${encodeURIComponent(newStatus)}`
                })
                    .then(response => {
                        if (!response.ok) throw new Error("Update failed");

                        location.reload();
                    })
                    .catch(err => {
                        alert("Failed to update status");
                        console.error(err);
                    });
            });

            editBar.appendChild(editLink);
            editBar.appendChild(assignLink);
            editBar.appendChild(select);
            container.appendChild(editBar);
        })
        .catch(err => console.error(err));
}

window.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const view = params.get("view");
    const id = params.get("id");

    if (view === "issue" && id) {
        showIssueDetails(id);
    } else if (view === "filter") {
        showFilter();
    } else {
        showList();
    }
});