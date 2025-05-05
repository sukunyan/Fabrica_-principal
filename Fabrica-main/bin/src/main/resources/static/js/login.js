document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('form');
    const mensajeError = document.querySelector('.mensaje-error'); // Asegúrate de que esta clase esté definida en tu HTML

    form.addEventListener("submit", async function (event) {
        event.preventDefault(); // Evita el envío tradicional del formulario

        // Capturar los datos del formulario
        const formData = new FormData(form);
        const data = new URLSearchParams(formData);

        try {
            const response = await fetch(form.action, {
                method: "POST",
                body: data
            });

            const result = await response.json(); // Leer la respuesta del servidor

            if (!response.ok) {
                mensajeError.textContent = result.error || "Error desconocido"; // Insertar mensaje de error
                mensajeError.classList.remove("hidden"); // Mostrar el error
            } else {
                window.location.href = "/panel"; // Redirigir si el login es exitoso
            }
        } catch (error) {
            mensajeError.textContent = "Error al conectar con el servidor"; // Mostrar mensaje de error si hay un fallo en la conexión
            mensajeError.classList.remove("hidden"); // Mostrar el error
        }
    });
});
