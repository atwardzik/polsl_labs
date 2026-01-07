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
            editLink.href = `tracker.html?view=edit&id=${issueId}`;
            editLink.textContent = "Edit";
            const assignLink = document.createElement("a");
            assignLink.href = ``;
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

function showEdit(issueId) {
    fetch("issue-details?id=" + issueId)
        .then(response => response.json())
        .then(issueData => {
            const container = document.getElementById("contents");
            container.innerHTML = "";

            const form = document.createElement("div");
            form.className = "issue-edit";

            const title = document.createElement("input");
            title.type = "text";
            title.value = issueData.title;
            title.className = "issue-edit-title";

            const metaRow = document.createElement("div");
            metaRow.className = "issue-edit-meta";

            const createLabeledField = (labelText, field) => {
                const wrapper = document.createElement("div");
                wrapper.className = "issue-edit-field";

                const label = document.createElement("label");
                label.textContent = labelText;

                wrapper.append(label, field);
                return wrapper;
            };

            const status = document.createElement("select");
            status.className = "issue-edit-status";
            ["OPEN", "CLOSED", "REOPENED", "IN_PROGRESS"].forEach(s => {
                const o = document.createElement("option");
                o.value = s;
                o.textContent = s.replace("_", " ");
                if (s === issueData.status) o.selected = true;
                status.appendChild(o);
            });

            const priority = document.createElement("select");
            priority.className = "issue-edit-priority";
            ["LOW", "MEDIUM", "HIGH", "CRITICAL"].forEach(p => {
                const o = document.createElement("option");
                o.value = p;
                o.textContent = p;
                if (p === issueData.priority) o.selected = true;
                priority.appendChild(o);
            });

            const dueDate = document.createElement("input");
            dueDate.type = "date";
            dueDate.className = "issue-edit-dueDate";
            dueDate.value = issueData.dueDate;

            metaRow.append(
                createLabeledField("Status", status),
                createLabeledField("Priority", priority),
                createLabeledField("Due date", dueDate)
            );

            const description = document.createElement("div");
            description.className = "issue-edit-description";
            description.contentEditable = "true";
            description.innerHTML = issueData.description || "";

            const saveBtn = document.createElement("button");
            saveBtn.textContent = "Save";
            saveBtn.className = "issue-save-btn";
            saveBtn.onclick = () => saveIssue(issueId);

            form.append(title, metaRow, description, saveBtn);
            container.appendChild(form);
        });
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
            // optional refresh:
            // location.reload();
        })
        .catch(err => {
            console.error(err);
            alert("Failed to save issue");
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
    } else {
        showList();
    }
});