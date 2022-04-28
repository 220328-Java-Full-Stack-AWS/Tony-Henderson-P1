import { baseUrl, handleError } from "./fetch.js";
import { getAuthJWT } from '../cookieUtils.js';

const UsersFetchMethods = ( () => {
    const usersReimbursementsPath = `${baseUrl}/users/reimbursements`;

    return ({

        /**
         * Gets one/all reimbursements depending on if the reimbursementId argument is given
         * @param {integer} userId The id of the user to target
         * @param {integer} [reimbursementId = null] (optional) The id of the reimbursement to get
         */
        get: async (userId, reimbursementId = null) => {
            const config = {
                method: "GET",
                headers: {
                    Authorization: getAuthJWT(),
                    "User-Id": userId
                }
            };
            if(reimbursementId != null)
                config.headers["Reimbursement-Id"] = reimbursementId;

            return await fetch(
                usersReimbursementsPath, config
            ).then(handleError);
        },

        /**
         * Updates a reimbursement
         * @param {existingReimbursementToUpdate} reimbursementToUpdate 
         */
        put: async (reimbursementToUpdate) => {
            return await fetch(
                usersReimbursementsPath, {
                    method: "PUT",
                    headers: {
                        Authorization: getAuthJWT()
                    },
                    body: JSON.stringify(reimbursementToUpdate)
                }
            ).then(handleError);
        },

        /**
         * Creates a reimbursement request for the person invoking or if userId is given for
         * that user.
         * @param {newReimbursement} reimbursement The reimbursement to add
         * @param {integer} [userId = null] (optional) The id of the user to append a reimbursement
         */
        post: async (reimbursement, userId = null) => {
            const config = {
                method: "POST",
                headers: {
                    Authorization: getAuthJWT()
                },
                body: JSON.stringify(reimbursement)
            };
            if(userId != null)
                config.headers["User-Id"] = userId;

            return await fetch(
                usersReimbursementsPath, config
            ).then(handleError);
        },

        /**
         * userId = null --- Deletes specific reimbursement for requester 
         * reimbursementId = null --- Deletes all reimbursements for specified user
         * userId & reimbursementId = null --- Deletes all reimbursements for requester
         * @param {integer} [userId = null] (optional) The id of the user to target
         * @param {integer} [reimbursementId = null] (optional) The id of the reimbursement to delete
         */
        delete: async (userId = null, reimbursementId = null) => {
            const config = {
                method: "DELETE",
                headers: {
                    Authorization: getAuthJWT()
                }
            };
            if(userId != null)
                config.headers["User-Id"] = userId;
            if(reimbursementId != null)
            config.headers["Reimbursement-Id"] = reimbursementId;

            return await fetch(
                usersReimbursementsPath, config
            ).then(handleError);
        }

    });
})();

export default UsersFetchMethods;

/**
 * Defines a new reimbursement to be created
 * @typedef {Object} newReimbursement
 * @property {decimal} amount
 * @property {string} description
 * @property {string} type
 */

/**
 * Defines a new reimbursement to be created
 * @typedef {Object} existingReimbursementToUpdate
 * @property {integer} id
 * @property {string} status
 * @property {user} author
 * @property {user} resolver
 * @property {decimal} amount
 * @property {string} description
 * @property {string} type
 * @property {string} creationDate
 * @property {string} resolutionDate
 */

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