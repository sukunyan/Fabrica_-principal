document.addEventListener("DOMContentLoaded", function() {
    const inputBuscar = document.querySelector(".buscar-container input");
    const tablaCoches = document.querySelector(".mostrarConsulta-container table tbody");

    // Cargar los coches desde el backend
    fetch("/gestionar/datos")
        .then(response => response.json())
        .then(coches => {
            if (coches.error) {
                console.error("Error:", coches.error);
                return;
            }

            // Agregar coches a la tabla
            coches.forEach(coche => {
                const fila = document.createElement("tr");
                fila.innerHTML = `
                    <td class="cod">${coche.cod}</td>
                    <td class="marca">${coche.marca}</td>
                    <td class="modelo">${coche.modelo}</td>
                    <td class="fecha">${coche.fecha}</td>
                    <td class="matricula">${coche.matricula}</td>
                    <td class="chasis">${coche.numChasis}</td>
                    <td>
                        <div class="botones-container">
                            <button class="btn-modificar"><i class="fas fa-edit"></i> Modificar</button>
                            <button class="btn-eliminar"><i class="fas fa-trash"></i> Eliminar</button>
                        </div>
                    </td>
                `;
                tablaCoches.appendChild(fila);

                // Funcionalidad del botón de "Eliminar"
                fila.querySelector(".btn-eliminar").addEventListener("click", function() {
                    const confirmar = confirm("¿Estás seguro de que deseas eliminar este coche?");
                    if (confirmar) {
                        // Obtener todos los datos de la fila
                        const codCoche = fila.querySelector(".cod").textContent;
                        const marca = fila.querySelector(".marca").textContent;
                        const modelo = fila.querySelector(".modelo").textContent;
                        const fecha = fila.querySelector(".fecha").textContent;
                        const matricula = fila.querySelector(".matricula").textContent;
                        const chasis = fila.querySelector(".chasis").textContent;

                        // Crear un objeto con todos los datos del coche
                        const cocheData = {
                            cod: codCoche,
                            marca: marca,
                            modelo: modelo,
                            fecha: fecha,
                            matricula: matricula,
                            numChasis: chasis
                        };

                        // Enviar los datos del coche al backend para eliminarlo
                        fetch("/gestionar/eliminar", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                            },
                            body: JSON.stringify(cocheData)  // Enviamos todos los datos del coche
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Error en la respuesta del servidor');
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                // Si la eliminación es exitosa, eliminamos la fila de la tabla
                                tablaCoches.removeChild(fila);
                            } else {
                                throw new Error(data.message || "Error al eliminar el coche");
                            }
                        })
                        .catch(error => {
                            console.error("Error al eliminar:", error);
                            alert("No se pudo eliminar el coche. Inténtelo de nuevo.");
                        });
                    }
                });

                // Añadir la funcionalidad de "Modificar"
                const btnModificar = fila.querySelector(".btn-modificar");
                let estadoEdicion = false;

                btnModificar.addEventListener("click", function() {
                    // Si ya está en modo edición
                    if (estadoEdicion) {
                        // Obtener los valores de los inputs
                        const inputs = fila.querySelectorAll("td input");
                        const nuevosValores = Array.from(inputs).map(input => input.value.trim());

                        // Validar que los campos no estén vacíos
                        if (nuevosValores.some(valor => valor === "")) {
                            alert("Todos los campos deben estar completos.");
                            return;
                        }

                        // Preparar datos para enviar al backend
                        const datosModificados = {
                            cod: fila.querySelector(".cod").textContent,
                            marca: nuevosValores[0],
                            modelo: nuevosValores[1],
                            fecha: nuevosValores[2],
                            matricula: nuevosValores[3],
                            numChasis: nuevosValores[4]
                        };

                        // Enviar datos al backend
                        fetch("/gestionar/actualizar", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                            },
                            body: JSON.stringify(datosModificados)
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Error en la respuesta del servidor');
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                // Actualizar celdas con nuevos valores
                                const celdas = fila.querySelectorAll("td:not(:first-child):not(:last-child)");
                                celdas.forEach((celda, index) => {
                                    celda.innerHTML = nuevosValores[index];
                                });

                                // Restaurar estado de edición
                                btnModificar.innerHTML = '<i class="fas fa-edit"></i> Modificar';
                                estadoEdicion = false;
                            } else {
                                throw new Error(data.message || "Error al actualizar los datos");
                            }
                        })
                        .catch(error => {
                            console.error("Error al guardar:", error);
                            alert("No se pudo guardar el registro. Inténtelo de nuevo.");
                        });
                    } else {
                        // Salir de cualquier otra fila en edición
                        document.querySelectorAll(".botones-container .btn-modificar").forEach(boton => {
                            // Si el botón no es el actual, restauramos el estado
                            if (boton !== btnModificar) {
                                const filaEnEdicion = boton.closest("tr");
                                const celdas = filaEnEdicion.querySelectorAll("td:not(:first-child):not(:last-child)");
                                celdas.forEach(celda => {
                                    // Restauramos los valores originales
                                    const valorOriginal = celda.querySelector("input")?.getAttribute("data-original-value");
                                    celda.innerHTML = valorOriginal || celda.textContent;
                                });

                                // Restauramos el botón de "Modificar"
                                boton.innerHTML = '<i class="fas fa-edit"></i> Modificar';
                            }
                        });

                        // Entrar en modo edición
                        btnModificar.innerHTML = '<i class="fas fa-save"></i> Guardar';
                        
                        // Convertir celdas a inputs
                        const celdas = fila.querySelectorAll("td:not(:first-child):not(:last-child)");
                        celdas.forEach(celda => {
                            const valor = celda.textContent;
                            celda.innerHTML = `<input type="text" value="${valor}" data-original-value="${valor}">`;
                        });

                        estadoEdicion = true;
                    }
                });

            });

            // Filtrar coches mientras se escribe
            inputBuscar.addEventListener("input", function() {
                const filtro = inputBuscar.value.toLowerCase().trim();

                document.querySelectorAll(".mostrarConsulta-container table tbody tr").forEach(fila => {
                    let mostrar = false;

                    // Si el filtro tiene ":", se intenta buscar en una columna específica
                    if (filtro.includes(":")) {
                        const [clave, valorFiltro] = filtro.split(":").map(str => str.trim());

                        // Verifica si la clave es válida y filtra
                        if (["cod", "marca", "modelo", "fecha", "matricula", "chasis"].includes(clave)) {
                            const valorCelda = fila.querySelector(`.${clave}`)?.textContent.toLowerCase();

                            if (valorCelda) {
                                mostrar = valorCelda.includes(valorFiltro);
                            } else {
                                mostrar = false;
                            }
                        }
                    } else {
                        // Búsqueda general en toda la fila
                        const valoresFila = Array.from(fila.querySelectorAll("td")).map(td => td.textContent.toLowerCase());
                        mostrar = valoresFila.some(valor => valor.includes(filtro));
                    }

                    fila.style.display = mostrar ? "" : "none";
                });
            });
        })
        .catch(error => console.error("Error al cargar los coches:", error));
});
