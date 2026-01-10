function showEdit(issueId) {
    fetch(`issue-details?id=${issueId}`)
        .then(response => response.json())
        .then(issueData => {
            const container = document.getElementById("contents");

            container.innerHTML = `
                <div class="issue-edit">
                    <input type="text" id="issue-edit-title" value="${issueData.title}">

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

                    <button class="modifier">Save</button>
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
