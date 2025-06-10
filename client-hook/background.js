chrome.runtime.onInstalled.addListener(() => {
  chrome.scripting.registerContentScripts([{
    matches: ["*://*/*"],
    js: ["content.js"]
  }]);
});
