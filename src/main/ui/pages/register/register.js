import auth from '../../scripts/fetch/auth.js';
import { isInputsValid } from '../../scripts/validation.js';

document.querySelector("#registerButton").addEventListener("click", () => {
    registerEmployee();
});
document.querySelector("#cancelButton").addEventListener("click", () => {
    window.location = "../login/login.html";
});

const errorHolder = document.querySelector(".errorHolder");


async function registerEmployee() {

    if(!isInputsValid()){
        errorHolder.innerText = "Can't register while some inputs are invalid."
        return;
    }
    
    const user = {};
    user.username = document.querySelector("#username").value;
    user.password = document.querySelector("#password").value;
    user.firstName = document.querySelector("#firstName").value;
    user.lastName = document.querySelector("#lastName").value;
    user.email = document.querySelector("#email").value;

    const address = document.querySelector("#address").value;
    const phoneNumber = document.querySelector("#phoneNumber").value;
    if (address != '')
        user.address = address;
    if (phoneNumber != '')
        user.phoneNumber = phoneNumber;

    auth.registerEmployee(user)
        .then( () => {
            window.location = "../home/home.html";
        })
        .catch(err => {
            errorHolder.innerText = err.message;
        });
}