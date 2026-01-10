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
    const items = ["abc", "def", "ghi"];

    assigneeInput.addEventListener("input", () => {
        const filtered = items.filter(i => i.startsWith(assigneeInput.value.toLowerCase()));
        assigneeSearch.innerHTML = "";

        filtered.forEach(item => {
            const li = document.createElement("li");
            li.textContent = item;
            li.addEventListener("click", () => {
               assigneeInput.value = item;
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
                throw {status: response.status, message: errorText};
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