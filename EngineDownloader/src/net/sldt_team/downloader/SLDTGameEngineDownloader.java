package net.sldt_team.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SLDTGameEngineDownloader {

    private static boolean isRedirected(Map<String, List<String>> header) {
        for (String hv : header.get(null)) {
            if (hv.contains(" 301 ") || hv.contains(" 302 ")) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getGameEngineVersionList() throws IOException {
        List<String> tags = new ArrayList<String>();

        String sURL = "https://api.github.com/repos/StoneLineDevTeam/SLDTGameEngine/tags";
        URL url = new URL(sURL);
        HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonArray rootArray = root.getAsJsonArray();
        for (JsonElement element : rootArray){
            JsonObject obj = element.getAsJsonObject();
            String tag = obj.get("name").getAsString();

            tags.add(tag);
        }

        return tags;
    }

    public static void downloadGameEngineFromGitHub(String version, File dlDir) throws Throwable {
        String link = "https://github.com/StoneLineDevTeam/SLDTGameEngine/releases/download/" + version + "/SLDTGameEngine.jar";
        URL url = new URL(link);
        HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
        Map<String, List<String>> header = http.getHeaderFields();
        while (isRedirected(header)) {
            link = header.get("Location").get(0);
            url = new URL(link);
            http = (HttpsURLConnection) url.openConnection();
            header = http.getHeaderFields();
        }
        InputStream input = http.getInputStream();
        byte[] buffer = new byte[4096];
        int n;
        OutputStream output = new FileOutputStream(new File(dlDir + File.separator + "SLDTGameEngine_" + version + ".jar"));
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        output.close();
    }
}
