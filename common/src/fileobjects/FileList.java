package fileobjects;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileList implements Serializable {

    private static final long serialVersionUID = -2534626971804290540L;

    private List<FileInformation> files = new ArrayList<>();

    public void add(FileInformation fileInformation){
        files.add(fileInformation);
    }

    public void addAll(List<FileInformation> list) {files.addAll(list);}

    public List<FileInformation> getFiles(){
        return files;
    }

    public Optional<FileInformation> getByFilename(String filename){
        FileInformation found = null;
        for (FileInformation fileParam: files) {
            if (fileParam.getName().equalsIgnoreCase(filename)) {
                found = fileParam;
                break;
            }
        }
        return Optional.ofNullable(found);
    }


}
