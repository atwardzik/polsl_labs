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
    fetch(`issue-details?id=${issueId}`)
        .then(response => response.json())
        .then(issueData => {
            const container = document.getElementById("contents");
            container.innerHTML = `
                <div class="issue-meta">
                    <h1>${issueData.title}</h1>
                    <p><strong>ID:</strong> ${issueId}</p>
                    <p><strong>Author:</strong> ${issueData.author}</p>
                    <p><strong>Status:</strong> ${issueData.status.replace("_", " ")}</p>
                    <p><strong>Priority:</strong> ${issueData.priority}</p>
                    <p><strong>Created:</strong> ${issueData.dateCreated}</p>
                    <p><strong>Due:</strong> ${issueData.dueDate}</p>
                    <hr>
                </div>
                <div class="issue-description">
                    ${issueData.description}
                </div>
                <div class="issue-edit-bar">
                    <a href="tracker.html?view=edit&id=${issueId}">Edit</a>
                    <a href="">Assign</a>
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
                fetch("edit-issue-servlet", {
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

function showEdit(issueId) {
    fetch(`issue-details?id=${issueId}`)
        .then(response => response.json())
        .then(issueData => {
            const container = document.getElementById("contents");

            container.innerHTML = `
                <div class="issue-edit">
                    <input type="text" class="issue-edit-title" value="${issueData.title}">

                    <div class="issue-edit-meta">
                        <div class="issue-edit-field">
                            <label>Status</label>
                            <select class="issue-edit-status">
                                <option value="OPEN">Open</option>
                                <option value="CLOSED">Closed</option>
                                <option value="REOPENED">Reopened</option>
                                <option value="IN_PROGRESS">In Progress</option>
                            </select>
                        </div>

                        <div class="issue-edit-field">
                            <label>Priority</label>
                            <select class="issue-edit-priority">
                                <option value="LOW">Low</option>
                                <option value="MEDIUM">Medium</option>
                                <option value="HIGH">High</option>
                                <option value="CRITICAL">Critical</option>
                            </select>
                        </div>

                        <div class="issue-edit-field">
                            <label>Due date</label>
                            <input type="date" class="issue-edit-dueDate" value="${issueData.dueDate}">
                        </div>
                    </div>

                    <div class="issue-edit-description" contenteditable="true">
                        ${issueData.description || ""}
                    </div>

                    <button class="issue-save-btn">Save</button>
                </div>
            `;

            const statusSelect = container.querySelector(".issue-edit-status");
            statusSelect.value = issueData.status;

            const prioritySelect = container.querySelector(".issue-edit-priority");
            prioritySelect.value = issueData.priority;

            container.querySelector(".issue-save-btn").onclick = () => saveIssue(issueId);
        })
        .catch(err => console.error(err));
}

function saveIssue(issueId) {
    const title = document.querySelector(".issue-edit-title").value;
    const status = document.querySelector(".issue-edit-status").value;
    const priority = document.querySelector(".issue-edit-priority").value;
    const dueDate = document.querySelector(".issue-edit-dueDate").value;
    const description = document.querySelector(".issue-edit-description").innerHTML;

    fetch("edit-issue-servlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            id: issueId,
            title: title,
            status: status,
            priority: priority,
            dueDate: dueDate,
            description: description
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Save failed");
            }
            return response.text();
        })
        .then(() => {
            alert("Issue saved successfully");
            showIssueDetails(issueId);
        })
        .catch(err => {
            console.error(err);
            alert("Failed to save issue");
        });
}

function showIssueCreator() {
    const container = document.getElementById("contents");

    container.innerHTML = `
                <div class="issue-edit">
                    <input type="text" class="issue-edit-title" placeholder="Title">

                    <div class="issue-edit-meta">
                        <div class="issue-edit-field">
                            <label>Status</label>
                            <select class="issue-edit-status">
                                <option value="OPEN">Open</option>
                                <option value="CLOSED">Closed</option>
                                <option value="REOPENED">Reopened</option>
                                <option value="IN_PROGRESS">In Progress</option>
                            </select>
                        </div>

                        <div class="issue-edit-field">
                            <label>Priority</label>
                            <select class="issue-edit-priority">
                                <option value="LOW">Low</option>
                                <option value="MEDIUM">Medium</option>
                                <option value="HIGH">High</option>
                                <option value="CRITICAL">Critical</option>
                            </select>
                        </div>

                        <div class="issue-edit-field">
                            <label>Due date</label>
                            <input type="date" class="issue-edit-dueDate">
                        </div>
                    </div>

                    <div class="issue-edit-description" contenteditable="true" data-placeholder="Enter the issue description here..."></div>

                    <button class="issue-save-btn">Save</button>
                </div>
            `;

    container.querySelector(".issue-save-btn").onclick = () => createIssue();
}

function createIssue() {
    const title = document.querySelector(".issue-edit-title").value;
    const status = document.querySelector(".issue-edit-status").value;
    const priority = document.querySelector(".issue-edit-priority").value;
    const dueDate = document.querySelector(".issue-edit-dueDate").value;
    const description = document.querySelector(".issue-edit-description").innerHTML;

    fetch("new-issue-servlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            title: title,
            status: status,
            priority: priority,
            dueDate: dueDate,
            description: description
        })
    })
        .then(async response => {
            if (!response.ok) {
                const errorText = await response.text();
                throw { status: response.status, message: errorText };
            }
            return response.text();
        })
        .then(() => {
            alert("Issue created successfully");
            showList();
        })
        .catch(err => {
            console.error("Error status:", err.status);
            console.error("Error message:", err.message);
            alert(`Failed to save issue: ${err.message} (code ${err.status})`);
        });
}

window.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const view = params.get("view");
    const id = params.get("id");

    if (view === "issue" && id) {
        showIssueDetails(id);
    } else if (view === "filter") {
        showFilter();
    } else if (view === "edit") {
        showEdit(id);
    } else if (view === "new") {
        showIssueCreator();
    } else {
        showList();
    }
});