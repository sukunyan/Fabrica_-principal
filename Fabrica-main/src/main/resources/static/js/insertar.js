document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('form');
    const mensajeError = document.querySelector('.mensaje-error');

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const formData = new FormData(form);

        try {
            const response = await fetch('/insertar', {
                method: 'POST',
                body: formData
            });

            const data = await response.json();

            if (response.ok) {
                mostrarMensaje(data.mensaje || "Coche guardado correctamente", false);
                form.reset(); // limpia el formulario
            } else {
                mostrarMensaje(data.error || "Error al guardar el coche", true);
            }
        } catch (error) {
            console.error('Error al enviar:', error);
            mostrarMensaje("Error de conexión con el servidor", true);
        }
    });

    function mostrarMensaje(texto, esError) {
        mensajeError.textContent = texto;

        if (esError) {
            mensajeError.classList.remove('hidden');
        } else {
            mensajeError.style.background = 'rgba(0, 128, 0, 0.1)';
            mensajeError.style.borderLeft = '5px solid #2ecc71';
            mensajeError.style.color = '#2ecc71';
            mensajeError.classList.remove('hidden');
        }

		setTimeout(() => {
		    mensajeError.classList.add('hidden');
		    mensajeError.textContent = '';

		    if (!esError) {
		        mensajeError.style.background = '';
		        mensajeError.style.borderLeft = '';
		        mensajeError.style.color = '';
		    }
		}, 4000);
    }
	
	
	const imagenPerfil = document.getElementById("imagen-perfil");

	fetch("/usuario/imagenes")
	    .then(response => response.json())
	    .then(data => {
	        if (data.url && imagenPerfil) {
	            imagenPerfil.src = data.url;
	        }
	    })
	    .catch(error => {
	        console.error('Error al obtener la imagen:', error);
	    });
});
