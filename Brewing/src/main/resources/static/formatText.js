async function formatTime(string) {
    if (string === null || string === undefined) {
        return "No registered endtime";
    }

    const replaced = string.replace('T', ' | ');
    return replaced.split('.')[0].trim();
}
