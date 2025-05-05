document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('form');
    const mensajeError = document.querySelector('.mensaje-error');

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        
        const formData = new FormData(form);
        const data = new URLSearchParams(formData);

        try {
            const response = await fetch(form.action, {
                method: "POST",
                body: data
            });

            const result = await response.json();

            if (!response.ok) {
                mensajeError.textContent = result.error || "Error desconocido";
                mensajeError.classList.remove("hidden"); 
            } else {
                window.location.href = "/login";
            }
        } catch (error) {
            mensajeError.textContent = "Error al conectar con el servidor";
            mensajeError.classList.remove("hidden"); 
        }
    });
});
