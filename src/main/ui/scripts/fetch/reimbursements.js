import { baseUrl, handleError } from "./fetch.js";
import { getAuthJWT } from '../cookieUtils.js';

const ReimbFetchMethods = (() => {
    const reimbursementsPath = `${baseUrl}/reimbursements`;

    return ({

        /**
         * Gets one/all reimbursements depending on if an argument is included
         * @param {integer} [reimbursementId = null] (optional) The id of the request to delete
         */
        get: async (reimbursementId = null) => {
            const config = {
                method: "GET",
                headers: {
                    Authorization: getAuthJWT()
                }
            };
            if(reimbursementId != null)
                config.headers["Reimbursement-Id"] = reimbursementId;

            return await fetch(
                reimbursementsPath, config
            ).then(handleError);
        },

        /**
         * Updates a reimbursement
         * @param {object} reimbursement 
         * @param {string} reimbursement.status 
         * @param {user} reimbursement.author
         * @param {user} reimbursement.resolver
         * @param {decimal} reimbursement.amount
         * @param {string} reimbursement.description 
         * @param {string} reimbursement.type 
         * @param {string} reimbursement.creationDate
         * @param {string} reimbursement.resolutionDate
         */
        put: async (reimbursement) => {
            return await fetch(
                reimbursementsPath, {
                    method: "PUT",
                    body: JSON.stringify(
                        reimbursement
                    ),
                    headers: {
                        Authorization: getAuthJWT()
                    }
                }
            ).then(handleError);
        },

        /**
         * Updates a reimbursement
         * @param {object} reimbursement 
         * @param {string} reimbursement.status 
         * @param {user} reimbursement.author
         * @param {user} reimbursement.resolver
         * @param {decimal} reimbursement.amount
         * @param {string} reimbursement.description 
         * @param {string} reimbursement.type 
         * @param {string} reimbursement.creationDate
         * @param {string} reimbursement.resolutionDate
         */
        post: async (reimbursement) => {
            return await fetch(
                reimbursementsPath, {
                    method: "POST",
                    body: JSON.stringify(reimbursement),
                    headers: {
                        Authorization: getAuthJWT()
                    }
                }
            ).then(handleError);
        },

        /**
         * Deletes a reimbursement with given id
         * @param {integer} reimbursementId The id of the request to delete
         */
        delete: async (reimbursementId) => {
            return await fetch(
                reimbursementsPath, {
                    method: "DELETE",
                    headers: {
                        Authorization: getAuthJWT(),
                        "Reimbursement-Id": reimbursementId
                    }
                }
            ).then(handleError);
        }
    });

})();

export default ReimbFetchMethods;


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