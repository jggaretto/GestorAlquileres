# Gestor de Alquileres

Sistema de escritorio desarrollado en Java para administrar alquileres inmobiliarios. Permite gestionar propietarios, inquilinos, propiedades, contratos y pagos, ademas de consultar reportes generales sobre recaudacion, deuda y estado de propiedades.

Proyecto realizado para la materia **Programacion II** de la **Universidad Nacional de Villa Mercedes (UNViMe)**, carrera **Programador Universitario de Sistemas**.

## Funcionalidades principales

- Inicio de sesion con usuario y contrasena.
- ABM de propietarios, inquilinos, propiedades, contratos y pagos.
- Busquedas por datos relevantes en cada modulo.
- Registro de contratos asociados a una propiedad y un inquilino.
- Control de pagos mensuales con estado `Pagado` o `Pendiente`.
- Panel de reportes con totales, deuda pendiente y pagos por contrato.
- Exportacion de reportes a archivo de texto.

## Tecnologias utilizadas

- Java
- Java Swing
- MySQL
- JDBC
- MySQL Connector/J
- jBCrypt
- JUnit

## Estructura del proyecto

```text
GestorAlquileres/
|-- db/
|   `-- proyecto_abm.sql
|-- lib/
|   |-- mysql-connector-j-9.7.0.jar
|   |-- jbcrypt-0.4.jar
|   `-- junit-platform-console-standalone-6.1.0.jar
|-- src/
|   |-- controller/
|   |-- model/
|   |-- repository/
|   |-- view/
|   `-- Main.java
`-- test/
    `-- repository/
```

## Requisitos previos

Antes de ejecutar el sistema, instalar:

- JDK 17 o superior.
- MySQL Server.
- MySQL Workbench, opcional pero recomendado.
- Visual Studio Code, IntelliJ IDEA, NetBeans o cualquier IDE compatible con Java.

## Configuracion de la base de datos

El proyecto usa una base de datos local llamada `proyecto_abm`.

Los datos de conexion se encuentran en:

```text
src/repository/Conexion.java
```

Configuracion actual:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/proyecto_abm";
private static final String USER = "root";
private static final String PASS = "root123";
```

Si tu usuario o contrasena de MySQL son distintos, modifica esos valores antes de ejecutar la aplicacion.

## Crear la base de datos paso a paso

### Opcion 1: desde MySQL Workbench

1. Abrir MySQL Workbench.
2. Conectarse al servidor local de MySQL.
3. Abrir el archivo:

```text
db/proyecto_abm.sql
```

4. Ejecutar todo el script.
5. Verificar que se haya creado la base `proyecto_abm` con sus tablas y datos de ejemplo.

### Opcion 2: desde terminal

Desde la carpeta raiz del proyecto:

```bash
mysql -u root -p < db/proyecto_abm.sql
```

Luego ingresar la contrasena de MySQL cuando se solicite.

## Usuario de prueba

El script SQL crea un usuario inicial para ingresar al sistema:

```text
Usuario: admin
Contrasena: admin123
Rol: ADMIN
```

La contrasena esta almacenada en la base de datos con BCrypt, por eso no aparece guardada como texto plano.

## Ejecucion del proyecto

### Desde un IDE

1. Abrir la carpeta del proyecto en el IDE.
2. Verificar que los `.jar` de la carpeta `lib/` esten agregados al classpath:
   - `mysql-connector-j-9.7.0.jar`
   - `jbcrypt-0.4.jar`
   - `junit-platform-console-standalone-6.1.0.jar`, solo necesario para pruebas.
3. Asegurarse de que MySQL este iniciado.
4. Ejecutar la clase:

```text
src/Main.java
```

5. Iniciar sesion con `admin` y `admin123`.

### Desde terminal en Windows PowerShell

Crear la carpeta de compilacion:

```powershell
New-Item -ItemType Directory -Force -Path bin
```

Compilar:

```powershell
javac -encoding UTF-8 -cp "lib/*" -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
```

Ejecutar:

```powershell
java -cp "bin;lib/*" Main
```

### Desde terminal en Linux o macOS

Crear la carpeta de compilacion:

```bash
mkdir -p bin
```

Compilar:

```bash
javac -encoding UTF-8 -cp "lib/*" -d bin $(find src -name "*.java")
```

Ejecutar:

```bash
java -cp "bin:lib/*" Main
```

## Uso del sistema

1. Iniciar la aplicacion.
2. Ingresar con el usuario de prueba.
3. Desde el menu principal, elegir el modulo que se desea administrar.
4. Para cargar un registro, completar el formulario y presionar el boton de guardado.
5. Para modificar o eliminar, seleccionar una fila de la tabla y usar el boton correspondiente.
6. Para buscar, escribir el criterio en el campo de busqueda del modulo.
7. En el panel de reportes, consultar los indicadores generales y la tabla de pagos por contrato.
8. Usar la opcion de exportacion para guardar un reporte en formato `.txt`.

## Modulos del sistema

### Propietarios

Administra las personas duenas de las propiedades. Registra nombre, apellido, DNI, telefono y correo electronico.

### Inquilinos

Administra las personas que alquilan propiedades. El DNI se utiliza como dato unico para evitar duplicados.

### Propiedades

Permite registrar inmuebles con direccion, tipo, precio mensual, disponibilidad y propietario asociado.

### Contratos

Vincula una propiedad con un inquilino durante un periodo determinado. Registra fecha de inicio, fecha de fin y monto mensual.

### Pagos

Registra pagos mensuales asociados a contratos. Cada pago tiene mes, monto, estado, fecha de pago e identificador de contrato.

### Reportes

Muestra indicadores generales del sistema, como pagos realizados, deuda pendiente, propiedades libres, propiedades ocupadas y detalle de pagos por contrato.

## Arquitectura

El proyecto esta organizado siguiendo una separacion por capas:

- `model`: clases que representan las entidades del sistema.
- `view`: pantallas, paneles y componentes visuales construidos con Swing.
- `controller`: logica de interaccion entre la vista y los datos.
- `repository`: acceso a la base de datos mediante JDBC y consultas SQL.

Esta organizacion permite separar la interfaz grafica, la logica de control y la persistencia de datos.

## Pruebas

El proyecto incluye una prueba para validar el inicio de sesion en:

```text
test/repository/UsuarioDAOTest.java
```

Para que la prueba funcione, la base de datos debe estar creada y debe existir el usuario `admin` cargado por el script SQL.

Compilar fuentes y pruebas en Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force -Path bin
javac -encoding UTF-8 -cp "lib/*" -d bin (Get-ChildItem -Recurse src,test -Filter *.java).FullName
```

Ejecutar pruebas:

```powershell
java -jar lib/junit-platform-console-standalone-6.1.0.jar --class-path "bin;lib/mysql-connector-j-9.7.0.jar;lib/jbcrypt-0.4.jar" --scan-class-path
```

## Problemas comunes

### Error de conexion a MySQL

Verificar que MySQL este iniciado y que los datos de `Conexion.java` coincidan con tu configuracion local.

### Base de datos inexistente

Ejecutar nuevamente:

```bash
mysql -u root -p < db/proyecto_abm.sql
```

### No reconoce el driver JDBC

Verificar que `mysql-connector-j-9.7.0.jar` este dentro de `lib/` y agregado al classpath.

### No permite iniciar sesion

Revisar que el script SQL se haya ejecutado completo y que exista el usuario `admin` en la tabla `usuarios`.

## Datos importantes

- Base de datos: `proyecto_abm`
- Script SQL: `db/proyecto_abm.sql`
- Clase principal: `src/Main.java`
- Clase de conexion: `src/repository/Conexion.java`
- Usuario de prueba: `admin`
- Contrasena de prueba: `admin123`
