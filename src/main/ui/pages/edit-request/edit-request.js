import { signOut } from '../../scripts/cookieUtils.js';
import usersReimbursementsFetch from '../../scripts/fetch/users-reimbursements.js';
import { isInputsValid } from '../../scripts/validation.js';



window.onload = function() {
    document.querySelector("#signOut").addEventListener("click", () => {
        signOut();
    });
    document.querySelector("#confirmButton").addEventListener("click", () => {
        editRequest();
    });
    
    const errorHolder = document.querySelector(".errorHolder");
    const reimbursementIdToEdit = new URLSearchParams(window.location.search).get("reimbId");
    let reimbToEdit; // full reimbursement object

    // Form elements
    const typeEl = document.querySelector("#type");
    const descriptionEl = document.querySelector("#description");
    const amountEl = document.querySelector("#amount");

    usersReimbursementsFetch.get(me.id, reimbursementIdToEdit)
        .then(resp => {
            reimbToEdit = resp.data;
            typeEl.value = reimbToEdit.type;
            descriptionEl.value = reimbToEdit.description;
            amountEl.value = reimbToEdit.amount;
        })
        .catch(err => {
            errorHolder.innerText = err.message;
        });

    function editRequest(){
        if(!isInputsValid()){
            errorHolder.innerText = "Correct invalid fields and try again.";
            return;
        }
        reimbToEdit.type = typeEl.value;
        reimbToEdit.amount = amountEl.value;
        reimbToEdit.description = descriptionEl.value;
        
        usersReimbursementsFetch.put(reimbToEdit)
            .then(() => {
                window.location = "/pages/home/home.html";
            })
            .catch(err => {
                errorHolder.innerText = err.message;
            });
    }
}