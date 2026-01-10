function showUser(username) {
    const container = document.getElementById("contents");
    container.innerHTML = "";

    fetch(`user-details?username=${username}`)
        .then(response => response.json())
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
        })
        .catch(err => {
            console.error("Error status:", err.status);
            console.error("Error message:", err.message);
            alert(`Failed to fetch user: ${err.message} (code ${err.status})`);
        });

    fetch(`list-issues?username=${username}`)
        .then(response => response.json())
        .then(issues => {
            const myIssuesLabel = document.createElement("p");
            myIssuesLabel.innerHTML = "<strong>My Issues:</strong>";
            container.appendChild(myIssuesLabel);

            issues.forEach(issue => {
                const card = document.createElement("a");
                card.className = "issues-card";
                card.href = `tracker.html?view=issue&id=${issue.id.substring(1)}`;
                card.className = "issues-card";
                card.innerHTML = `
                    <div class="issue-main">
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