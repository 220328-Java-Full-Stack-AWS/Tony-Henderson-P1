import { getAuthJWT } from "../cookieUtils.js";
import { baseUrl, handleError } from "./fetch.js";

const UsersFetchMethods = ( () => {
    const usersPath = `${baseUrl}/users`;

    return ({

        /**
         * Gets one/all users depending on if an argument is included
         * @param {integer} [userId = null] The id of the user to look for
         */
        get: (userId = null) => {
            const config = {
                method: "GET",
                headers: {
                    Authorization: getAuthJWT()
                }
            };
            if(userId != null)
                config.headers["User-Id"] = userId;
            
            return await fetch(
                usersPath, config
            ).then(handleError);
        },

        /**
         * Updates a user
         * @param {user} user 
         */
        put: (user) => {
            return await fetch(
                usersPath, JSON.stringify(user)
            ).then(handleError);
        }

    });
})();

export default UsersFetchMethods;

/**
 * Defines a user
 * @typedef {Object} user
 * @property {integer} id
 * @property {string} username
 * @property {string} role
 * @property {string} firstName
 * @property {string} lastName
 * @property {string} email
 * @property {string} phoneNumber
 * @property {string} address
 */