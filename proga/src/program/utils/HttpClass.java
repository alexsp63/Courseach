package program.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpClass {

    private static String getString(URLConnection conn){
        try {
            //если страница возвращается, значит, не произошёл переход в корень /, где у меня ничего нет,
            //а именно туда и попадают после успешной авторизации
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

    //post для регистрации, тут нет никакого токена, опять же пермит у меня для всех на регистрацию
    public static String PostRequest(String urlString, String jsonString){
        try{
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
            try (OutputStream outputStream = httpURLConnection.getOutputStream()){
                outputStream.write(out);
            }
            return getString(conn);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String PutRequest(String urlString, String jsonString){
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

    public static boolean DeleteRequest(String urlString){
        try{
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            httpURLConnection.getResponseCode();
            return true;
        } catch (IOException e){
            return false;
        }
    }
}
