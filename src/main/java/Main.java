import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()

        ) {
            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=mryFjiWJtcKAY3KMncxu8qzabLd0EuuTedSzJWVx");
            CloseableHttpResponse response = httpClient.execute(request);
            String body = new String(response.getEntity().getContent().readAllBytes());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            NASAAnswer nasaAnswer = gson.fromJson(body, NASAAnswer.class);
            System.out.println(nasaAnswer);
            String fileURL = nasaAnswer.getUrl();
            request = new HttpGet(fileURL);
            response = httpClient.execute(request);
            byte[] image = response.getEntity().getContent().readAllBytes();
            FileOutputStream fileOutputStream = new FileOutputStream(fileURL.substring(fileURL.lastIndexOf("/") + 1));
            fileOutputStream.write(image, 0, image.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
