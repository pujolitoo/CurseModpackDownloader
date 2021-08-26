package org.pujolitoo.CModpackDownloader.event;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

import org.pujolitoo.CModpackDownloader.CModpackDownloader;
import org.pujolitoo.CModpackDownloader.Utils;
import org.pujolitoo.CModpackDownloader.enums.ProjectInfo;
import org.apache.commons.io.FileUtils;
import org.pujolitoo.CModpackDownloader.CMDGUI;
import org.pujolitoo.CModpackDownloader.Project;
import org.pujolitoo.CModpackDownloader.Output;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.File;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.model.ZipParameters;

public class Download implements ActionListener{

    private CMDGUI frame;
    private String baseURL = "https://addons-ecs.forgesvc.net/api/v2";
    File outputPath;

    public Download(CMDGUI parentFrame){
        this.frame = parentFrame;

    }


    @Override
    public void actionPerformed(ActionEvent e){
        this.frame.changeButtonEnable(false);
        this.frame.setIdFieldEnabled(false);
        Toolkit.getDefaultToolkit().beep();
        Thread thread = new Thread(new Runnable(){
           @Override
           public void run(){
                downloadModpack();
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
        try{
            Integer.parseInt(frame.getProjectId());
            searchURL = "/addon/" + frame.getProjectId();
            url = baseURL + searchURL;
            type = ProjectInfo.BY_ID;
        }catch(NumberFormatException e){
            searchURL = "/addon/search?gameId=432&categoryId=0&searchFilter=${projectSlug}&pageSize=20&index=$index&sort=1&sortDescending=true&sectionId=4471";
            searchURL.replace("${projectSlug}", this.frame.getProjectId());
            url = baseURL + searchURL;
            type = ProjectInfo.BY_SEARCH;
        }
        System.out.println(url);
        frame.log("Getting modpack metadata...");
        project = Utils.getProjectMetadata(url, type);
        if(project == null){
            frame.log("Error: Empty Metadata.");
            JOptionPane.showMessageDialog(frame, "Modpack not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!check()){
            return;
        }

        Utils.download(project.getContentDownloadURL(), CModpackDownloader.tmpFolder.getAbsolutePath() + "/" + project.getFileName());

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
        downloadMods(modlist);
        frame.log("Mods downloaded.");

        frame.log("Copying overrides...");
        copyOverride();

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
        
        Utils.deleteFolder(CModpackDownloader.tmpFolder);
        CModpackDownloader.tmpFolder.mkdir();
        frame.log("Temp folder restored.");
        System.out.println("Temp folder deleted.");
        frame.log("Done!");

    }

    private boolean downloadMods(JsonObject manifest){
        String modURL = baseURL + "/addon/";
        JsonArray mods = manifest.get("files").getAsJsonArray();
        JsonObject mod = null;
        for(int i = 0; i < mods.size(); i++){
            try {
				mod = Utils.getJSON(modURL + mods.get(i).getAsJsonObject().get("projectID").getAsInt());
			} catch (IOException e) {
				e.printStackTrace();
			}
            for(int r = 0; r < mod.get("latestFiles").getAsJsonArray().size(); r++){
                if(mod.get("latestFiles").getAsJsonArray().get(r).getAsJsonObject().get("id").getAsInt() == mods.get(i).getAsJsonObject().get("fileID").getAsInt()){
                    String fileName = mod.get("latestFiles").getAsJsonArray().get(r).getAsJsonObject().get("fileName").getAsString();
                    frame.log("Downloading mod" + "(" + i + "/" + Integer.toString(mods.size()) + "): " + mod.get("name").getAsString());
                    Utils.download(mod.get("latestFiles").getAsJsonArray().get(r).getAsJsonObject().get("downloadUrl").getAsString(), new File(CModpackDownloader.tmpFolder, "/profile/mods/" + fileName).getAbsolutePath());
                }
            }
        }
        return true;
    }

    private void copyOverride(){
        try {
			FileUtils.copyDirectory(new File(CModpackDownloader.tmpFolder, "./extracted/overrides"), new File(CModpackDownloader.tmpFolder, "/profile"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
