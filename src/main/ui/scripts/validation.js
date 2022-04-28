export function isInputsValid(){
    const invalidInputs = document.querySelectorAll("input:invalid, select:invalid, textarea:invalid");

    if(invalidInputs.length > 0){
        invalidInputs.forEach( el => {
            el.style.borderColor = "red";
        });
        return false;
    }
    return true;
}