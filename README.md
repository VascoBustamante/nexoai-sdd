# NexoAI — Specification-Driven Development

Prototipo funcional desarrollado para el curso de **Diseño de Software**, aplicando el enfoque **Specification-Driven Development (SDD)** sobre el proyecto NexoAI.

## Descripción

NexoAI es una propuesta de asistente personal inteligente orientado a optimizar la organización del tiempo, actividades, tareas y comunicaciones de sus usuarios.

Para la aplicación práctica de SDD se seleccionó el caso de uso de negocio:

**CUN-01 — Gestionar agenda personal**

Dentro de este caso de uso se delimitó como alcance del prototipo el **registro de eventos en la agenda personal**.

---

## Objetivo del prototipo

Permitir que un usuario autenticado registre eventos en su agenda proporcionando:

- Título
- Descripción opcional
- Fecha y hora de inicio
- Fecha y hora de finalización

Antes del almacenamiento definitivo, el sistema valida la información, comprueba el intervalo temporal, detecta posibles superposiciones y solicita confirmación al usuario.

---

## Requisito funcional

### RF-01

El sistema debe permitir que un usuario autenticado registre un evento en su agenda personal proporcionando la información requerida del compromiso.

Antes del almacenamiento definitivo, el sistema debe validar la información, comprobar el intervalo temporal, consultar posibles superposiciones y presentar la operación al usuario para su confirmación.

---

## Reglas del prototipo

| ID | Regla |
|---|---|
| REG-01 | Solo un usuario autenticado y autorizado puede acceder a su agenda. |
| REG-02 | El título, inicio y finalización son obligatorios. La descripción es opcional. |
| REG-03 | La fecha y hora de finalización deben ser posteriores al inicio. |
| REG-04 | Si existe una superposición, el sistema debe advertir al usuario y permitirle confirmar o cancelar. |
| REG-05 | Cada evento registrado debe quedar asociado al perfil del usuario autenticado. |

---

## Criterios de aceptación

### CA-01 — Evento válido
Un evento con información válida puede ser evaluado, confirmado y posteriormente visualizado en la agenda.

### CA-02 — Superposición
Si existe una superposición con otro evento, el sistema muestra una advertencia y permite al usuario confirmar o cancelar.

### CA-03 — Intervalo inválido
Si la finalización es anterior o igual al inicio, la propuesta es rechazada y el evento no se registra.

### CA-04 — Cancelación
Si el usuario cancela una propuesta, esta se descarta y el evento no es persistido.

### CA-05 — Acceso protegido
La agenda solo puede ser utilizada después de una autenticación válida.

---

## Flujo SDD aplicado

```text
Necesidad
   ↓
CUN-01
   ↓
RF-01
   ↓
ESP-01
   ↓
REG-01 ... REG-05
   ↓
CA-01 ... CA-05
   ↓
Diseño
   ↓
Implementación
   ↓
Validación