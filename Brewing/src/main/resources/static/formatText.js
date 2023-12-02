function formatTime(string) {
    const replaced = string.replace('T', ' | ');
    return replaced.split('.')[0].trim();
}