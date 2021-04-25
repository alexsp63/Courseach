package program.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, осуществляющий связь между клиентом и сервером
 */
public class HttpClass {

    /**
     * Преобразование ответа сервера в строку
     * @param conn - соединение
     * @return - строку-ответ, если статус не 20*, то null
     */
    private static String getString(URLConnection conn){
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            return sb.toString();
        } catch (IOException e){
            return null;
        }
    }

    /**
     * Отдельное добавление статистики, которое сразу вернёт добавленный объект
     * @param url - ссылка для POST'а
     * @param jsonString - строка json'а, которую нужно добавить
     * @param token - токен текущего пользователя
     * @return добавленный объект статистики
     * @throws UnirestException - исключение
     */
    public static HttpResponse<String> PostStatisticsRequest(String url, String jsonString, String token) throws UnirestException {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json; charset=UTF-8");
        requestHeaders.put("Authorization", token);
        return Unirest.post(url).headers(requestHeaders).body(jsonString).asString();
    }

    /**
     * Получение неавторизованным пользователем занятых логинов
     * @param urlString - url для запроса
     * @return строку ответа
     */
    //это для неавторизованного пользователя, тут токена нет, сюда у меня разрешение всем на сервере
    public static String getRequest(String urlString){
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            return getString(conn);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Получение чего-либо для уже авторизованного пользователя
     * @param urlString - ссылка для получения данных
     * @param token - токен текущего пользователя
     * @return строку ответа
     */
    //такая перегрузка нужна, потому что эти геты уже для авторизованного пользователя по токену
    public static String getRequest(String urlString, String token){
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", token);
            httpURLConnection.connect();
            return getString(conn);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод для регистрации и авторизации
     * @param urlString - ссылка
     * @param jsonString - строка для добавления/авторизации
     * @return строку ответа
     */
    //post для регистрации и авторизации, тут нет никакого токена, опять же пермит у меня для всех на регистрацию
    public static String PostRequest(String urlString, String jsonString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("POST"); //устанавливаю тип метода
            httpURLConnection.setDoOutput(true); //собираюсь использовать это соединение для вывода
            byte[] out = jsonString.getBytes(StandardCharsets.UTF_8);
            int len = out.length;
            httpURLConnection.setFixedLengthStreamingMode(len);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.connect();
            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(out);
            }
            return getString(conn);
        } catch (ConnectException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод добавления для авторизованных пользователей
     * @param urlString - адрес для выполнения запроса
     * @param jsonString - строка добавления
     * @param token - токен
     * @return строку ответа
     */
    public static String PostRequest(String urlString, String jsonString, String token){
        try{
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            byte[] out = jsonString.getBytes(StandardCharsets.UTF_8);
            int len = out.length;
            httpURLConnection.setFixedLengthStreamingMode(len);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.setRequestProperty("Authorization", token);
            httpURLConnection.connect();
            try (OutputStream outputStream = httpURLConnection.getOutputStream()){
                outputStream.write(out);
            }
            return getString(conn);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод удаления записи
     * @param urlString - адрес для выполнения действия
     * @param jsonString - объект, на который нужно обновить, в виду строки
     * @param token - токен пользователя
     * @return строку ответаы
     */
    public static String PutRequest(String urlString, String jsonString, String token){
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setDoOutput(true);
            byte[] out = jsonString.getBytes(StandardCharsets.UTF_8);
            int len = out.length;
            httpURLConnection.setFixedLengthStreamingMode(len);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.setRequestProperty("Authorization", token);
            httpURLConnection.connect();
            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(out);
            }
            return getString(conn);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод для удаления записи из таблицы
     * @param urlString - адрес для выполнения запроса
     * @param token - токен текущего пользователя
     * @return строку ответа
     */
    public static boolean DeleteRequest(String urlString, String token){
        try{
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Authorization", token);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            httpURLConnection.getResponseCode();
            return true;
        } catch (IOException e){
            return false;
        }
    }
}
