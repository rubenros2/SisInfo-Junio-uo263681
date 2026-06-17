# Historias de usuario (Título, descripción y criterios de aceptación)

- **HU-01: Recogida de información vía telefónica**
  - Descripción: Como teleoperador, quiero poder recoger la información del envío vía telefónica para poder gestionar los envíos de los clientes.

- **HU-02: Recogida de información desde la web**

Descripción

Como usuario registrado, quiero poder introducir la información de un envío desde la web para solicitar su gestión y conocer previamente el coste asociado al servicio.

Criterios de aceptación

El usuario inicia sesión en el sistema y accede a la opción "Iniciar proceso de envío".

Al seleccionarla, el sistema muestra un formulario para introducir los siguientes datos:

a. Datos del destinatario

* Nombre
* DNI

b. Datos del paquete

* Categoría de tamaño (Pequeño, Mediano o Grande)
* Peso

c. Datos del envío

* Tipo de recogida:
  * Recogida en domicilio
  * Entrega en oficina/punto de recogida por parte del cliente
* Lugar de recogida (si procede)
* Tipo de entrega:
  * Entrega a domicilio
  * Entrega en oficina/punto de recogida
* Lugar de entrega

Una vez introducida toda la información requerida, el sistema calcula automáticamente el precio del envío aplicando las tarifas vigentes y muestra al usuario un resumen con:

* Datos del destinatario
* Datos del paquete
* Datos del envío
* Importe total calculado

El usuario debe confirmar la aceptación del importe para continuar con la contratación.

Tras la confirmación:

* Se genera un número único de seguimiento.
* El envío queda registrado con estado "Envío solicitado".
* Se genera un justificante que incluye:
  * Número de seguimiento.
  * Fecha y hora de creación.
  * Datos del destinatario.
  * Categoría y peso del paquete.
  * Datos del envío.
  * Importe total calculado.

El justificante queda disponible para consulta y descarga por parte del usuario.

Estimación: 5 días

- **HU-03: Recogida de información desde servicio web**

Descripción

Como empresa externa, quiero poder enviar la información de un envío mediante una API para gestionar envíos desde mis propios sistemas.

Criterios de aceptación

La empresa externa realiza una petición al API del sistema enviando un documento JSON con la información necesaria para procesar el envío.

El JSON deberá contener:

a. Datos de la empresa

* Nombre de la empresa
* CIF

b. Datos del destinatario

* Tipo de destinatario (empresa o particular)
* Nombre o razón social
* DNI o CIF

c. Datos del paquete

* Dimensiones
* Peso

d. Datos del envío

* Tipo de recogida:
  * Recogida en domicilio
  * Entrega en oficina/punto de recogida por parte del remitente
* Lugar de recogida (si procede)
* Tipo de entrega:
  * Entrega a domicilio
  * Entrega en oficina/punto de recogida
* Lugar de entrega

El sistema validará que:

* El JSON tiene un formato válido.
* Todos los campos obligatorios han sido informados.
* Los datos cumplen las reglas de negocio establecidas.

Si la validación es correcta:

* El sistema registra el envío.
* Se genera un número único de seguimiento.
* El envío queda en estado "Envío solicitado".

La respuesta del API incluirá:

* Resultado de la operación.
* Número de seguimiento generado.
* Estado inicial del envío.
* Importe calculado según las tarifas vigentes.

Si la validación falla, el sistema devolverá los errores detectados.

Estimación: 5 días

- **HU-04: Transportista accede a la información del envío en la recogida del paquete**

Descripción

Como transportista, quiero consultar las recogidas que tengo asignadas desde mi terminal portátil para poder realizar la recogida de los paquetes.

Criterios de aceptación

El transportista inicia sesión en el sistema mediante sus credenciales.

Al acceder, encontrará una opción denominada "Recogidas asignadas".

Al seleccionarla, el sistema mostrará la lista de envíos pendientes de recogida asignados al transportista autenticado.

Para cada envío se mostrará:

* Número de seguimiento.
* Nombre del remitente.
* Dirección de recogida.
* Tipo de entrega.
* Fecha prevista de recogida.

El transportista podrá seleccionar un envío para consultar su detalle.

Al acceder al detalle se mostrará:

a. Datos del remitente

* Nombre
* DNI

b. Datos del destinatario

* Nombre
* DNI

c. Datos del paquete

* Dimensiones
* Peso

d. Datos del envío

* Tipo de recogida
* Tipo de entrega
* Lugar de entrega

Una vez recogido el paquete, el transportista pulsará el botón "Confirmar recogida".

Tras la confirmación:

* El envío cambiará al estado "Recogido".
* Se registrará la fecha y hora de la recogida.
* La acción quedará reflejada en el historial del envío.

Estimación: 3 días

- **HU-05: Gerente asigna rutas y vehículos a los envíos**
  - Descripción: Como gerente, quiero poder asignar rutas y vehículos a los envíos en estado "En almacén/oficina" para los desplazamientos relativos a la recogida y entrega de los paquetes. No se requiere la asignación de rutas y vehículos a los pasos intermedios, ya que estos no son relevantes para el funcionamiento del SI en este punto.

- **HU-06: Operario de almacén gestiona la llegada de paquete al punto intermedio**

Descripción

Como operario de almacén, quiero registrar la llegada de un paquete a una instalación logística para verificar su estado y actualizar su ubicación dentro del proceso de transporte.

Criterios de aceptación

El operario inicia sesión en el sistema y accede a la opción "Gestionar llegadas".

A continuación introduce o selecciona el número de seguimiento del envío.

El sistema mostrará:

* Número de seguimiento.
* Remitente.
* Destinatario.
* Peso registrado.
* Última ubicación conocida.
* Estado actual del envío.

El operario deberá informar:

* Peso real del paquete.
* Estado del paquete:

  * Correcto.
  * Dañado.
  * Abierto.
  * Mojado.
  * Otro.

Si el peso registrado y el peso real presentan discrepancias significativas o el estado indicado no es correcto:

* El envío pasará al estado "Incidencia detectada".
* El sistema registrará el motivo de la incidencia.
* Se notificará automáticamente al remitente.

Si no existen incidencias:

* El operario pulsará el botón "Confirmar llegada".
* El sistema registrará la nueva ubicación.
* La llegada quedará reflejada en el historial del envío.

Estimación: 5 días

- **HU-07: Operario de almacen gestiona la salida de paquete del punto intermedio**

Descripción

Como operario de almacén, quiero gestionar la salida de un paquete desde la instalación logística actual para que continúe su ruta de transporte hacia su próximo destino.

Criterios de aceptación

El operario inicia sesión en el sistema y accede a la pestaña "Registrar Salida (HU-07)".

El sistema solicitará seleccionar el almacén de expedición actual desde un menú desplegable.

A continuación, el sistema mostrará una tabla con los envíos disponibles para salida desde el almacén seleccionado, mostrando:

* Número de seguimiento.
* Estado actual.
* Estado del paquete.

El operario seleccionará el envío que desea expedir. Al seleccionarlo, el sistema mostrará el detalle del siguiente tramo de su ruta:

* Estado físico del paquete.
* Próximo destino planificado.
* Vehículo asignado (Matrícula).
* ID del Transportista asignado.

Para que la expedición pueda ser autorizada, el sistema mostrará unas verificaciones de seguridad que deben cumplirse:

* Que el paquete se encuentre físicamente disponible.
* Que no existan incidencias abiertas que bloqueen el envío.
* Que exista un vehículo de transporte asignado para el siguiente tramo.

Si todas las comprobaciones son afirmativas, el sistema habilitará el botón de confirmación.

El operario pulsará el botón "Confirmar Salida (Expedir)".

Tras la confirmación:

* El sistema registrará la salida y el envío pasará a estar en estado "En tránsito".
* Se mostrará un mensaje de éxito confirmando la expedición.
* El historial del envío y su listado se actualizarán automáticamente para reflejar su nueva situación.

Estimación: 3 días
- **HU-08: Usuario consulta el estado de su envío**

Descripción

Como usuario, quiero consultar el estado de mis envíos para conocer su situación actual y el historial completo de movimientos realizados.

Criterios de aceptación

El usuario inicia sesión en el sistema y accede a la opción "Consultar envíos".

El sistema mostrará una lista de los envíos asociados al usuario ordenados por fecha de creación.

Para cada envío se mostrará:

* Número de seguimiento.
* Fecha de creación.
* Destinatario.
* Estado actual.
* Tipo de entrega.

Si el usuario no dispone de envíos, se mostrará un mensaje informativo.

El usuario podrá seleccionar cualquier envío mediante el botón "Consultar".

Al acceder al detalle se mostrará:

* Número de seguimiento.
* Remitente.
* Destinatario.
* Información del paquete.
* Tipo de recogida.
* Tipo de entrega.
* Estado actual.

Además, se mostrará un historial cronológico de eventos donde cada registro incluirá:

* Fecha.
* Hora.
* Estado.
* Ubicación.
* Observaciones relevantes.
* Responsable o medio de transporte asociado cuando corresponda.

Estimación: 3 días

- **HU-09: Usuario cambia el punto de recogida del paquete**
  - Descripción: Como usuario, quiero poder cambiar el punto de recogida del paquete para adaptarlo a mis necesidades.

- **HU-10: Usuario cambia la dirección de entrega del paquete**
  - Descripción: Como usuario, quiero poder cambiar la dirección de entrega del paquete para adaptarlo a mis necesidades.
  
- **HU-11: Confirmación de la entrega del envío**
  - Descripción: Como transportista u operario, quiero poder confirmar la entrega del envío para indicar que se ha recibido correctamente.

- **HU-12: Tarificación de los envíos**
  - Descripción: Como gerente, quiero poder establecer una tarifa base para los envíos y poder añadir cargos adicionales según el peso y el tamaño del paquete.
