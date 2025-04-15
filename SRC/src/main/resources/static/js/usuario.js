document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modal-confirmacion");
    const overlay = document.getElementById("overlay");
    const passwordInput = document.getElementById("passwordConfirmacion");
    const confirmarBtn = document.getElementById("confirmarBtn");
    const cancelarBtn = document.getElementById("cancelarBtn");

    let formActual = null;

    document.querySelectorAll("form").forEach(form => {
        form.addEventListener("submit", e => {
            e.preventDefault();
            formActual = form;
            mostrarModal();
        });
    });
	
	fetch("/usuario/datos")
	    .then(res => res.json())
	    .then(datos => {
	        if (datos.usuario) {
	            document.getElementById("nombre").textContent = datos.usuario;
	        }
	    })
	    .catch(error => console.error("Error al obtener datos de usuario:", error));

    confirmarBtn.addEventListener("click", () => {
        const password = passwordInput.value;
        if (!password.trim()) {
            alert("Introduce tu contraseña actual");
            return;
        }

        const hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.name = "password_obl";
        hidden.value = password;
        formActual.appendChild(hidden);

        ocultarModal();
        formActual.submit();
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

    // Rellenar nombre de usuario desde el backend
    fetch("/usuario/datos")
        .then(res => res.json())
        .then(datos => {
            if (datos.usuario) {
                document.getElementById("nombre").textContent = datos.usuario;
            }
        })
        .catch(error => console.error("Error al obtener datos de usuario:", error));
});
