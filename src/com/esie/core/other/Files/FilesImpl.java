package com.esie.core.other.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static com.esie.core.other.ANSI.ANSI.*;

public class FilesImpl implements FilesInterface {

    private List<File> files = new ArrayList<>();
    volatile private int counter = 0;
    volatile static int finishedThreads;
    private String name;

    public FilesImpl(String s) {
        this.name = s;
        finishedThreads = 0;
    }


    public int getCounter() {
        return counter;
    }


    public String getName() {
        return name;
    }


    public List<File> getFiles() {
        return files;
    }


    private void addFile(File file) {
        counter++;
        files.add(file);
        System.out.print("\r" + ANSI_GREEN + "Found " + counter + " file(s)" + ANSI_RESET);
    }


     @Override
     public void listFiles(String directoryName, String ext, String noExt){
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        assert fList != null;
        for (File file : fList) {
            if (file.isFile() && file.getName().endsWith(ext) && !file.getName().endsWith(noExt))
                addFile(file);
            else if (file.isDirectory())
                listFiles(file.getAbsolutePath(), ext, noExt);
        }
     }
}
