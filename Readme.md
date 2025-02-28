# 🚀 Proyecto ECIBoot - Servidor Concurrente en Java con Docker

Servidor web en Java utilizando un framework propio (sin Spring). Mejorado para soportar concurrencia y un apagado elegante, y preparado para despliegue en la nube con Docker y AWS EC2.

---

## 📌 Características Principales

✅ Manejo concurrente de solicitudes con **pool de hilos**
✅ Apagado controlado para liberar recursos correctamente
✅ Ejecución en contenedor Docker para facilitar despliegue
✅ Preparado para despliegue en AWS EC2

---

## 🛠️ Requisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado lo siguiente:

- **Java 11 o superior** - Para ejecutar el servidor
- **Maven** - Para la compilación
- **Docker** - Para contenedorización y despliegue

---

## 🚀 Instalación y Ejecución

1️⃣ Clonar el repositorio:

```sh
git clone https://github.com/TuUsuario/ECIBoot.git
```

2️⃣ Acceder a la carpeta del proyecto:

```sh
cd ECIBoot
```

3️⃣ Compilar y ejecutar la aplicación:

```sh
mvn clean package exec:java -Dexec.mainClass="server.Main"
```

4️⃣ Acceder desde el navegador:

🔗 [http://localhost:8080](http://localhost:8080)

---

## 📂 Estructura del Proyecto

```
ECIBoot/
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── server/
│   │   │   │   ├── ConcurrentServer.java  # Implementación del servidor concurrente
│   │   │   │   ├── ClientHandler.java  # Manejo de conexiones de clientes
│   │   │   │   ├── ShutdownHandler.java  # Apagado controlado del servidor
│   │   ├── resources/
│   │   │   ├── static/  # Archivos HTML, CSS, JS
```

---

## 🏗️ Implementación de Concurrencia

Se han agregado mejoras para el manejo de múltiples solicitudes simultáneas:

✅ Uso de **hilos** para gestionar cada conexión
✅ Implementación de **pool de hilos** para optimizar recursos
✅ Sincronización adecuada para evitar condiciones de carrera

📌 **Ejemplo de código:**

```java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentServer {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Atendiendo cliente: " + clientSocket.getInetAddress());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 📦 Uso de Docker

El proyecto está configurado para ejecutarse dentro de un contenedor Docker, facilitando su despliegue y portabilidad.

### 🚀 Construcción de la Imagen

```sh
docker build -t eciboot-server .
```

### 📡 Ejecución del Contenedor

```sh
docker run -p 8080:8080 eciboot-server
```

🔗 Accede a la aplicación en [http://localhost:8080](http://localhost:8080)

---

## 🛠️ Dockerfile

El `Dockerfile` contiene los siguientes pasos:

1️⃣ Usa una imagen base con Java preinstalado.
2️⃣ Copia los archivos del proyecto al contenedor.
3️⃣ Compila y empaqueta el proyecto usando Maven.
4️⃣ Define el comando de inicio para ejecutar el servidor.

---

## 🔄 Despliegue en AWS EC2

Para desplegar en una instancia EC2:

1️⃣ **Sube la imagen a Docker Hub** o transfiere el código a EC2.
2️⃣ En la instancia EC2, instala Docker:
   ```sh
   sudo yum update -y
   sudo yum install docker -y
   sudo systemctl start docker
   ```
3️⃣ Descarga y ejecuta la imagen:
   ```sh
   docker run -p 8080:8080 eciboot-server
   ```
4️⃣ Accede desde el navegador o con `curl`:
   ```sh
   curl http://<EC2_PUBLIC_IP>:8080
   ```

![image](https://github.com/user-attachments/assets/45b77604-8e74-4708-8de3-73da96b31350)


---

## 📌 Autor

👨‍💻 **Tu Nombre**  
🔗 GitHub: [Flyp](https://github.com/FlypZed)
📧 Contacto: Flyp.and@gmail.com

---

