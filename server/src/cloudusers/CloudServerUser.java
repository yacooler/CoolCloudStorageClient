package cloudusers;

import database.entity.User;
import fileobjects.FileInformation;
import frames.DataFrame;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс пользователь серверной части. Содержит состояние для текущего пользователя
 */
public class CloudServerUser{
    @Getter
    private User databaseuser;

    public void setDatabaseuser(User databaseuser) {
        this.databaseuser = databaseuser;
        rootPath = Paths.get("storage", databaseuser.getName());
    }

    //Для текущего обрабатываемого файла
    @Getter @Setter
    private FileInformation currentFileInformation;

    @Getter @Setter
    private String currentFileDirectory;

    //Кадр передаваемого файла
    @Getter @Setter
    private DataFrame dataFrame;

    //Кэшированные потоки
    @Getter @Setter
    private InputStream inputStream;

    @Getter @Setter
    private OutputStream outputStream;

    //Корневой каталог
    @Getter
    Path rootPath;

    public String getDBUserLogin(){
        if (databaseuser != null){
            return databaseuser.getName();
        }
        return null;
    }
}
