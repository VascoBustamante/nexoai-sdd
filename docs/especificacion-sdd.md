# Especificación SDD — NexoAI

## 1. Propósito

Este documento contiene la especificación utilizada para dirigir el desarrollo
del prototipo funcional de NexoAI realizado para el curso de Diseño de Software.

La implementación se desarrolla siguiendo el enfoque
**Specification-Driven Development (SDD)**, utilizando la especificación como
referencia para el diseño, implementación y posterior validación del software.

La cadena utilizada en el proyecto es:

CUN-01 → RF-01 → ESP-01 → Reglas → Criterios de aceptación → Diseño → Implementación → Validación

---

## 2. Caso de uso de negocio

### CUN-01 — Gestionar agenda personal

**Objetivo:**  
Mantener organizados y actualizados los compromisos del usuario.

**Actor principal:**  
Usuario Personal.

**Trabajador del negocio:**  
Asistente Personal Inteligente.

**Entidades relacionadas:**
- Perfil de Usuario.
- Evento de Agenda.

### Flujo general del caso de uso

1. El usuario solicita gestionar un compromiso.
2. El Asistente identifica la información correspondiente.
3. Consulta los compromisos existentes.
4. Valida la operación solicitada.
5. Registra o actualiza el Evento de Agenda.
6. Comunica el resultado al usuario.

El prototipo no implementa la totalidad de CUN-01. Para la aplicación de SDD
se seleccionó específicamente la funcionalidad de **registro de eventos**.

---

## 3. Alcance del prototipo

El prototipo incluye:

- autenticación del usuario;
- acceso a la agenda personal;
- ingreso de título;
- descripción opcional;
- fecha y hora de inicio;
- fecha y hora de finalización;
- validación de los datos;
- validación del intervalo temporal;
- detección de superposiciones;
- generación de una propuesta temporal;
- confirmación o cancelación;
- registro del evento confirmado;
- asociación del evento con el perfil del usuario;
- consulta de los eventos registrados.

Quedan fuera del alcance:

- modificación de eventos;
- eliminación de eventos;
- calendarios externos;
- recordatorios y notificaciones;
- reconocimiento de voz;
- procesamiento mediante inteligencia artificial;
- coordinación de disponibilidad entre múltiples usuarios.

---

## 4. Requisito funcional

### RF-01 — Registrar evento en agenda personal

El sistema debe permitir que un usuario autenticado registre un evento en su
agenda personal proporcionando la información requerida del compromiso.

Antes del almacenamiento definitivo, el sistema debe validar la información,
comprobar el intervalo temporal, consultar posibles superposiciones y presentar
la operación al usuario para su confirmación.

---

## 5. Especificación funcional

### ESP-01 — Registro de evento en agenda personal

**Requisito relacionado:** RF-01  
**Caso de uso relacionado:** CUN-01  
**Actor:** Usuario Personal  
**Entidades:** Perfil de Usuario y Evento de Agenda.

### Entradas

| Campo | Obligatorio | Descripción |
|---|---|---|
| Título | Sí | Identifica el compromiso |
| Descripción | No | Información adicional |
| Inicio | Sí | Fecha y hora de inicio |
| Fin | Sí | Fecha y hora de finalización |

### Precondiciones

- P1: El usuario debe estar autenticado.
- P2: Debe existir un perfil asociado al usuario.
- P3: El usuario debe estar autorizado para operar sobre su propia agenda.
- P4: Debe proporcionar información suficiente para evaluar el evento.

### Flujo principal

1. El usuario accede a su agenda.
2. Ingresa los datos del evento.
3. El sistema valida los campos obligatorios.
4. El sistema valida el intervalo temporal.
5. El sistema consulta los eventos existentes.
6. Se determinan posibles superposiciones.
7. El sistema genera una propuesta temporal.
8. Si existe superposición, se muestra una advertencia.
9. El sistema presenta la propuesta al usuario.
10. El usuario confirma o cancela.
11. Si confirma, el evento se registra asociado a su perfil.
12. La agenda se actualiza y se comunica el resultado.

---

## 6. Regla de superposición

Para un nuevo intervalo:

`[In, Fn)`

y un evento existente:

`[Ie, Fe)`

existe una superposición cuando:

`In < Fe AND Fn > Ie`

La existencia de una superposición no provoca automáticamente el rechazo del
evento en este prototipo.

El sistema informa al usuario y permite confirmar o cancelar la operación.

---

## 7. Reglas del prototipo

### REG-01 — Acceso autorizado

Solo un usuario autenticado y autorizado puede acceder a su agenda y realizar
operaciones sobre ella.

### REG-02 — Información requerida

El título, la fecha/hora de inicio y la fecha/hora de finalización son
obligatorios.

La descripción es opcional.

### REG-03 — Intervalo temporal válido

La fecha y hora de finalización deben ser estrictamente posteriores a la fecha
y hora de inicio.

### REG-04 — Superposición

Si existe una superposición con otro evento, el sistema debe informar al
usuario.

La advertencia no bloquea automáticamente el registro. El usuario puede
confirmar o cancelar la operación.

### REG-05 — Propiedad del evento

Todo evento confirmado debe quedar asociado al perfil correspondiente al
usuario autenticado.

---

## 8. Criterios de aceptación

### CA-01 — Registro válido

**DADO** que el usuario se encuentra autenticado  
**Y** proporciona información válida para un evento  
**CUANDO** evalúa y confirma la propuesta  
**ENTONCES** el evento debe registrarse  
**Y** debe aparecer en su agenda.

---

### CA-02 — Superposición

**DADO** que existe un evento previamente registrado  
**Y** el nuevo evento se superpone temporalmente con este  
**CUANDO** el usuario evalúa la propuesta  
**ENTONCES** el sistema debe mostrar una advertencia de conflicto  
**Y** debe permitir confirmar o cancelar la operación.

---

### CA-03 — Intervalo inválido

**DADO** que el usuario introduce un evento  
**CUANDO** la finalización es anterior o igual al inicio  
**ENTONCES** el sistema debe rechazar la propuesta  
**Y** el evento no debe registrarse.

---

### CA-04 — Cancelación

**DADO** que existe una propuesta válida pendiente de confirmación  
**CUANDO** el usuario selecciona cancelar  
**ENTONCES** la propuesta debe descartarse  
**Y** el evento no debe registrarse.

---

### CA-05 — Acceso protegido

**DADO** que un usuario intenta acceder a la funcionalidad  
**CUANDO** no dispone de una autenticación válida  
**ENTONCES** el sistema debe impedir el acceso a la agenda.

---

## 9. Correspondencia con la implementación

| Elemento | Componente principal |
|---|---|
| REG-01 | `SecurityConfig` y controladores |
| REG-02 | `EventoRequest` |
| REG-03 | `EventoService` |
| REG-04 | `EventoService`, `EventoRepository`, `EventoController`, `agenda.html` |
| REG-05 | `EventoService`, `PerfilUsuarioRepository`, `Evento` |
| Confirmación y cancelación | `PropuestaEventoService`, `EventoController` |
| Interfaz | `agenda.html` |
| Persistencia | Spring Data JPA y PostgreSQL |

---

## 10. Endpoints principales

La implementación utiliza los siguientes endpoints para la funcionalidad:

| Método | Endpoint | Función |
|---|---|---|
| GET | `/api/eventos` | Consultar eventos del usuario |
| POST | `/api/eventos/evaluar` | Evaluar una nueva propuesta |
| POST | `/api/eventos/confirmar/{token}` | Confirmar y registrar una propuesta |
| POST | `/api/eventos/cancelar/{token}` | Cancelar una propuesta |

La separación entre `/evaluar` y `/confirmar` materializa la decisión de que
una propuesta evaluada no equivale todavía a un evento persistido.

---

## 11. Propuestas temporales

`PropuestaEventoService` administra las propuestas pendientes de confirmación.

Cada propuesta:

- posee un identificador UUID;
- pertenece al usuario que la generó;
- posee un periodo limitado de validez;
- puede ser confirmada;
- puede ser cancelada.

Las propuestas se mantienen temporalmente en memoria para el alcance de este
prototipo.

Por esta razón, no constituyen un mecanismo de persistencia diseñado para un
entorno de producción.

---

## 12. Validación

La implementación fue validada mediante pruebas funcionales manuales y pruebas
automatizadas.

### Pruebas automatizadas

`EventoServiceTest` contiene pruebas relacionadas con:

- intervalo válido;
- finalización anterior al inicio;
- inicio y finalización iguales;
- detección de superposición;
- ausencia de superposición;
- registro de un evento confirmado.

`PropuestaEventoServiceTest` contiene pruebas relacionadas con:

- creación de propuestas;
- recuperación por el propietario;
- rechazo de acceso desde otro usuario;
- identificadores inexistentes;
- eliminación de propuestas;
- generación de identificadores diferentes.

También se utiliza `NexoaiApplicationTests` para verificar la carga del
contexto de la aplicación.

Resultado obtenido:

```text
Tests run: 13
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS