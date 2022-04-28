import auth from '../../scripts/fetch/auth.js';

const usernameInput = document.querySelector("#username");
const passwordInput = document.querySelector("#password");
const errorHolder = document.querySelector(".errorHolder");

document.querySelector("#loginButton").addEventListener('click', async () => {
    await submitForm();
});

async function submitForm() {
    // Validate form
    await auth.login(usernameInput.value, passwordInput.value)
    .then(res => {
        window.location = "../home/home.html";
    })
    .catch(err => {
        errorHolder.innerText = err.message;
    });


}