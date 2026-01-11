function showIssueDetails(issueId) {
    fetch(`issue-details?id=${issueId}`)
        .then(response => response.json())
        .then(issue => {
            const container = document.getElementById("contents");
            container.innerHTML = `
                <div class="issue-meta">
                    <h1>${issue.data.title}</h1>
                    <p><strong>${issue.locale.id}:</strong> <span class="issue-id">${issue.data.id}</span></p>
                    <p><strong>${issue.locale.author}:</strong> ${issue.data.authorFullName} (${issue.data.authorUsername})</p>
                    <p><strong>${issue.locale.status}:</strong> ${issue.locale[issue.data.status]}</p>
                    <p><strong>${issue.locale.priority}:</strong> ${issue.locale[issue.data.priority]}</p>
                    <p><strong>${issue.locale.createdOn}:</strong> ${issue.data.dateCreated}</p>
                    <p><strong>${issue.locale.dueOn}:</strong> ${issue.data.dueDate}</p>
                </div>
                <div class="issue-description">
                    ${issue.data.description}
                </div>
                <div class="issue-edit-bar">
                    <span class="modifier" onclick="window.location='tracker.html?view=edit&id=${issueId}';">Edit</span>
                    <span class="modifier" onclick="">Assign</span>
                    <select id="status-select">
                        <option value="OPEN">${issue.locale.OPEN}</option>
                        <option value="CLOSED">${issue.locale.CLOSED}</option>
                        <option value="IN_PROGRESS">${issue.locale.IN_PROGRESS}</option>
                        <option value="REOPENED">${issue.locale.REOPENED}</option>
                    </select>
                </div>
            `;

            const select = container.querySelector("#status-select");
            select.value = issue.data.status;

            select.addEventListener("change", () => {
                const newStatus = select.value;
                fetch("edit-issue", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: `id=${encodeURIComponent(issueId)}&status=${encodeURIComponent(newStatus)}`
                })
                    .then(async response => {
                        if (response.status === 401 || response.status === 400) {
                            throw new Error("You are not logged in!");
                        }

                        if (!response.ok) {
                            throw new Error("Unexpected error");
                        }

                        return await response.json();
                    })
                    .then(issue => {
                        if (!issue) return;
                        location.reload();
                    })
                    .catch(err => {
                        console.error("Error message:", err.message);
                        alert(`Failed to update issue: ${err.message}`);
                    });
            });
        })
        .catch(err => console.error(err));
}