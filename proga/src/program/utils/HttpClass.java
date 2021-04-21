package program.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClass {

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

    public static HttpResponse<String> PostStatisticsRequest(String url, String jsonString, String token) throws UnirestException {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json; charset=UTF-8");
        requestHeaders.put("Authorization", token);
        return Unirest.post(url).headers(requestHeaders).body(jsonString).asString();
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

    //post для регистрации и авторизации, тут нет никакого токена, опять же пермит у меня для всех на регистрацию
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
