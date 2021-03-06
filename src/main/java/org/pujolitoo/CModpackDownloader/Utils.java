package org.pujolitoo.CModpackDownloader;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.pujolitoo.CModpackDownloader.enums.ProjectInfo;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonArray;

import javax.swing.*;


public class Utils {
    public static FileOutputStream stream;
    
    public static JsonElement getJSON(String url) throws MalformedURLException, IOException{

        InputStream is = new URL(url).openStream();
        try {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JsonElement json = JsonParser.parseString(jsonText);
        return json;
        } finally {
            is.close();
        }
    }


    public static boolean download(String url, String path)throws IOException {
        if(url.contains(" "))
            url = url.replace(" ", "%20");
        InputStream in = null;
        in = new URL(url).openStream();
        Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

        return true;
    }

    public static Project getProjectMetadata(String url, ProjectInfo type){
        JsonObject projectJson = null;
        Project project = null;
        try {
            projectJson = getJSON(url).getAsJsonObject();
            project = createProject(projectJson);
        } catch (IOException e) {
            e.printStackTrace();
        }catch(IllegalStateException e){
            return null;
        }
        return project;
    }

    private static Project createProject(JsonObject obj){
        Project project = new Project();
        project.setProjectId(obj.get("id").getAsInt());
        project.setProjectName(obj.get("name").getAsString());
        project.setProjectSlug(obj.get("slug").getAsString());
        project.setContentDownloadURL(obj.get("latestFiles").getAsJsonArray().get(0).getAsJsonObject().get("downloadUrl").getAsString());
        project.setFileName(obj.get("latestFiles").getAsJsonArray().get(0).getAsJsonObject().get("fileName").getAsString());
        project.setLatestFileID(obj.get("latestFiles").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsInt());
        return project;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }


    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static JsonObject loadJsonFormFile(File file){
        JsonObject json = null;
        try {
            FileReader reader = new FileReader(file);
			json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return json;
    }

    public static void deleteTemp(){
        Utils.deleteFolder(CModpackDownloader.tmpFolder);
        CModpackDownloader.tmpFolder.mkdir();
    }

    public static void centerWindow(Window window, Component parentWindow){
        window.setLocationRelativeTo(parentWindow);
    }

    
}
