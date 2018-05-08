package com.esie.backup;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.other.ANSI.ANSI;
import com.esie.core.other.Files.FilesInterface;
import com.esie.core.other.Files.FilesImpl;
import com.esie.core.other.Encryption.EncryptionUtil;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.esie.core.other.Encryption.EncryptionUtil.encryptToBytes;


public class Main {

    static private ConfigurationSingleton configuration = ConfigurationSingleton.getInstance("src/resources/configuration.txt");
    static private List<FilesInterface> bcDirectoriesLoaded = new ArrayList<>();
    static private List<String> sourceDirectories   = new ArrayList<>();
    static private List<String> targetDirectories   = new ArrayList<>();
    static private String THIS_DATE;
    static private File dateDir;


    private static void initKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        MessageDigest SHA1 = MessageDigest.getInstance("SHA-1");
        byte[] KEY = (scanner.next()).getBytes("UTF-8");
        KEY = SHA1.digest(KEY);
        KEY = Arrays.copyOf(KEY, 16); //128bit
        EncryptionUtil.initKey(KEY);
    }


    private static void loadConfigFile(){

       // getInstance("backup.configurationSingleton");
       // System.out.println("ConfigurationSingleton loaded." + "\n" +
       //         "Checking source directories...");
       // for (int i = 1; i < 1000; i++) {
       //     try {
       //         if (configuration.getValue("B" + i) != null)
       //             sourceDirectories.add(configuration.getValue("B" + i));
       //     } catch (Exception e) {
       //         break;
       //     }
       // }
       // System.out.println(ANSI.ANSI_GREEN + "Found " + sourceDirectories.size() + " source directories." + ANSI.ANSI_RESET + "\n" +
       //         "Checking files: ");
    }


    private static void CreateManifest() throws Exception {

        for (String s : sourceDirectories) {

            // Checking all files.
            FilesImpl filesImplClass = new FilesImpl(s);
            System.out.println("Checking " + ANSI.ANSI_PURPLE + s + ANSI.ANSI_RESET + ".");
            double startTime = System.nanoTime();
            filesImplClass.listFiles(s, "",".nobackup");
            double endTime = System.nanoTime();
            System.out.print(" in " + (((endTime - startTime)/1000000000)/60) + " minutes.");
            bcDirectoriesLoaded.add(filesImplClass);
            System.out.println();

            // Indexing all files in source dirs.
            if (Boolean.parseBoolean(configuration.getValue("INDEXING"))) {
                double percent;
                int counter = 0;
                int manifestFile = 0;
                String fileListIndex = "";

                for (File file : filesImplClass.getFiles()) {
                    fileListIndex = fileListIndex + "\n" +
                                    file.getAbsolutePath() + "," + file.length();
                    counter++;
                    percent = counter * 100 / filesImplClass.getCounter();
                    System.out.print("\r" + ANSI.ANSI_CYAN + "Processing backup manifest " + percent + "%. " +
                                            ANSI.ANSI_RESET + "File " + (manifestFile+1) + ". " + ANSI.ANSI_GREEN + "Total files: " + counter + "." + ANSI.ANSI_RESET);
                    if (fileListIndex.getBytes().length > 1000000){
                        try (FileOutputStream fos = new FileOutputStream(s + "backup_manifest_" + manifestFile + ".nobackup")) {
                                              fos.write(encryptToBytes(fileListIndex.getBytes()));
                                              fos.close();
                            manifestFile++;
                            fileListIndex = "";
                        }
                    }
                }
                System.out.println();
                manifestFile++;
                try (FileOutputStream fos = new FileOutputStream(s + "backup_manifest_" + manifestFile + ".nobackup")) {
                                      fos.write(encryptToBytes(fileListIndex.getBytes()));
                                      fos.close();
                    }
            } else System.out.println("\n" + "backup manifest disabled. All files will be copied.");
        }
        if (Boolean.parseBoolean(configuration.getValue("INDEXING")))
            System.out.println("\n" + "backup manifest saved.");
    }


    private static void checkDirs(){
        System.out.println("Checking target directories...");
        for (int i = 1; i < 1000; i++){
            try {
                if (configuration.getValue("T" + i) != null)
                    targetDirectories.add(configuration.getValue("B" + i));
            } catch (Exception e) {
                break;
            }
        }
    }


    private static void createDateDir(){
        THIS_DATE = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(new Date()) + "." + System.getProperty("user.name");
        System.out.println(ANSI.ANSI_GREEN + "Found " + targetDirectories.size() + " target directories." + ANSI.ANSI_RESET + "\n" +
                ANSI.ANSI_CYAN + "INITIALISING BACKUP..." + ANSI.ANSI_GREEN + "\n" +
                "DATE " + THIS_DATE + ANSI.ANSI_RESET);

        dateDir = new File(configuration.getValue("T1") + THIS_DATE + "/");
        BackUpThread.dateDir = dateDir;
        if (!dateDir.exists()) {
            if (dateDir.mkdir())
                System.out.println("Directory created.");
            else
                System.out.println(ANSI.ANSI_RED + "ERROR" + ANSI.ANSI_RESET);
        }
    }


    private static void copyManifest() {
        int counter = -1;
        System.out.println(ANSI.ANSI_CYAN + "COPYING BACKUP MANIFESTS." + ANSI.ANSI_RESET);
        for (String s : sourceDirectories) {
            //System.out.println(s);
            while (true) {
                try {
                    counter++;
                    Files.copy(new File(s + "backup_manifest_" + counter + ".nobackup").toPath(),
                               new File(configuration.getValue("T1") + THIS_DATE + "/backup_manifest_" + counter + ".nobackup").toPath(),
                               StandardCopyOption.REPLACE_EXISTING);
                    new File(s + "backup_manifest_" + counter + ".nobackup").delete();
                } catch (Exception e) {
                    break;
                }
            }
        }
    }


	public static void main(String[] args) throws Exception {

        double startTime = System.nanoTime();
        System.out.println("============================START============================" + "\n" +
                           "Enter EncryptionUtil password");
        initKey();
        loadConfigFile();
        try {
            CreateManifest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkDirs();
        createDateDir();

        for (FilesInterface finder : bcDirectoriesLoaded)
           // BackUp(finder);

        if (Boolean.parseBoolean(configuration.getValue("INDEXING")))
            copyManifest();

        double endTime = System.nanoTime();
        System.out.println(ANSI.ANSI_CYAN + "BACKUP COMPLETED IN " + ANSI.ANSI_GREEN + ((endTime - startTime)/1000000000/60) + ANSI.ANSI_CYAN + " MINUTES.");
    }
}

