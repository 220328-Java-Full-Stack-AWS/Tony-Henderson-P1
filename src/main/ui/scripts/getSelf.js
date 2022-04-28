getSelf()
    .then(resp => {
        me = resp.data;
    })
    .catch(err => {
        window.location = "/pages/login/login.html";
    });

async function getSelf() {
    return await fetch(
        "http://localhost:8080/p1/auth/user", {
            method: "GET",
            headers: {
                Authorization: getAuthJWT()
            }
        }
    ).then(handleError);
}

async function handleError(response) {
    if(!response.ok)
        throw await response.json();
    return await response.json();
}

function parseAuthCookie() {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; auth=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function getAuthJWT() {
    const authJWT = parseAuthCookie();
    if(authJWT != null)
        return authJWT;
    else {
        // window.location = "/";
        throw new Error("You are not logged in");
    }
}