# CRUD_MVC - CRUD basico de estudiantes con MVC (consola)

CRUD de estudiantes sin patrones adicionales, solo con Modelo, Vista y Controlador.
El ID se genera automaticamente al crear y no se puede modificar.

| Carpeta | Responsabilidad | Clases |
| --- | --- | --- |
| `modelo` | Datos y operaciones en memoria. El ID es `final`. | `Estudiante`, `EstudianteModelo` |
| `vista` | Mostrar el menu y leer datos por consola. | `EstudianteVista` |
| `controlador` | Recibir la opcion, validar y llamar al modelo. | `EstudianteControlador` |
| raiz | Arranca el programa. | `Main` |

## Como ejecutar (PowerShell)

```powershell
cd "C:\Users\adria\Documents\GitHub\RicardoLainez36838_G5_ADSW\PROYECTO\2. DISEÑOS"
javac (Get-ChildItem "CRUD_MVC" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
java CRUD_MVC.Main
```
