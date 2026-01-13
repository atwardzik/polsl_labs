function showUser(username) {
    const container = document.getElementById("contents");
    container.innerHTML = "";

    fetch(`user-details`)
        .then(async response => {
            if (response.status === 401 || response.status === 400) {
                alert(`Failed to fetch user: You are not logged in!`);
                window.location = 'index.jsp';
            }

            if (!response.ok) {
                throw new Error("Unexpected error");
            }

            return await response.json();
        })
        .then(userData => {
            container.innerHTML = `
                <div>
                    <h1>${userData.name} ${userData.surname}</h1>
                    <p><strong>Username:</strong> ${userData.username}</p>
                    <p><strong>Created on:</strong> ${userData.createdOn}</p>
                    <p><strong>Last seen on:</strong> ${userData.lastSeenOn}</p>
                </div>
            `;

            const rolesBox = document.createElement("div");
            const rolesLabel = document.createElement("label");
            rolesLabel.innerHTML = "<strong>Assigned Roles:</strong>";
            rolesBox.appendChild(rolesLabel);

            const roles = document.createElement("ul");
            roles.className = "userRoles";
            userData.roles.forEach(role => {
                const roleElement = document.createElement("li");
                roleElement.innerText = role;
                roles.appendChild(roleElement);
            });
            rolesBox.appendChild(roles);

            container.appendChild(rolesBox);
            fetch(`list-issues?username=${userData.username}`)
                .then(response => response.json())
                .then(issues => {
                    const myIssuesLabel = document.createElement("p");
                    myIssuesLabel.innerHTML = "<strong>My Issues:</strong>";
                    container.appendChild(myIssuesLabel);

                    issues.data.forEach(issue => {
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
                                <div title="${issues.locale[status]}" class="status-badge ${statusClass}"></div>
                                <div class="issue-list-id">${issue.id.substring(0,7)}</div>
                                <div class="issue-list-title">${issue.title}</div>
                            </div>
                            <div class="list-meta-info">
                                <div title="${issues.locale.author}" class="issue-list-author">${issue.author}</div>
                                <div title="${issues.locale.dueOn}" class="issue-list-dueDate">${issue.dueDate}</div>
                                <div title="${issues.locale.createdOn}" class="issue-list-dateCreated">${issue.dateCreated}</div>
                            </div>
                        `;
                        container.appendChild(card);
                    });
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        })
        .catch(err => {
            console.error("Error message:", err.message);
        });

}