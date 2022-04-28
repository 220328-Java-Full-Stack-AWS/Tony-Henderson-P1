async function handleError(response) {
    if(!response.ok)
        throw await response.json();
    return await response.json();
}

export { handleError };