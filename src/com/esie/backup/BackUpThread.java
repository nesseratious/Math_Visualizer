package com.esie.backup;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.other.ANSI.ANSI;
import com.esie.core.other.Files.FilesImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import static com.esie.core.other.Encryption.EncryptionUtil.encryptToBytes;


class BackUpThread {

    static ConfigurationSingleton configuration = ConfigurationSingleton.getInstance("src/resources/configuration.txt");
    volatile public static File dateDir;
    private volatile static int finishedThreads;
    volatile private static int counter;
    private static final int threads = Integer.parseInt(configuration.getValue("THREADS"));
    private static final int MAX_FILE_SIZE = 700000000;
    private static HashMap <Integer, List<File>> threadData = new HashMap<>();
    private static String EXTENSION = "";


    private static void InitExt(){
        if (Boolean.parseBoolean(configuration.getValue("ENCRYPTION")))
            EXTENSION = ".EES";
        if (Boolean.parseBoolean(configuration.getValue("COMPRESSION")))
            EXTENSION = ".ZIP";
        if (Boolean.parseBoolean(configuration.getValue("ENCRYPTION")) &&
                Boolean.parseBoolean(configuration.getValue("COMPRESSION")))
            EXTENSION = ".ZEES";
    }


    static void BackUp (FilesImpl filesImpl) {
        InitExt();
        finishedThreads = 0;
        counter = 0;
        System.out.println(ANSI.ANSI_CYAN + "Initialising " + threads + " thread(s)." + ANSI.ANSI_RESET);
        for (int i = 1; i <= threads; i++){
            List<File> localList = new ArrayList<>();
            int tempFileCount = 0,
                threadFiles = filesImpl.getCounter() / threads;

            for (int j = 0; j < threadFiles;  j++){
                localList.add(filesImpl.getFiles().get(j+(threadFiles*(i-1))));
                tempFileCount++;
            }
            System.out.println("" + "Listed " + tempFileCount + " file(s) in thread " + (i-1) + ".");
            threadData.put(i, localList);
        }
        System.out.println(ANSI.ANSI_CYAN + "STARTING BACKUP." +ANSI.ANSI_RESET);

        for (int thread : threadData.keySet()){
            new Thread(() -> {
               for (File file : threadData.get(thread)){
                   counter++;
                   double percent = counter * 100 / filesImpl.getCounter();
                   System.out.print("\r" + "Processing " + percent + "% " + ANSI.ANSI_PURPLE + filesImpl.getName() + ANSI.ANSI_RESET + " root:" + file.getAbsolutePath());
                   try {
                       backupFile(file);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               finishedThreads++;
            }).start();
        }
        while(true){
            if(finishedThreads == threads){
                System.out.print("\r" + "Processing 100% " + ANSI.ANSI_PURPLE + filesImpl.getName() + ANSI.ANSI_RESET);
                break;
            }
        }
        System.out.println();
    }


    private static void backupFile(File file) throws Exception {

        String encryptedName;
        // if (Boolean.parseBoolean(configuration.getValue("ENCRYPTION")))
        //     encryptedName = encrypt(file.getName()).replaceAll("/","&8A8G03HNAC0GI03N&");
        // else
            encryptedName = file.getName();

        if (file.length() < MAX_FILE_SIZE) {
            FileInputStream s = new FileInputStream(file.getAbsolutePath());
            byte[] data = new byte[s.available()];
            s.read(data);
            s.close();
            byte[] arrayEnc = new byte[0];

            if (Boolean.parseBoolean(configuration.getValue("ENCRYPTION"))) {
                try {
                    arrayEnc = encryptToBytes(data);
                } catch (Exception e) {
                    System.out.println(ANSI.ANSI_RED + "ERROR ENCRYPTION" + ANSI.ANSI_RESET);
                    e.printStackTrace();
                }
            } else
                arrayEnc = data;
                data = null;

            File tempCheckFile = new File(dateDir + "/" + file.getAbsolutePath().replaceAll(file.getName(), ""));
            if (!tempCheckFile.exists())
                tempCheckFile.mkdirs();

            if (!Boolean.parseBoolean(configuration.getValue("COMPRESSION"))){
                try (FileOutputStream fos = new FileOutputStream(tempCheckFile + "/" + encryptedName + EXTENSION)) {
                    fos.write(arrayEnc);
                    fos.close();
                }
            } else {
                ZipOutputStream out = new ZipOutputStream(
                        new FileOutputStream(new File(tempCheckFile + "/" + file.getName() + EXTENSION)));

                ZipEntry e = new ZipEntry(tempCheckFile + "/" + file.getName());
                out.putNextEntry(e);
                out.write(arrayEnc, 0, arrayEnc.length);
                out.closeEntry();
                out.close();
            }

        } else {
            Files.copy(file.toPath(),
                    new File(dateDir + "/" + file.getAbsolutePath().replaceAll(file.getName(), "") + "/" + file.getName() + EXTENSION).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
