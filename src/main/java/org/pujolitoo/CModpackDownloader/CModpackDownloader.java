package org.pujolitoo.CModpackDownloader;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CModpackDownloader{

    public static File tmpFolder = new File(System.getProperty("java.io.tmpdir") + "/CModpackDownloader");

    public static void main(String[] args){
        System.out.println(tmpFolder.getAbsolutePath());
        setupWorkspaceFolder();
        start();
    }

    public static void start(){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                CMDGUI main = new CMDGUI();
                main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                main.setVisible(true);
            }
        });
    }

    public static void setupWorkspaceFolder(){
        if(tmpFolder.isDirectory()){
        }else{
            tmpFolder.mkdir();
        }
    }
}