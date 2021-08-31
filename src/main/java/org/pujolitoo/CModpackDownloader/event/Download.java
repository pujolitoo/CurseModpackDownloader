package org.pujolitoo.CModpackDownloader.event;

import java.awt.event.*;
import java.io.IOException;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

import org.pujolitoo.CModpackDownloader.CModpackDownloader;
import org.pujolitoo.CModpackDownloader.Utils;
import org.pujolitoo.CModpackDownloader.enums.ProjectInfo;
import org.apache.commons.io.FileUtils;
import org.pujolitoo.CModpackDownloader.ui.CMDGUI;
import org.pujolitoo.CModpackDownloader.Project;
import org.pujolitoo.CModpackDownloader.ui.Output;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.File;
import java.net.URL;

import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.model.ZipParameters;

public class Download implements ActionListener{

    private CMDGUI frame;
    private String baseURL = "https://addons-ecs.forgesvc.net/api/v2";
    File outputPath;
    boolean overrides;

    public Download(CMDGUI parentFrame){
        this.frame = parentFrame;

    }


    @Override
    public void actionPerformed(ActionEvent e){
        this.frame.changeButtonEnable(false);
        this.frame.setIdFieldEnabled(false);
        Thread thread = new Thread(new Runnable(){
           @Override
           public void run(){
                downloadModpack();
                frame.setProgressBar1Indeterminate(false);
                frame.changeButtonEnable(true);
                frame.setIdFieldEnabled(true);  
           } 
        });
        thread.start();
        
    }

    private boolean check(){
        if(!frame.getProjectId().equals("")){
            outputPath = getOutputPath();
            if(outputPath == null || outputPath.getAbsolutePath() == ""){
                return false;
            }else{
                return true;  
            }
        }else{
            projectIdEmpty();
            return false;
        }
    }

    private void projectIdEmpty(){
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(frame, "ProjectId is requiered to continue.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private File getOutputPath(){
        Output dialog = new Output(this.frame);
        dialog.setVisible(true);
        this.overrides = dialog.getOverride();
        return dialog.getPath();
    }



    public void downloadModpack(){
        String url;
        String searchURL;
        ProjectInfo type;
        Project project;
        File extracted = new File(CModpackDownloader.tmpFolder, "/extracted");
        File profile = new File(CModpackDownloader.tmpFolder, "/profile");
        File mods = new File(CModpackDownloader.tmpFolder, "/profile/mods");
        profile.mkdir();
        mods.mkdir();
        String id = frame.getProjectId().replaceAll("\\s+", "");
        try{
            Integer.parseInt(id);
            searchURL = "/addon/" + id;
            url = baseURL + searchURL;
            type = ProjectInfo.BY_ID;
        }catch(NumberFormatException e){
            searchURL = "/addon/search?gameId=432&categoryId=0&searchFilter=#projectSlug#&pageSize=20&index=$index&sort=1&sortDescending=true&sectionId=4471";
            searchURL = searchURL.replaceAll("#projectSlug#", id);
            try {
                JsonArray projectjson = (JsonArray) Utils.getJSON(baseURL + searchURL);
                searchURL = "/addon/" + projectjson.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            url = baseURL + searchURL;
            type = ProjectInfo.BY_SEARCH;
        }
        System.out.println(url);
        frame.log("Getting modpack metadata...");
        project = Utils.getProjectMetadata(url, type);
        if(project == null){
            Toolkit.getDefaultToolkit().beep();
            frame.log("Error: Empty Metadata.");
            JOptionPane.showMessageDialog(frame, "Modpack not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!check()){
            return;
        }

        frame.setProgressBar1Indeterminate(true);
        try {
            Utils.download(project.getContentDownloadURL(), CModpackDownloader.tmpFolder.getAbsolutePath() + "/" + project.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.log("Extracting modpack contents...");
        try {
			ZipFile zip = new ZipFile(new File(CModpackDownloader.tmpFolder.getAbsolutePath() + "/" + project.getFileName()));
            zip.extractAll(extracted.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}

        frame.log("Reading manifest file...");
        JsonObject modlist = Utils.loadJsonFormFile(new File(extracted, "/manifest.json"));
        System.out.println(modlist);

        frame.log("Downloading mods...");
        if(!downloadMods(modlist)){
            frame.setIdField("");
            Utils.deleteTemp();
            return;
        }
        frame.log("Mods downloaded.");

        frame.log("Copying overrides...");
        copyOverride();

        File zipFinal;
        if(overrides){
            outputPath.delete();
            zipFinal = new File(outputPath.getAbsolutePath());
        }else{
            if(outputPath.exists()){
                int samename = 0;
                File directory = new File(outputPath.getAbsolutePath()
                        .replaceAll(outputPath.getName(), ""));
                String fileNamenoextension = outputPath.getName().replaceAll(".zip", "");
                for(File f : directory.listFiles()){
                    if(f.getName().contains(outputPath.getName())){
                        samename++;
                    }
                }
                for(int i = 0; i < samename; i++){
                    zipFinal = new File(outputPath.getAbsolutePath().replace(fileNamenoextension, fileNamenoextension + "(" + i + ")"));
                    if(!zipFinal.exists()){
                        break;
                    }
                }
            }else{
                zipFinal = new File(outputPath.getAbsolutePath());
            }
        }

        System.out.println("Packing zip...");
        frame.log("Packing in zip...");
        try {
			ZipFile finalZip = new ZipFile(outputPath);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            finalZip.addFolder(profile, parameters);
            System.out.println("Packed successfully in: " + outputPath.getAbsolutePath());
            frame.log("Packed succesfully in:" + outputPath.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}

        frame.setIdField("");

        Utils.deleteTemp();
        frame.log("Temp folder restored.");
        System.out.println("Temp folder deleted.");
        frame.log("Done!");

    }

    private boolean downloadMods(JsonObject manifest){
        String modURL = baseURL + "/addon/";
        JsonArray mods = manifest.get("files").getAsJsonArray();
        JsonObject modInfo = null;
        JsonObject modFile = null;
        for(int i = 0; i < mods.size(); i++){
            modInfo = getModInfo(manifest, i);
            modFile = getModFile(manifest, i);
            frame.log("Downloading mod " + "(" + i + "/" + mods.size() + "): " + modInfo.get("name").getAsString());
            try {
                Utils.download(modFile.get("downloadUrl").getAsString(), new File(CModpackDownloader.tmpFolder, "/profile/mods/" + modFile.get("fileName").getAsString()).getAbsolutePath());
            } catch (IOException e) {
                int r = 0;
                while(r < 5){
                    try{
                        Utils.download(modFile.get("downloadUrl").getAsString(), new File(CModpackDownloader.tmpFolder, "/profile/mods/" + modFile.get("fileName").getAsString()).getAbsolutePath());
                        return true;
                    }catch(IOException exc){
                        r++;
                        frame.log("There was an unexpected error while downloading the mod. Retrying..." + "(" + r + "/" + 5 + ")");
                        if(r==5){
                            exc.printStackTrace();
                            frame.log("ERROR: The mod couldn't be downloaded.");
                            return false;
                        }
                    }
                }
                e.printStackTrace();
            }
        }
        return true;
    }

    private JsonObject getModFile(JsonObject manifest, int index){
        String modURL = baseURL + "/addon/";
        JsonArray mods = manifest.get("files").getAsJsonArray();
        JsonObject result = null;
        try {
            result = Utils.getJSON(modURL + mods.get(index).getAsJsonObject().get("projectID").getAsInt() + "/file/" + mods.get(index).getAsJsonObject().get("fileID").getAsString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JsonObject getModInfo(JsonObject manifest, int index){
        String modURL = baseURL + "/addon/";
        JsonArray mods = manifest.get("files").getAsJsonArray();
        JsonObject result = null;
        try {
            result = Utils.getJSON(modURL + mods.get(index).getAsJsonObject().get("projectID").getAsInt()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void copyOverride(){
        try {
			FileUtils.copyDirectory(new File(CModpackDownloader.tmpFolder, "./extracted/overrides"), new File(CModpackDownloader.tmpFolder, "/profile"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
