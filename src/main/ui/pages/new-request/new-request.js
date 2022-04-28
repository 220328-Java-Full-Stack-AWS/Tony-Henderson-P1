import { signOut } from "../../scripts/cookieUtils.js";
import usersReimbursementsFetch from '../../scripts/fetch/users-reimbursements.js';
import { isInputsValid } from "../../scripts/validation.js";


window.onload = () => {

    const errorHolder = document.querySelector(".errorHolder");

    document.querySelector("#signOut").addEventListener("click", () => {
        signOut();
    });

    document.querySelector("#submitButton").addEventListener("click", () => {
        submitRequest();
    });
    
    
    function submitRequest(){
        if(!isInputsValid()){
            errorHolder.innerText = "Correct invalid fields and try again.";
            return;
        }

        const reimbursement = {};
        reimbursement.amount = document.querySelector("#amount").value;
        reimbursement.description = document.querySelector("#description").value;
        reimbursement.type = document.querySelector("#type").value;

        usersReimbursementsFetch.post(reimbursement)
            .then( () => {
                window.location = "/pages/home/home.html";
            })
            .catch( err => {
                errorHolder.innerText = err.message;
            });

    }
}