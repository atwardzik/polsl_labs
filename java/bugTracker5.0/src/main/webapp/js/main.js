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