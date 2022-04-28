import { getAuthJWT } from '../cookieUtils.js';
import { baseUrl, handleError } from './fetch.js';

/**
 * Contains auth fetch methods.
 */
const AuthFetchMethods = (() => {
    const registerPath = `${baseUrl}/auth/register`;
    const authUserPath = `${baseUrl}/auth/user`;

    return ({

        /**
         * Gets self
         */
        getSelf: async () => {
            return await fetch(
                authUserPath, {
                    method: "GET",
                    headers: {
                        Authorization: getAuthJWT()
                    }
                }
            ).then(handleError);
        },

        /**
         * Attempts to verify a user
         * @param {string} username 
         * @param {string} password 
         */
        login: async (username, password) => {
            console.log(username, password);
            console.log(JSON.stringify({
                username: username,
                password: password
            }))
            return await fetch(
                authUserPath, {
                    method: "POST",
                    body: JSON.stringify({
                        username: username,
                        password: password
                    })
                }
            )
            .then( res => {
                const cookieHeader = res.headers.get("setcookie");
                document.cookie = `auth=${cookieHeader}; Max-Age=86400; Path=/;`;
                return res;
            })
            .then(handleError);
        },

        /**
         * Register an employee.
         * @param {object} user - The user to register.
         * @param {string} user.username
         * @param {string} user.password
         * @param {string} user.firstName
         * @param {string} user.lastName
         * @param {string} user.email
         * @param {string} [user.address]
         * @param {string} [user.phoneNumber]
         */
        registerEmployee: async (user) => {
            return await fetch(
                registerPath, {
                    method: "POST",
                    body: JSON.stringify(user)
                }
            ).then( res => {
                const cookieHeader = res.headers.get("setcookie");
                document.cookie = `auth=${cookieHeader}; Max-Age=86400; Path=/;`;
                return res;
            }).then(handleError);
        }
    });
})();

export default AuthFetchMethods;