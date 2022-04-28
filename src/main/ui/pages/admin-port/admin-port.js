import { signOut } from '../../scripts/cookieUtils.js';
import ReimbFetchMethods from '../../scripts/fetch/reimbursements.js';
import reimbursementsFetch from '../../scripts/fetch/reimbursements.js';


const tableBody = document.querySelector("#reimbursementsTable tbody");
let reimbursements; // get data out
const errorHolder = document.querySelector(".errorHolder");

window.onload = function() {
    if(me.role != "FINANCE_MANAGER")
        window.location = "/pages/home/home.html";
    init();
    
    document.querySelector("#signOut").addEventListener("click", () => {
        signOut();
    });

    document.querySelector("#filterPending").addEventListener("click", () => {
        sortByPending();
    });
    document.querySelector("#filterApproved").addEventListener("click", () => {
        sortByApproved();
    });
    document.querySelector("#filterDenied").addEventListener("click", () => {
        sortByDenied();
    });
        
}
    
async function init(){
    reimbursements = await reimbursementsFetch.get();
    setTimeout(() => {
        populateTable(reimbursements);
    }, 100);
}

async function populateTable(reimbursements){
    tableBody.innerHTML = "";
    for(let reimbursement of reimbursements.data){
        let tr = `<tr reimbId="${reimbursement.id}">
        <td>${reimbursement.status}</td>
        <td>${reimbursement.type}</td>
        <td>$${reimbursement.amount}</td>
        <td>${reimbursement.description}</td>
        `;
        if (reimbursement.status == "PENDING"){
            tr += `<td><a href="#" class="approveLink" reimbId="${reimbursement.id}">Approve</a> <a href="#" class="denyLink" reimbId="${reimbursement.id}">Deny</a></td></tr>`;
        }
        else {
            tr += "</tr>";
        }

        tableBody.insertAdjacentHTML("beforeend", tr);
    }
    // Wire Event Handlers
    document.querySelectorAll(".denyLink").forEach( el => {
        el.addEventListener("click", (event) => {
            denyRequest(event.target.getAttribute("reimbId"));
        });
    })

    document.querySelectorAll(".approveLink").forEach( el => {
        el.addEventListener("click", (event) => {
            approveRequest(event.target.getAttribute("reimbId"));
        });
    })
}

async function approveRequest(reimbId){

    const res = await ReimbFetchMethods.get(reimbId);
    const reimbursement = res.data;

    reimbursement.status = "APPROVED";
    reimbursement.resolver = me;
    reimbursement.resolutionDate = new Date();

    ReimbFetchMethods.put(reimbursement)
        .then( resp => {
            successMessage(resp.message);
            
            const tr = document.querySelector(`tr[reimbId='${reimbId}']`);
            tr.querySelector("td:first-child").innerText = "APPROVED";
            tr.querySelector("td:last-child").innerHTML = "";
        })
        .catch(err => {
            errorMessage(err.message);
        });
}

async function denyRequest(reimbId){
    const res = await ReimbFetchMethods.get(reimbId);
    const reimbursement = res.data;

    reimbursement.status = "DENIED";
    reimbursement.resolver = me;
    reimbursement.resolutionDate = new Date();

    ReimbFetchMethods.put(reimbursement)
        .then( resp => {
            successMessage(resp.message);
            
            const tr = document.querySelector(`tr[reimbId='${reimbId}']`);
            tr.querySelector("td:first-child").innerText = "DENIED";
            tr.querySelector("td:last-child").innerHTML = "";
        })
        .catch(err => {
            errorMessage(err.message);
        });

}

function sortByPending(){
    reimbursements.data.sort((a, b) => {
        if(a.status == "PENDING" && b.status != "PENDING")
            return -1;
        else if (a.status == "PENDING" && b.status == "PENDING")
            return 0;
        else 
            return 1;
    });
    populateTable(reimbursements);
}
function sortByDenied(){
    reimbursements.data.sort((a, b) => {
        if(a.status == "DENIED" && b.status != "DENIED")
            return -1;
        else if (a.status == "DENIED" && b.status == "DENIED")
            return 0;
        else 
            return 1;
    });
    populateTable(reimbursements);
}
function sortByApproved(){
    reimbursements.data.sort((a, b) => {
        if(a.status == "APPROVED" && b.status != "APPROVED")
            return -1;
        else if (a.status == "APPROVED" && b.status == "APPROVED")
            return 0;
        else 
            return 1;
    });
    populateTable(reimbursements);
}

function errorMessage(message){
    errorHolder.innerText = message;
    setTimeout(() => {
        errorHolder.innerText = "";
    }, 2000);
}

function successMessage(message){
    errorHolder.style.color = "green";
    errorHolder.innerText = message;
    setTimeout(() => {
        errorHolder.innerText = "";
        errorHolder.style.color = "red";
        
    }, 2000);
}
