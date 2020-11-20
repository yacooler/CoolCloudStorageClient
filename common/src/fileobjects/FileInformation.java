package fileobjects;

import lombok.Getter;
import lombok.Setter;

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

    @Getter @Setter
    private String name;

    @Getter @Setter
    private long size;

    @Getter @Setter
    private FileType type;

    @Getter @Setter
    private LocalDateTime lastModified;


    public FileInformation(Path path){
        try {
            if (path.getFileName() == null) {
                this.name = path.getRoot().toString();
                this.type = FileType.DIRECTORY;
                this.size = -1L;
            } else {
                this.name = path.getFileName().toString();
                this.size = Files.size(path);
                this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
                if (this.type == FileType.DIRECTORY) this.size = -1L;
                this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneId.systemDefault());
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл " + path.toString(), e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileInformation{");
        sb.append("name='").append(name).append('\'');
        sb.append(", size=").append(size);
        sb.append(", type=").append(type);
        sb.append(", lastModified=").append(lastModified);
        sb.append('}');
        return sb.toString();
    }
}
