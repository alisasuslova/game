import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {

        List<String> dirName = new LinkedList<>();
        dirName.add("D://Games/src");
        dirName.add("D://Games/res");
        dirName.add("D://Games/savegames");
        dirName.add("D://Games/temp");
        dirName.add("D://Games/src/main");
        dirName.add("D://Games/src/test");
        dirName.add("D://Games/res/drawables");
        dirName.add("D://Games/res/vectors");
        dirName.add("D://Games/res/icons");

        writeToFile(makeDir(dirName));

        List<String> fileName = new LinkedList<>();
        fileName.add("D://Games/src/main/Main.java");
        fileName.add("D://Games/src/main/Utils.java");
        fileName.add("D://Games/temp/temp.txt");

        writeToFile(makeFile(fileName));

        GameProgress gameProgress1 = new GameProgress(70, 2, 3, 3.6);
        String path1 = "D://Games/savegames/gameProgress1.dat";

        GameProgress gameProgress2 = new GameProgress(85, 4, 5, 5.8);
        String path2 = "D://Games/savegames/gameProgress2.dat";

        GameProgress gameProgress3 = new GameProgress(90, 5, 9, 7.6);
        String path3 = "D://Games/savegames/gameProgress3.dat";

        List<String> pathes = new LinkedList<>();
        pathes.add(path1);
        pathes.add(path2);
        pathes.add(path3);

        List<GameProgress> gameProgress = new LinkedList<>();
        gameProgress.add(gameProgress1);
        gameProgress.add(gameProgress2);
        gameProgress.add(gameProgress3);

        saveGame(pathes, gameProgress);
        String zipPath = "D://Games/savegames/gameSaves.zip";
        zipFiles(zipPath, pathes);
        deleteFiles(pathes);

        String openZipPath = "D://Games/savegames/";
        openZip(zipPath, openZipPath);

        List<String> packed_pathes = new LinkedList<>();
        packed_pathes.add("D://Games/savegames/packed_gameProgress1.dat");
        packed_pathes.add("D://Games/savegames/packed_gameProgress2.dat");
        packed_pathes.add("D://Games/savegames/packed_gameProgress3.dat");

        print(openProgress(packed_pathes));

    }

    public static String makeDir(List<String> dirName) {
        LocalDateTime localDateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < dirName.size(); i++) {
            File dir = new File(dirName.get(i));
            if (dir.mkdir())
                sb.append(localDateTime)
                        .append(" ")
                        .append("Каталог " + dirName.get(i) + " создан")
                        .append("\n");
        }
        String result = sb.toString();
        return result;
    }

    public static void writeToFile(String result) {

        try (FileOutputStream fos = new FileOutputStream("D://Games/temp/temp.txt", true)) {
            byte[] bytes = result.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String makeFile(List<String> fileName) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < fileName.size(); i++) {
            File file = new File(fileName.get(i));
            if (file.createNewFile())
                sb.append(localDateTime)
                        .append(" ")
                        .append("Файл " + fileName.get(i) + " был создан")
                        .append("\n");

            if (fileName.get(i).equals("D://Games/temp/temp.txt"))
                sb.append(localDateTime)
                        .append(" ")
                        .append("Файл " + fileName.get(i) + " был создан")
                        .append("\n");
        }
        String result = sb.toString();
        return result;
    }

    public static void saveGame(List<String> pathes, List<GameProgress> gameProgress) {

        for (int i = 0; i < pathes.size(); i++) {
            for (int j = 0; j < gameProgress.size(); j++) {
                File file = new File(pathes.get(i));
                try {
                    if (file.createNewFile())
                        System.out.println("Сохранение прогресса произведено. Файл " + pathes.get(i) + " записан.");
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                if (i == j) {
                    try (FileOutputStream fos = new FileOutputStream(pathes.get(i));
                         ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                        oos.writeObject(gameProgress.get(j));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public static void zipFiles(String zipPath, List<String> pathes) throws FileNotFoundException {

        ZipOutputStream zout = new ZipOutputStream(new
                FileOutputStream(zipPath));
        for (int i = 0; i < pathes.size(); i++) {
            try (
                    FileInputStream fis = new FileInputStream(pathes.get(i))) {
                ZipEntry entry = new ZipEntry("packed_gameProgress" + (i + 1) + ".dat");
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Создан архив " + zipPath);
    }

    public static void deleteFiles(List<String> pathes) {

        for (int i = 0; i < pathes.size(); i++) {
            File file = new File(pathes.get(i));
            if (file.delete())
                System.out.println("Файл " + pathes.get(i) + " удален");
        }
    }

    public static void openZip(String zipPath, String openZipPath) {
        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(openZipPath + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
            System.out.println("Файлы сохранений из архива " + zipPath + " разархивированы в каталоге D://Games/savegames/");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<GameProgress> openProgress(List<String> packed_pathes) {
        List<GameProgress> www = new LinkedList<>();
        GameProgress rezult = null;

        for (int i = 0; i < packed_pathes.size(); i++) {

            try (FileInputStream fis = new FileInputStream(packed_pathes.get(i));
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                rezult = (GameProgress) ois.readObject();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            www.add(rezult);
        }
        return www;
    }

    public static void print(List<GameProgress> www) {

        Object[] objects = www.toArray();
        for (Object obj : objects) {
            System.out.print(obj.toString());
        }
    }


}
