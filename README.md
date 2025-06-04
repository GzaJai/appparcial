# Aplicación para parcial 
#### En este repositorio se encuentra la entrega para el parcial de la catedra de Programación II con la profesora Naveda.

> [!NOTE] 
> La aplicación se debe ejecutar desde el Main.java ubicado en ***com.utn.appparcial.Main***

Al ejecutar la app esta crea la base de datos y crea una entidad Profesor. Esta entidad va a ser útil para el testeo de la misma. 

Una vez inicia la app se debe iniciar sesión, las credenciales para iniciar sesión como profesor administrador son:

**email:** admin@utn.com

**contraseña:** admin123

Una vez que se inició la sesión se muestra el menú de opciones como profesor. Donde se pueden llevar a cabo varias tareas, las cuales son: 

 - ***Asignarme a una materia:*** acá el profesor puede darse de alta en alguna de las materias disponibles, esto con el fin de poder crear exámenes para esa materia, cargar las calificaciones de sus alumnos o ver el estado de sus alumnos.
 - ***Crear examen:*** el profesor puede crear un nuevo examen, la aplicación le solicitara al profesor que seleccione la materia a la que pertenece este examen (solo se podrá seleccionar una materia en la que el profesor este dado de alta). A continuación se le solicitara que ingrese el titulo del examen y la fecha del mismo (con formato *AAAA-MM-DD*).
 - ***Poner nota a alumno:*** se muestra los exámenes que se han tomado y el profesor selecciona por *ID* de cual desea agregar una nueva nota. Luego se muestra la lista de alumnos registrados en esa materia y tambien se debe seleccionar por *ID* a cual se le agregara la nota, se solicita al profesor que ingrese la nota y si es correcto se da aviso por un mensaje.
 - ***Registrar nuevo alumno:*** se solicita al profesor que ingrese los datos del alumno: *Nombre, Apellido, Email y Contraseña*, luego se selecciona en que materia se desea dar de alta al alumno.
 - ***Ver alumnos:*** Muestra una lista de los alumnos que pertenecen a las materias del profesor. Se puede seleccionar un alumno mediante su *ID* de usuario para ver más información del mismo, o se puede volver al menú principal si el numero ingresado es *0*
  - ***Salir:*** se sale de la aplicación.

Tambien la aplicación crea algunos alumnos para que se pueda ingresar y ver como es su menú.
Para ingresar como alumno están las siguientes credenciales: 
 - Alumno 1: 
 -- **email:** juan.perez@utn.com
 --**contraseña:** 1234
 - Alumna 2: 
 -- **email:** lucia.gomez@utn.com
 --**contraseña:** 1234

Una vez en el menú de alumno se pueden seleccionar dos opciones: 
- ***Ver materias:*** Se listan las materias en las que esta inscripto el alumno.
- ***Ver notas:*** Se muestra la materia, el titulo del examen y la nota del mismo para el alumno.
