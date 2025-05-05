document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('form');
    const mensajeError = document.querySelector('.mensaje-error');
    const modal = document.getElementById("modal-confirmacion");
    const overlay = document.getElementById("overlay");
    const passwordInput = document.getElementById("passwordConfirmacion");
    const confirmarBtn = document.getElementById("confirmarBtn");
    const cancelarBtn = document.getElementById("cancelarBtn");

    if (form && confirmarBtn && cancelarBtn) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            mostrarModal();
        });

        confirmarBtn.addEventListener("click", async function () {
            const password = passwordInput.value.trim();
            if (!password) {
                alert("Introduce tu contraseña actual");
                return;
            }

            // Crea un FormData para enviar los datos del formulario
            const formData = new FormData(form);
            formData.append("password_obl", password);

            ocultarModal();

            try {
                const response = await fetch('/usuario', {
                    method: 'POST',
                    body: formData
                });

                const data = await response.json();

                if (response.ok) {
                    mostrarMensaje(data.mensaje || "Cambios guardados correctamente", false);
                    form.reset(); // Resetea el formulario
                } else {
                    mostrarMensaje(data.error || "Error al guardar los cambios", true);
                }
            } catch (error) {
                console.error('Error al enviar:', error);
                mostrarMensaje("Error de conexión con el servidor", true);
            }
        });

        cancelarBtn.addEventListener("click", () => {
            ocultarModal();
        });

        function mostrarModal() {
            modal.classList.remove("oculto");
            overlay.classList.remove("oculto");
            passwordInput.value = "";
            passwordInput.focus();
        }

        function ocultarModal() {
            modal.classList.add("oculto");
            overlay.classList.add("oculto");
        }
    } else {
        console.error("Uno o más elementos no se encuentran en el DOM");
    }

    function mostrarMensaje(texto, esError) {
        mensajeError.textContent = texto;

        if (esError) {
            mensajeError.classList.remove('hidden');
            mensajeError.style.background = 'rgba(255, 0, 0, 0.1)';
            mensajeError.style.borderLeft = '5px solid #e74c3c';
            mensajeError.style.color = '#e74c3c';
        } else {
            mensajeError.classList.remove('hidden');
            mensajeError.style.background = 'rgba(0, 128, 0, 0.1)';
            mensajeError.style.borderLeft = '5px solid #2ecc71';
            mensajeError.style.color = '#2ecc71';
        }

        // Ocultar mensaje después de 4 segundos
        setTimeout(() => {
            mensajeError.classList.add('hidden');
            mensajeError.style.background = '';
            mensajeError.style.borderLeft = '';
            mensajeError.style.color = '';
        }, 4000);
    }

    // Obtener la imagen de perfil actual
    const imagenesPerfil = document.querySelectorAll(".imagen-perfil");
    fetch("/usuario/imagenes")
        .then(response => response.json())
        .then(data => {
            if (data.url) {
                imagenesPerfil.forEach(img => {
                    img.src = data.url;
                });
            }
        })
        .catch(error => {
            console.error('Error al obtener la imagen:', error);
        });
});
