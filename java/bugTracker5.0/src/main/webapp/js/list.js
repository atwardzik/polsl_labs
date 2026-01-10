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
                    <div class="issue-main">
                        <div title="${status.replace("_", " ")}" class="status-badge ${statusClass}"></div>
                        <div class="issue-list-id">${issue.id.substring(1)}</div>
                        <div class="issue-list-title">${issue.title}</div>
                    </div>
                    <div class="list-meta-info">
                        <div title="Author" class="issue-list-author">${issue.author}</div>
                        <div title="Due on" class="issue-list-dueDate">${issue.dueDate}</div>
                        <div title="Created on" class="issue-list-dateCreated">${issue.dateCreated}</div>
                    </div>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}