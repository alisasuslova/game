import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        LocalDateTime localDateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();


        File dir = new File("res");
        if (dir.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("Каталог res создан")
                    .append("\n");


        File dir1 = new File("savegames");
        if (dir1.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("Каталог savegames создан")
                    .append("\n");


        File dir2 = new File("temp");
        if (dir2.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("Каталог temp создан")
                    .append("\n");


        File dir3 = new File("src/main");
        if (dir3.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("В каталоге src создан новый каталог main")
                    .append("\n");


        File dir4 = new File("src/test");
        if (dir4.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("В каталоге src создан новый каталог test")
                    .append("\n");


        File file1 = new File("src/main/Main.java");
        try {
            if (file1.createNewFile())
                sb.append(localDateTime)
                        .append(" ")
                        .append("Файл Main.java был создан в каталоге main")
                        .append("\n");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        File file2 = new File("src/main/Utils.java");
        try {
            if (file2.createNewFile())
                sb.append(localDateTime)
                        .append(" ")
                        .append("Файл Utils.java был создан в каталоге main")
                        .append("\n");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        File dir5 = new File("res/drawables");
        if (dir5.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("В каталоге res создана новая директория drawables")
                    .append("\n");


        File dir6 = new File("res/vectors");
        if (dir6.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("В каталоге res создана новая директория vectors")
                    .append("\n");


        File dir7 = new File("res/icons");
        if (dir7.mkdir())
            sb.append(localDateTime)
                    .append(" ")
                    .append("В каталоге res создана новая директория icons")
                    .append("\n");


        File file3 = new File("temp/temp.txt");
        try {
            if (file3.createNewFile())
                sb.append(localDateTime)
                        .append(" ")
                        .append("Файл temp.txt был создан в каталоге temp")
                        .append("\n");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        String result = sb.toString();

        try (FileOutputStream fos = new FileOutputStream("temp/temp.txt", true)) {
            byte[] bytes = result.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        GameProgress gameProgress1 = new GameProgress(70, 2, 3, 3.6);
        String path1 = "savegames/gameProgress1.dat";
        saveGame(path1, gameProgress1);


        GameProgress gameProgress2 = new GameProgress(85, 4, 5, 5.8);
        String path2 = "savegames/gameProgress2.dat";
        saveGame(path2, gameProgress2);

        GameProgress gameProgress3 = new GameProgress(90, 5, 9, 7.6);
        String path3 = "savegames/gameProgress3.dat";
        saveGame(path3, gameProgress3);

        GameProgress gameProgress4 = new GameProgress(120, 10, 21, 21.3);
        String path4 = "savegames/gameProgress4.dat";
        saveGame(path4, gameProgress4);

        List<String> pathes = new LinkedList<>();
        pathes.add(path1);
        pathes.add(path2);
        pathes.add(path3);
        pathes.add(path4);

        String zipPath = "savegames/gameSaves.zip";
        zipFiles(zipPath, pathes);


        deleteFiles(pathes);
        String openZipPath = "savegames/";
        openZip(zipPath, openZipPath);


        System.out.println(openProgress("savegames/packed_gameProgress1.dat"));
        System.out.println(openProgress("savegames/packed_gameProgress2.dat"));
        System.out.println(openProgress("savegames/packed_gameProgress3.dat"));
        System.out.println(openProgress("savegames/packed_gameProgress4.dat"));

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
            System.out.println("Файлы сохранений из архива " + zipPath + " разархивированы в каталоге savegames/");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String packed_pathes) {

        GameProgress rezult = null;
        try (FileInputStream fis = new FileInputStream(packed_pathes);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            rezult = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return rezult;
    }


    public static void saveGame(String path, GameProgress gameProgress) {

        File file = new File(path);
        try {
            if (file.createNewFile())
                System.out.println("Сохранение прогресса произведено. Файл " + path + " записан.");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFiles(List<String> pathes) {

        for (int i = 0; i < pathes.size(); i++) {
            File file = new File(pathes.get(i));

            if (file.delete())
                System.out.println("Файл " + pathes.get(i) + " удален");
        }
    }

}

