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
                        
                        <div class="issue-edit-field">
                            <label>Assignee</label>
                            <input type="text" class="issue-edit-assignee">
                            <ul class="search-results"></ul>
                        </div>
                    </div>

                    <div class="issue-edit-description" data-placeholder="Enter issue description here..." contenteditable="true"></div>

                    <button id="issue-save-button" class="modifier">Save</button>
                </div>
            `;

    const assigneeInput = container.querySelector(".issue-edit-assignee");
    const assigneeSearch = container.querySelector(".search-results");
    const items = [];
    fetch("users-list")
        .then(response => response.json())
        .then(people => {
            people.forEach(person => items.push(person.username + "@" + person.fullName));
        })
        .catch(err => {
            console.error("Error status:", err.status);
        });

    assigneeInput.addEventListener("input", () => {
        const filtered = items.filter(i => i.substring(i.indexOf("@") + 1).toLowerCase().startsWith(assigneeInput.value.toLowerCase()));
        assigneeSearch.innerHTML = "";

        filtered.forEach(item => {
            const li = document.createElement("li");
            li.textContent = item;
            li.addEventListener("click", () => {
                assigneeInput.value = item;
                assigneeSearch.style.display = "none";
            });
            assigneeSearch.appendChild(li);
        });
        assigneeSearch.style.display = "block";
    });

    document.addEventListener("click", e => {
        if (!e.target.closest(".issue-edit-field")) {
            assigneeSearch.style.display = "none";
        }
    });

    container.querySelector(".modifier").onclick = () => createIssue();
}

function createIssue() {
    const title = document.querySelector(".issue-edit-title");
    const status = document.querySelector(".issue-edit-status");
    const priority = document.querySelector(".issue-edit-priority");
    const dueDate = document.querySelector(".issue-edit-dueDate");
    const assignee = document.querySelector(".issue-edit-assignee");
    const description = document.querySelector(".issue-edit-description");

    let errorFlag = false;
    if (title.value.length === 0) {
        title.className += " inputError";
        errorFlag = true;
    }

    if (description.innerHTML.trim().length === 0) {
        description.className += " inputError";
        errorFlag = true;
    }

    if (errorFlag) {
        alert("Title and Description fields are obligatory.");
        return;
    }

    fetch("new-issue-servlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            title: title.value,
            status: status.value,
            priority: priority.value,
            dueDate: dueDate.value,
            assignee: assignee.value.substring(0, assignee.value.indexOf("@") - 1),
            description: description.innerHTML
        })
    })
        .then(async response => {
            if (!response.ok) {
                const errorText = await response.text();
                throw {status: response.status, message: errorText};
            }
            return response.json();
        })
        .then(issue => {
            window.location.replace(`tracker.html?view=issue&id=${issue.id.substring(1)}`);
        })
        .catch(err => {
            console.error("Error status:", err.status);
            console.error("Error message:", err.message);
            alert(`Failed to save issue: ${err.message} (code ${err.status})`);
        });
}