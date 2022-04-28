
export function getAuthJWT() {
    const authJWT = parseAuthCookie();
    if(authJWT != null)
    return authJWT;
    else {
        // window.location = "/";
        throw new Error("You are not logged in");
    }
}

export function signOut(){
    deleteCookies();
    window.location = "/pages/login/login.html";
}

function parseAuthCookie() {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; auth=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function deleteCookies() {
    var cookies = document.cookie.split("; ");
    for (var c = 0; c < cookies.length; c++) {
        var d = window.location.hostname.split(".");
        while (d.length > 0) {
            var cookieBase = encodeURIComponent(cookies[c].split(";")[0].split("=")[0]) + '=; expires=Thu, 01-Jan-1970 00:00:01 GMT; domain=' + d.join('.') + ' ;path=';
            var p = location.pathname.split('/');
            document.cookie = cookieBase + '/';
            while (p.length > 0) {
                document.cookie = cookieBase + p.join('/');
                p.pop();
            };
            d.shift();
        }
    }
}