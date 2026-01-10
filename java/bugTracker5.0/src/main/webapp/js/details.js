function showIssueDetails(issueId) {
    fetch(`issue-details?id=${issueId}`)
        .then(response => response.json())
        .then(issueData => {
            const container = document.getElementById("contents");
            container.innerHTML = `
                <div class="issue-meta">
                    <h1>${issueData.title}</h1>
                    <p><strong>ID:</strong> <span class="issue-id">${issueData.id}</span></p>
                    <p><strong>Author:</strong> ${issueData.authorFullName} (${issueData.authorUsername})</p>
                    <p><strong>Status:</strong> ${issueData.status.replace("_", " ")}</p>
                    <p><strong>Priority:</strong> ${issueData.priority}</p>
                    <p><strong>Created:</strong> ${issueData.dateCreated}</p>
                    <p><strong>Due:</strong> ${issueData.dueDate}</p>
                </div>
                <div class="issue-description">
                    ${issueData.description}
                </div>
                <div class="issue-edit-bar">
                    <span class="modifier" onclick="window.location='tracker.html?view=edit&id=${issueId}';">Edit</span>
                    <span class="modifier" onclick="">Assign</span>
                    <select id="status-select">
                        <option value="OPEN">Open</option>
                        <option value="CLOSED">Closed</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="REOPENED">Reopened</option>
                    </select>
                </div>
            `;

            const select = container.querySelector("#status-select");
            select.value = issueData.status;

            select.addEventListener("change", () => {
                const newStatus = select.value;
                fetch("edit-issue", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
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
        })
        .catch(err => console.error(err));
}