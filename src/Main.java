import com.sun.net.httpserver.HttpServer;
import handlers.TodoHandler;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Создаем обработчик задач
        TodoHandler todoHandler = new TodoHandler();

        // Настраиваем маршруты
        server.createContext("/", todoHandler);      // Главная страница
        server.createContext("/todos", todoHandler);  // Все операции с задачами

        server.setExecutor(null);
        server.start();

        System.out.println("🚀 Todo-менеджер запущен!");
        System.out.println("📱 http://localhost:8080/");
        System.out.println("💾 Данные сохраняются в todos.txt");
        System.out.println("Для остановки нажмите Ctrl+C");
    }
}