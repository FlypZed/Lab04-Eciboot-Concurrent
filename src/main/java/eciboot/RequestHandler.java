package eciboot;

import java.io.*;
import java.net.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RequestHandler {
    private static RequestHandler instance;
    private static final int THREAD_POOL_SIZE = 10;
    private ExecutorService executorService;

    public static RequestHandler getInstance() {
        if (instance == null) {
            instance = new RequestHandler();
        }
        return instance;
    }

    public void run(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(8080);
        IoCFramework.loadControllers();
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando servidor...");
            try {
                serverSocket.close();
                executorService.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        System.out.println("Servidor iniciado en el puerto 8080...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            executorService.execute(() -> handleClient(clientSocket));
        }
    }


    private void handleClient(Socket clientSocket) {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] parts = requestLine.split(" ");
            String path = parts[1];

            out.println(IoCFramework.handleRequest(path));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
