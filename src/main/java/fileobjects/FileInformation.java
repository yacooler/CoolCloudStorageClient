package fileobjects;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FileInformation implements Serializable {

    private static final long serialVersionUID = -1878952550081334747L;

    public enum FileType{
        FILE("F"), DIRECTORY("D");
        private String name;

        FileType(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private String name;
    private long size;
    private FileType type;
    private LocalDateTime lastModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public FileInformation(Path path){
        try {
            this.name = path.getFileName().toString();
            this.size = Files.size(path);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if (this.type == FileType.DIRECTORY) this.size = -1;
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл " + path.toString(), e);
        }
    }


}
