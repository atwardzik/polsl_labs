window.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const view = params.get("view");
    const id = params.get("id");
    const username = params.get("username");

    if (view === "issue" && id) {
        showIssueDetails(id);
    } else if (view === "filter") {
        showFilter();
    } else if (view === "edit" && id) {
        showEdit(id);
    } else if (view === "new") {
        showIssueCreator();
    } else if (view === "user" && username) {
        showUser(username);
    } else {
        showList();
    }
});