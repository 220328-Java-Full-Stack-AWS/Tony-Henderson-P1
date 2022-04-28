import { signOut } from '../../scripts/cookieUtils.js';
import usersReimbursementsFetch from '../../scripts/fetch/users-reimbursements.js';



const tableBody = document.querySelector("#reimbursementsTable tbody");
const errorHolder = document.querySelector(".errorHolder");

window.onload = function() {

    if(me.role == "FINANCE_MANAGER")
        document.querySelector("nav ul").insertAdjacentHTML("afterbegin", 
        `<a href="/pages/admin-port/admin-port.html"><li>Process Requests</li></a>`);

    setTimeout( () => {
        populateTable();
    }, 100);

    document.querySelector("#signOut").addEventListener("click", () => {
        signOut();
    });

}

async function populateTable(){
    const myReimbursements = await usersReimbursementsFetch.get(me.id);

    for(let reimbursement of myReimbursements.data){

        let tr = `<tr reimbId=${reimbursement.id}>
        <td>${reimbursement.status}</td>
        <td>${reimbursement.type}</td>
        <td>$${reimbursement.amount}</td>
        <td>${reimbursement.description}</td>
        `;
        if (reimbursement.status == "PENDING"){
            tr += `<td><a href="/pages/edit-request/edit-request.html?reimbId=${reimbursement.id}">Edit</a> <a href="#" class="deleteLink" id="${reimbursement.id}">Delete</a></td></tr>`;
        }
        else 
            tr += "</tr>"

        tableBody.insertAdjacentHTML("beforeend", tr); 
    };

    function deleteRequest(reimbId) {
        usersReimbursementsFetch.delete(null, reimbId)
            .then( (resp) => {
                document.querySelector(`tr[reimbId='${reimbId}'`).remove();

                document.querySelector(".errorHolder").style.color = "green";
                errorHolder.innerText = resp.message;
                setTimeout( () => {
                    errorHolder.innerText = "";
                    document.querySelector(".errorHolder").style.color = "red";
                }, 2000)
            })
            .catch(err => {
                errorHolder.innerText = err.message;
                setTimeout( () => {
                    errorHolder.innerText = "";
                }, 3500)
            });
    }

    // Wire Event Handlers
    document.querySelectorAll(".deleteLink").forEach( el => {
        el.addEventListener("click", (event) => {
            deleteRequest(event.target.getAttribute("id"));
        });
    });
}