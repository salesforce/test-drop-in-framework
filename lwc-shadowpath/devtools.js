/*
 * Add a sidepanel to the Element Inspector main panel, which shows generated
 * cssSelector paths for elements protected by our LWC components' Synthetic Shadow DOM,
 * and generated Java queryStrings for updating obsolete Selenium selectors.
 */

let generateShadowJSPath = function() {
    /*
     * Add classes to element tagname, for use in selectors.
     */
    function getSelector(elem, long=true) {
        if (!elem.tagName)
            return elem.nodeName;
        let name = elem.tagName.toLowerCase();
        if (long) {
            elem.classList.forEach((cls) => {
                // Might need to add more to escape list.
                // Might be simpler to just drop class names with werid characters.
                name += '.' + cls.replace(/([{}])/g, '\\\\$1');
            });
        }
        return name;
    }

    /*
     * Is the element at the root of a components synthetic Shadow DOM tree?
     */
    function isShadowRoot(node) {
        return node instanceof ShadowRoot;
    }

    /*
     * Find the path from the element up to the document.
     * The path will include all LWC shadow boundaries.
     */
    function findPath(node) {
        if (!node) return;
        const path = [];
        path.push({
            elem: node,
            selector: getSelector(node)
        });
        while (node !== null && node !== document) {
            let parent = node.parentNode;
            if (isShadowRoot(parent)) {
                parent = parent.host;
                path.push({
                    elem: parent,
                    selector: getSelector(parent, false)
                });
            }
            node = parent;
        }
        return path;
    }

    function jsPathElementsToQuerySelector(jsPath) {
        console.log(jsPath);
        let expanded = [];
        jsPath.forEach(function(elem) {
            if(elem[1] == 0) {
                expanded.push(".querySelector('" + elem[0] + "')");
            } else if(elem[1] == '*') {
                expanded.push(".querySelectorAll('" + elem[0] + "')");
            } else {
                expanded.push(".querySelectorAll('" + elem[0] + "')[" + elem[1] + "]");
            }
        });
        return "document" + expanded.join(".shadowRoot");
    }

    function jsPathElementsToShadowPath(jsPath) {
        let selectors = [];
        jsPath.forEach(function(elem) {
            let selector = elem[0];
            if(elem[1] > 0) {
                selector += "[" + elem[1] + "]";
            }
            selectors.push(selector);
        });
        return selectors.join(" => ");
    }

    /**
     * Turn potential path into javascript selector.
     * Use eval to select between multiple possible top-down paths.
     */
    function jsPathFromPath(path) {
        if (!path || path.length === 0) return;
        let jsPath = [];
        // let jsPath = "document";
        for (let i = path.length - 1; i >= 0; i--) {
            const node = path[i];
            const selector = node.selector;
            const query = jsPathElementsToQuerySelector(jsPath.concat([[selector, '*']]));
            console.log(query);
            // const test = query + ".querySelectorAll('" + selector + "')";
            const possibilities = eval(query);
            if (possibilities.length === 0) {
                console.log("Error: Lost my way. No valid paths from " + query);
                throw "Lost my way. No valid paths from " + query;
            } else if (possibilities.length === 1) { // Selector gives an unique element
                // jsPath += ".querySelector('" + selector + "')";
                // jspathElements.push(".querySelector('" + selector + "')");
                jsPath.push([selector, 0]);
            } else {
                let found = false;
                for (let p = 0; p < possibilities.length; p++) {
                    if (possibilities[p] === node.elem) {
                        // jsPath += ".querySelectorAll('" + selector + "')[" + p + "]";
                        //jspathElements.push(".querySelectorAll('" + selector + "')[" + p + "]");
                        jsPath.push([selector, p]);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    console.log("Error: Could not find way to " + selector);
                    throw "Could not find way to " + selector;
                }
            }
            // We have not reached the element yet, so add shadowRoot.
            // if (i !== 0) {
            //     jsPath = jsPath + '.shadowRoot';
            // }
        }
        return jsPath;
    }

    // Find the optimal selector for currently selected element.
    const jsPath = jsPathFromPath(findPath($0));
    const jsPathSelector = jsPathElementsToQuerySelector(jsPath);
    let data = Object.create(null);
    data.tagname = getSelector($0, true);
    data.jsPath = jsPathSelector;
    // data.java = 'String queryString = "return ' + jspath.replace(/"/g, '\\"') + '";';
    data.java = 'String queryString = "return ' + jsPathSelector.replace(/"/g, "'") + '";';
    data.shadowPath = jsPathElementsToShadowPath(jsPath);
    return data;
};

/*
 * Add a sidepanel to the Element Inspector main panel.
 * Inspired by: https://chromium.googlesource.com/chromium/src/+/master/chrome/common/extensions/docs/examples/api/devtools/panels/chrome-query
 */
chrome.devtools.panels.elements.createSidebarPane(
    "LWC JS Path",
    function(sidebar) {
        function updateElementProperties() {
            sidebar.setExpression("(" + generateShadowJSPath.toString() + ")()");
        }
        updateElementProperties();
        chrome.devtools.panels.elements.onSelectionChanged.addListener(updateElementProperties);
    }
);
